package com.leydere.irrsymptrack;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ActivityAddIrritant extends AppCompatActivity {
    
    Button dateButton, timeButton;
    TextView dateTextView, timeTextView;
    EditText editTextIrritantTitle;
    FloatingActionButton fabAddIrritantRecord;
    Calendar calendar = Calendar.getInstance();
    int radioIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irritant);
        
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        editTextIrritantTitle = findViewById(R.id.editTextIrritantTitle);
        fabAddIrritantRecord = findViewById(R.id.fabAddIrritantRecord);
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

        fabAddIrritantRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { addIrritantRecordFAB(calendar); }
        });


        

    } //end of OnCreate

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonLow:
                if (checked)
                    radioIdSelected = 0;
                break;
            case R.id.radioButtonMid:
                if (checked)
                    radioIdSelected = 1;
                break;
            case R.id.radioButtonHigh:
                if (checked)
                    radioIdSelected = 2;
                break;
        }
    }

    //
    private void addIrritantRecordFAB(Calendar calendar) {
        //TODO add irritant records based off title text, time and date inputs, and severity radio button

        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        ModelIrritant modelIrritant;
        try{
            modelIrritant = new ModelIrritant(editTextIrritantTitle.getText().toString(), dateTimeString, String.valueOf(radioIdSelected));
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddIrritant.this, "input error", Toast.LENGTH_SHORT).show();
            modelIrritant = new ModelIrritant("error", "error", "error");
        }


        //tester Toast - can alter text value to my purposes
        Context context = getApplicationContext();
        CharSequence toastText = "Toast = " + String.valueOf(radioIdSelected);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();
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
