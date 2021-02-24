package com.leydere.irrsymptrack;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ActivityAddSymptom extends AppCompatActivity {

    Button dateButton, timeButton;
    TextView dateTextView, timeTextView;
    EditText editTextSymptomTitle;
    FloatingActionButton fabAddSymptomRecord;
    Calendar calendar = Calendar.getInstance();
    int radioIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        editTextSymptomTitle = findViewById(R.id.editTextSymptomTitle);
        fabAddSymptomRecord = findViewById(R.id.fabAddSymptomRecord);
        radioIdSelected = -1;

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
            public void onClick(View view) { addSymptomRecordFAB(calendar); }
        });




    } //end of OnCreate

    //adds value to the radio button selected, must be in this format
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonSymLow:
                if (checked)
                    radioIdSelected = 0;
                break;
            case R.id.radioButtonSymMid:
                if (checked)
                    radioIdSelected = 1;
                break;
            case R.id.radioButtonSymHigh:
                if (checked)
                    radioIdSelected = 2;
                break;
        }
    }

    // uses this
    private void addSymptomRecordFAB(Calendar calendar) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //TODO resolve image path for model constructor

        //create model to go into DB
        ModelSymptom modelSymptom;
        try{
            modelSymptom = new ModelSymptom(editTextSymptomTitle.getText().toString(), dateTimeString, String.valueOf(radioIdSelected), "");
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
        CharSequence toastText = "Toast = " + String.valueOf(radioIdSelected);
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
