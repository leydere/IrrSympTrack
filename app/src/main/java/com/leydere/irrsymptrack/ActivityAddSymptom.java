package com.leydere.irrsymptrack;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityAddSymptom extends AppCompatActivity {

    Button dateButton, timeButton, addPictureButton;
    TextView dateTextView, timeTextView;
    EditText editTextSymptomTitle;
    FloatingActionButton fabAddSymptomRecord;
    Calendar calendar = Calendar.getInstance();
    int radioSymIdSelected;
    RadioGroup radioGroupSymptom;
    RadioButton radioButtonSymLow;
    RadioButton radioButtonSymMid;
    RadioButton radioButtonSymHigh;
    int idFromSymptomList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        //presidentList = myApplication.getPresdientList(); //going to have to go back through series on RecyclerView to figure out where this links to

        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        editTextSymptomTitle = findViewById(R.id.editTextSymptomTitle);
        fabAddSymptomRecord = findViewById(R.id.fabAddSymptomRecord);
        radioSymIdSelected = -1;
        radioGroupSymptom = findViewById(R.id.radioGroupSymptom);
        radioButtonSymLow = findViewById(R.id.radioButtonSymLow);
        radioButtonSymMid = findViewById(R.id.radioButtonSymMid);
        radioButtonSymHigh = findViewById(R.id.radioButtonSymHigh);
        addPictureButton = findViewById(R.id.addPictureButton);

        databaseHelper = new DatabaseHelper(ActivityAddSymptom.this);

        Intent intent = getIntent(); // this is for intent sent from AdapterSymptomList
        idFromSymptomList = intent.getIntExtra("id", -1); //Based on this if idFromSymptom list > -1 you can treat this as an edit.  Otherwise treat as create new.
        //President president = null;  //not sure how this will translate to my purposes. leaving in for time being (ModelSymptom maybe)
        //Toast.makeText(ActivityAddSymptom.this, "Id from pushed extra == " + idFromSymptomList, Toast.LENGTH_SHORT).show();

        //TODO: handle logic of what to do if intent has id value or does not (ie. edit a record or create a new record)
        // Maybe rather than doing it Shad's way I can just use that id to query the database and populate my object that way.  Alternate I could pass the object rather than the id number.
        if (idFromSymptomList > -1) {
            //editing a record
            ModelSymptom symptomToEdit = databaseHelper.getSingleSymptomRecord(idFromSymptomList);
            //Toast.makeText(ActivityAddSymptom.this, "TimeDate from pushed extra == " + symptomToEdit.getSymTimeDate(), Toast.LENGTH_SHORT).show();

            //set title text
            editTextSymptomTitle.setText(symptomToEdit.getSymTitle());
            //get time-date and format for use
            String symTimeDate = symptomToEdit.getSymTimeDate();
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(symTimeDate));
            } catch (Exception e) {
                Toast.makeText(ActivityAddSymptom.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            //set date text
            CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
            dateTextView.setText(dateCharSequence);
            //set time text
            CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar);
            timeTextView.setText(timeCharSequence);
            //set severity radio button
            try{
                int i = Integer.parseInt(symptomToEdit.getSymSeverity());
                if (i == 0){
                    radioButtonSymLow.setChecked(true);
                } else if (i == 1){
                    radioButtonSymMid.setChecked(true);
                } else if (i == 2){
                    radioButtonSymHigh.setChecked(true);
                }
            } catch (Exception e) {
                Toast.makeText(ActivityAddSymptom.this, "input error from severity", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            //creating a record. Not sure I even need an else statement here. This is the onCreate.  If intent id is passed do above, otherwise do not.
        }

        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                handleDateButton(calendar);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        fabAddSymptomRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO create distinction between adding a new record and editing an existing record.
                addSymptomRecordFAB(calendar);
                //TODO insert navigate back to mainactivity.secondfragment here; believe can base off FindToolsApp.AddToolActivity line 63 .requestFocus() feature
                // findViewById(R.id.)...

            }
        });

        radioGroupSymptom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonSymLow){
                    radioSymIdSelected = 0;
                }
                else if (checkedId==R.id.radioButtonSymMid){
                    radioSymIdSelected = 1;
                }
                else if (checkedId==R.id.radioButtonSymHigh){
                    radioSymIdSelected = 2;
                }
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO camera functionality goes here
            }
        });


    } //end of OnCreate

    //when floating action button is clicked this method is called from the OnClickListener; functionality to create symptom model and feed to database helper add record method is included
    private void addSymptomRecordFAB(Calendar calendar) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //TODO resolve image path for model constructor

        //create model to go into DB
        ModelSymptom modelSymptom;
        try{
            modelSymptom = new ModelSymptom(editTextSymptomTitle.getText().toString(), dateTimeString, String.valueOf(radioSymIdSelected), "");
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddSymptom.this, "input error", Toast.LENGTH_SHORT).show();
            modelSymptom = new ModelSymptom("error", "error", "error", "error");
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(ActivityAddSymptom.this);
        boolean success = databaseHelper.addSymptomRecord(modelSymptom);

        if (success == true) {
            Toast.makeText(ActivityAddSymptom.this, "record added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddSymptom.this, "record added failure", Toast.LENGTH_SHORT).show();
        }

        //tester Toast - can alter text value to my purposes
        Context context = getApplicationContext();
        CharSequence toastText = "Toast = " + String.valueOf(radioSymIdSelected);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);
        //toast.show();
    }

    private CharSequence dateTimeFormatToDB(Calendar calendar) {
        CharSequence dateCharSequence = DateFormat.format("yyyy-MM-dd", calendar);
        CharSequence timeCharSequence = DateFormat.format("HH:mm:ss.sss", calendar);
        return dateCharSequence + " " + timeCharSequence;
    }


    //Supporting time date methods

    private void handleDateButton(Calendar calendar1) {

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // below is Youtuber's format date; formats the string; not certain it is useful for DB though
                //Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);

                CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar1);
                dateTextView.setText(dateCharSequence);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);

                CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar1);
                timeTextView.setText(timeCharSequence);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }


}
