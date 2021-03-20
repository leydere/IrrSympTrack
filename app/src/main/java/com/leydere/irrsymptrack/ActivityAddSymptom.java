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

    Button dateButton, timeButton;
    TextView dateTextView, timeTextView, addSymptomToolbarText;
    EditText editTextSymptomTitle;
    FloatingActionButton fabAddSymptomRecord;
    Calendar calendar = Calendar.getInstance();  //TODO: potential pain point of clock issue, maybe get instance later or provide if statement to get instance if new and get old data if existing
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
        addSymptomToolbarText = findViewById(R.id.addSymptomToolbarText);
        editTextSymptomTitle = findViewById(R.id.editTextSymptomTitle);
        fabAddSymptomRecord = findViewById(R.id.fabAddSymptomRecord);
        radioSymIdSelected = -1;
        radioGroupSymptom = findViewById(R.id.radioGroupSymptom);
        radioButtonSymLow = findViewById(R.id.radioButtonSymLow);
        radioButtonSymMid = findViewById(R.id.radioButtonSymMid);
        radioButtonSymHigh = findViewById(R.id.radioButtonSymHigh);

        databaseHelper = new DatabaseHelper(ActivityAddSymptom.this);

        Intent intent = getIntent(); // this is for intent sent from AdapterSymptomList
        idFromSymptomList = intent.getIntExtra("id", -1); //Based on this if idFromSymptom list > -1 you can treat this as an edit.  Otherwise treat as create new.
        //President president = null;  //not sure how this will translate to my purposes. leaving in for time being (ModelSymptom maybe)
        //Toast.makeText(ActivityAddSymptom.this, "Id from pushed extra == " + idFromSymptomList, Toast.LENGTH_SHORT).show();

        //if statement that determines if to display a record or start with blank
        if (idFromSymptomList > -1) {
            addSymptomToolbarText.setText("Edit Existing Symptom Record");
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
                //Toast.makeText(ActivityAddSymptom.this, "Sev record is: " + symptomToEdit.getSymSeverity(), Toast.LENGTH_SHORT).show();
                int i = Integer.parseInt(symptomToEdit.getSymSeverity());
                if (i == 0){
                    radioButtonSymLow.setChecked(true);
                    radioSymIdSelected = 0;
                } else if (i == 1){
                    radioButtonSymMid.setChecked(true);
                    radioSymIdSelected = 1;
                } else if (i == 2){
                    radioButtonSymHigh.setChecked(true);
                    radioSymIdSelected = 2;
                }
                //Toast.makeText(ActivityAddSymptom.this, "Radio selected is: " + radioSymIdSelected, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ActivityAddSymptom.this, "input error from severity", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            addSymptomToolbarText.setText("Add New Symptom Record");
            //TODO: populate time-date textviews based on current time
            //set date text
            CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
            dateTextView.setText(dateCharSequence);
            //set time text
            CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar);
            timeTextView.setText(timeCharSequence);
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
                handleTimeButton(calendar);
            }
        });

        fabAddSymptomRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (idFromSymptomList > -1){
                    updateExistingSymptomRecordFAB(calendar);
                }else {
                    addSymptomRecordFAB(calendar);
                }


                //TODO insert navigate back to mainactivity.secondfragment here; believe can base off FindToolsApp.AddToolActivity line 63 .requestFocus() feature
                // findViewById(R.id.)...

                /*
                //intent and extra sent back but ran into issue of how to navigate to specific fragment from an activity.
                Intent goBackToMainFragment2 = new Intent(ActivityAddSymptom.this, MainActivity.class);
                goBackToMainFragment2.putExtra("goToFragment2", true);
                startActivity(goBackToMainFragment2);*/

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

    }

    private void updateExistingSymptomRecordFAB(Calendar calendar) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //TODO resolve image path for model constructor

        //create model to go into DB
        ModelSymptom modelSymptom;
        try{
            modelSymptom = new ModelSymptom(idFromSymptomList, editTextSymptomTitle.getText().toString(), dateTimeString, String.valueOf(radioSymIdSelected), "");
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddSymptom.this, "input error", Toast.LENGTH_SHORT).show();
            modelSymptom = new ModelSymptom(-1,"error", "error", "error", "error");
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(ActivityAddSymptom.this);
        boolean success = databaseHelper.updateExistingSymptomRecord(modelSymptom);

        if (success == true) {
            Toast.makeText(ActivityAddSymptom.this, "record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddSymptom.this, "record updated failure", Toast.LENGTH_SHORT).show();
        }
    }


    private CharSequence dateTimeFormatToDB(Calendar calendar) {
        CharSequence dateCharSequence = DateFormat.format("yyyy-MM-dd", calendar);
        CharSequence timeCharSequence = DateFormat.format("HH:mm:ss.sss", calendar);
        return dateCharSequence + " " + timeCharSequence;
    }


    //region Supporting time date methods
    private void handleDateButton(Calendar calendar1) {

        int YEAR = calendar1.get(Calendar.YEAR);
        int MONTH = calendar1.get(Calendar.MONTH);
        int DATE = calendar1.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);

                CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar1);
                dateTextView.setText(dateCharSequence);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void handleTimeButton(Calendar calendar1) {
        int HOUR = calendar1.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar1.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);

                CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar1);
                timeTextView.setText(timeCharSequence);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }
    //endregion


}
