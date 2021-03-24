package com.leydere.irrsymptrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityAddIrritant extends AppCompatActivity implements AdapterTagIrritantSelection.OnItemClickListener {

    ArrayList<ModelIrritantTag> irritantTagsList;
    ArrayList<Integer> selectedIrritantTagIDsList;
    Button dateButton, timeButton, selectIrritantTagsButton;
    TextView dateTextView, timeTextView, addIrritantToolbarText;
    EditText editTextIrritantTitle;
    FloatingActionButton fabAddIrritantRecord;
    Calendar calendar = Calendar.getInstance();
    int radioIrrIdSelected;
    RadioGroup radioGroupIrritant;
    RadioButton radioButtonIrrLow;
    RadioButton radioButtonIrrMid;
    RadioButton radioButtonIrrHigh;
    int idFromIrritantList, idFromAvailableIrrTag;
    DatabaseHelper databaseHelper;
    RecyclerView irritantTagSelectionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irritant);
        
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        selectIrritantTagsButton = findViewById(R.id.selectIrritantTagsButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        addIrritantToolbarText = findViewById(R.id.addIrritantToolbarText);
        editTextIrritantTitle = findViewById(R.id.editTextIrritantTitle);
        fabAddIrritantRecord = findViewById(R.id.fabAddIrritantRecord);
        radioIrrIdSelected = -1;
        radioGroupIrritant = findViewById(R.id.radioGroupIrritant);
        radioButtonIrrLow = findViewById(R.id.radioButtonIrrLow);
        radioButtonIrrMid = findViewById(R.id.radioButtonIrrMid);
        radioButtonIrrHigh = findViewById(R.id.radioButtonIrrHigh);
        irritantTagSelectionRecyclerView = findViewById(R.id.irritantTagSelectionRecyclerView);

        databaseHelper = new DatabaseHelper(ActivityAddIrritant.this);

        Intent intent = getIntent(); // this is for intent sent from AdapterIrritantList
        idFromIrritantList = intent.getIntExtra("id", -1); //Based on this if idFromIrritant list > -1 you can treat this as an edit.  Otherwise treat as create new.
        //TODO: where did this IrrTag intent come from?  Starting to look like I created it here but never implemented a source.
        idFromAvailableIrrTag = intent.getIntExtra("idFromAvailableIrrTag", -1);

        //TODO: after get intent, but before set adapter - create the new list (set with data inside the if record exists statement)
        selectedIrritantTagIDsList = new ArrayList<>();

        //list to support recycler view
        irritantTagsList = new ArrayList<>();
        irritantTagsList.addAll(databaseHelper.getAllIrritantTags());
        setIrritantTagsSelectionAdapter();
        //TODO: color irritant tag list accordingly if existing record and has associated tag data

        // if else statement that determines if to display a record or start with blank
        // if > -1 edit existing record
        if (idFromIrritantList > -1) {
            addIrritantToolbarText.setText("Edit Existing Irritant Record");
            //editing a record
            ModelIrritant irritantToEdit = databaseHelper.getSingleIrritantRecord(idFromIrritantList);

            //set title text
            editTextIrritantTitle.setText(irritantToEdit.getIrrTitle());
            //get time-date and format for use
            String irrTimeDate = irritantToEdit.getIrrTimeDate();
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(irrTimeDate));
            } catch (Exception e) {
                Toast.makeText(ActivityAddIrritant.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            //set date text
            CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
            dateTextView.setText(dateCharSequence);
            //set time text
            CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar);
            timeTextView.setText(timeCharSequence);
            //set severity radio button
            try{
                int i = Integer.parseInt(irritantToEdit.getIrrSeverity());
                if (i == 0){
                    radioButtonIrrLow.setChecked(true);
                    radioIrrIdSelected = 0;
                } else if (i == 1){
                    radioButtonIrrMid.setChecked(true);
                    radioIrrIdSelected = 1;
                } else if (i == 2){
                    radioButtonIrrHigh.setChecked(true);
                    radioIrrIdSelected = 2;
                }
            } catch (Exception e) {
                Toast.makeText(ActivityAddIrritant.this, "input error from severity", Toast.LENGTH_SHORT).show();
            }

        }
        // else create a new record
        else{
            addIrritantToolbarText.setText("Add New Irritant Record");
            //set date text
            CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
            dateTextView.setText(dateCharSequence);
            //set time text
            CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar);
            timeTextView.setText(timeCharSequence);
        }

        //region on click listeners
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

        selectIrritantTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddIrritant.this, ActivityNewIrritantTags.class);
                startActivity(intent);
            }
        });

        fabAddIrritantRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (idFromIrritantList > -1){
                    updateExistingIrritantRecordFAB(calendar, selectedIrritantTagIDsList);
                }else {
                    addIrritantRecordFAB(calendar, selectedIrritantTagIDsList);
                }


                //TODO insert navigate back to mainactivity.firstfragment here; believe can base off FindToolsApp.AddToolActivity line 63 .requestFocus() feature
                // findViewById(R.id.)...

            }
        });

        radioGroupIrritant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonIrrLow){
                    radioIrrIdSelected = 0;
                }
                else if (checkedId==R.id.radioButtonIrrMid){
                    radioIrrIdSelected = 1;
                }
                else if (checkedId==R.id.radioButtonIrrHigh){
                    radioIrrIdSelected = 2;
                }
            }
        });
        //endregion

    } //end of OnCreate

    private void setIrritantTagsSelectionAdapter() {
        AdapterTagIrritantSelection adapter = new AdapterTagIrritantSelection(irritantTagsList, this, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        irritantTagSelectionRecyclerView.setLayoutManager(layoutManager);
        irritantTagSelectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantTagSelectionRecyclerView.setAdapter(adapter);
    }

    //region FAB supporting functions
    private void addIrritantRecordFAB(Calendar calendar, ArrayList<Integer> selectedIrritantTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelIrritant modelIrritant;
        try{
            modelIrritant = new ModelIrritant(editTextIrritantTitle.getText().toString(), dateTimeString, String.valueOf(radioIrrIdSelected));
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddIrritant.this, "input error", Toast.LENGTH_SHORT).show();
            modelIrritant = new ModelIrritant("error", "error", "error");
        }

        //adding record now returns the new modelID instead of boolean & a -1 if it fails
        int returnedID = databaseHelper.addIrritantRecord(modelIrritant);
        Toast.makeText(ActivityAddIrritant.this, "returned ID " + returnedID, Toast.LENGTH_SHORT).show();
        if (returnedID != -1) {
            Toast.makeText(ActivityAddIrritant.this, "record added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddIrritant.this, "record added failure", Toast.LENGTH_SHORT).show();
        }

        /*
        boolean success = databaseHelper.addIrritantRecord(modelIrritant);

        if (success == true) {
            Toast.makeText(ActivityAddIrritant.this, "record added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddIrritant.this, "record added failure", Toast.LENGTH_SHORT).show();
        }

         */
        //TODO: create associative data for new records here !!!NEED RECORD ID OF NEWLY CREATED RECORD!!!
        int numberOfTagsIDs = selectedIrritantTagIDsList.size();
        if (numberOfTagsIDs > 0 && returnedID != -1){
            boolean associativeSuccess = databaseHelper.createIrritantTagAssociativeRecord(returnedID, selectedIrritantTagIDsList);
            if (associativeSuccess == true) {
                Toast.makeText(ActivityAddIrritant.this, "associative records added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityAddIrritant.this, "associative records failure", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateExistingIrritantRecordFAB(Calendar calendar, ArrayList<Integer> selectedIrritantTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelIrritant modelIrritant;
        try{
            modelIrritant = new ModelIrritant(idFromIrritantList, editTextIrritantTitle.getText().toString(), dateTimeString, String.valueOf(radioIrrIdSelected));
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddIrritant.this, "input error", Toast.LENGTH_SHORT).show();
            modelIrritant = new ModelIrritant(-1,"error", "error", "error");
        }

        boolean success = databaseHelper.updateExistingIrritantRecord(modelIrritant);

        if (success == true) {
            Toast.makeText(ActivityAddIrritant.this, "record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddIrritant.this, "record updated failure", Toast.LENGTH_SHORT).show();
        }

        //TODO: create associative data for existing records here

    }

    private CharSequence dateTimeFormatToDB(Calendar calendar) {
        CharSequence dateCharSequence = DateFormat.format("yyyy-MM-dd", calendar);
        CharSequence timeCharSequence = DateFormat.format("HH:mm:ss.sss", calendar);
        return dateCharSequence + " " + timeCharSequence;
    }

    //endregion

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

    @Override
    public void onItemClick(int irrTagModelID, boolean tagRecordSelected) {
        boolean listContains = selectedIrritantTagIDsList.contains(irrTagModelID);
        if (tagRecordSelected && !listContains){
            selectedIrritantTagIDsList.add(irrTagModelID);
        }else if(!tagRecordSelected && listContains){
            selectedIrritantTagIDsList.remove(selectedIrritantTagIDsList.indexOf(irrTagModelID));
        }

        /*
        Toast.makeText(ActivityAddIrritant.this, "ID = " + irrTagModelID + ", isSelected = " + tagRecordSelected, Toast.LENGTH_SHORT).show();

        try{
            int i = selectedIrritantTagIDsList.size() - 1;
            Toast.makeText(ActivityAddIrritant.this, "list ends with =  " + selectedIrritantTagIDsList.get(i), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(ActivityAddIrritant.this, "failed", Toast.LENGTH_SHORT).show();
        }
        */

    }


}
