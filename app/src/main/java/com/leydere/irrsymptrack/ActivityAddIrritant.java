package com.leydere.irrsymptrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

/**
 * ActivityAddIrritant is used to create new irritant records. Edit record variant is accessed if irritant record id is passed to activity as an extra.
 * ActivityNewIrritantTags is accessible through this activity. FAB creates new record or updates existing record depending
 * on which variant of the activity has been selected.
 */
public class ActivityAddIrritant extends AppCompatActivity implements AdapterTagIrritantSelection.OnItemClickListener {

    ArrayList<ModelIrritantTag> irritantTagsList;
    ArrayList<Integer> selectedIrritantTagIDsList;
    Button dateButton, timeButton, newIrritantTagsButton;
    TextView dateTextView, timeTextView, addIrritantToolbarText;
    EditText editTextIrritantTitle;
    FloatingActionButton fabAddIrritantRecord;
    Calendar calendar = Calendar.getInstance();
    int radioIrrIdSelected;
    RadioGroup radioGroupIrritant;
    RadioButton radioButtonIrrLow;
    RadioButton radioButtonIrrMid;
    RadioButton radioButtonIrrHigh;
    int idOfExistingIrritantRecord;
    DatabaseHelper databaseHelper;
    RecyclerView irritantTagSelectionRecyclerView;

    /**
     * OnCreate recycler view is populated with existing irritant tags, fields for input are set to existing record if record id was passed to activity.
     * Otherwise time and date default to current calendar instance and other fields remain blank.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irritant);
        
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        newIrritantTagsButton = findViewById(R.id.newIrritantTagsButton);
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
        selectedIrritantTagIDsList = new ArrayList<>();

        Intent intent = getIntent(); // this is for intent sent from AdapterIrritantList
        idOfExistingIrritantRecord = intent.getIntExtra("id", -1); //Based on this if idFromIrritant list > -1 you can treat this as an edit.  Otherwise treat as create new.

        // if else statement that determines if to display a record or start with blank
        // if > -1 edit existing record
        if (idOfExistingIrritantRecord > -1) {
            addIrritantToolbarText.setText("Edit Existing Irritant Record");
            ModelIrritant irritantToEdit = databaseHelper.getSingleIrritantRecord(idOfExistingIrritantRecord);
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
                int i = irritantToEdit.getIrrSeverity();
                if (i == 1){
                    radioButtonIrrLow.setChecked(true);
                    radioIrrIdSelected = 1;
                } else if (i == 2){
                    radioButtonIrrMid.setChecked(true);
                    radioIrrIdSelected = 2;
                } else if (i == 3){
                    radioButtonIrrHigh.setChecked(true);
                    radioIrrIdSelected = 3;
                }
            } catch (Exception e) {
                Toast.makeText(ActivityAddIrritant.this, "input error from severity", Toast.LENGTH_SHORT).show();
            }
            //populates selected tags id list with associated irritant tag Id's here
            selectedIrritantTagIDsList = databaseHelper.getTagIDsAssociatedToThisIrritantRecord(idOfExistingIrritantRecord);
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

        //list to support recycler view
        irritantTagsList = new ArrayList<>();
        irritantTagsList.addAll(databaseHelper.getAllIrritantTags());
        setIrritantTagsSelectionAdapter();

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

        newIrritantTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddIrritant.this, ActivityNewIrritantTags.class);
                intent.putExtra("id", idOfExistingIrritantRecord);
                startActivity(intent);
            }
        });

        fabAddIrritantRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (idOfExistingIrritantRecord > -1){
                    updateExistingIrritantRecordFAB(calendar, selectedIrritantTagIDsList);
                }else {
                    addIrritantRecordFAB(calendar, selectedIrritantTagIDsList);
                }
                Intent intent = new Intent(ActivityAddIrritant.this, ActivityRecordsListIrritants.class);
                startActivity(intent);
            }
        });

        radioGroupIrritant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonIrrLow){
                    radioIrrIdSelected = 1;
                }
                else if (checkedId==R.id.radioButtonIrrMid){
                    radioIrrIdSelected = 2;
                }
                else if (checkedId==R.id.radioButtonIrrHigh){
                    radioIrrIdSelected = 3;
                }
            }
        });
        //endregion

    } //end of OnCreate

    /**
     * OnResume the tag list and recyclerview are reset to accommodate any tags that may have been added.
     */
    @Override
    protected void onResume(){
        super.onResume();

        //list to support recycler view
        irritantTagsList = new ArrayList<>();
        irritantTagsList.addAll(databaseHelper.getAllIrritantTags());
        setIrritantTagsSelectionAdapter();

    }

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterTagIrritantSelection.java for more details.
     */
    private void setIrritantTagsSelectionAdapter() {
        AdapterTagIrritantSelection adapter = new AdapterTagIrritantSelection(selectedIrritantTagIDsList,irritantTagsList, this, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        irritantTagSelectionRecyclerView.setLayoutManager(layoutManager);
        irritantTagSelectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantTagSelectionRecyclerView.setAdapter(adapter);
    }

    //region FAB supporting functions

    /**
     * Creates irritant model object, adds object to DB as new record.  Success and failure toasts for irritant record added and associative tag records added.
     * Toast for failure to create model object.
     * @param calendar
     * @param selectedIrritantTagIDsList
     */
    private void addIrritantRecordFAB(Calendar calendar, ArrayList<Integer> selectedIrritantTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelIrritant modelIrritant;
        try{
            modelIrritant = new ModelIrritant(editTextIrritantTitle.getText().toString(), dateTimeString, radioIrrIdSelected);
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddIrritant.this, "input error", Toast.LENGTH_SHORT).show();
            modelIrritant = new ModelIrritant("error", "error", -1);
        }

        //adding record now returns the new modelID instead of boolean & a -1 if it fails
        int returnedID = databaseHelper.addIrritantRecord(modelIrritant);
        if (returnedID != -1) {
            Toast.makeText(ActivityAddIrritant.this, "record added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddIrritant.this, "record added failure", Toast.LENGTH_SHORT).show();
        }

        // create associative data for new records here - uses irritant ID of newly created record & list of selected tag IDs
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

    /**
     * Creates irritant model object, updates object to existing record in DB.  Success and failure toasts for irritant record updated and associative tag records updated.
     * Toast for failure to create model object.
     * @param calendar
     * @param selectedIrritantTagIDsList
     */
    private void updateExistingIrritantRecordFAB(Calendar calendar, ArrayList<Integer> selectedIrritantTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelIrritant modelIrritant;
        try{
            modelIrritant = new ModelIrritant(idOfExistingIrritantRecord, editTextIrritantTitle.getText().toString(), dateTimeString, radioIrrIdSelected);
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddIrritant.this, "input error", Toast.LENGTH_SHORT).show();
            modelIrritant = new ModelIrritant(-1,"error", "error", -1);
        }

        boolean success = databaseHelper.updateExistingIrritantRecord(modelIrritant);

        if (success == true) {
            Toast.makeText(ActivityAddIrritant.this, "record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddIrritant.this, "record updated failure", Toast.LENGTH_SHORT).show();
        }

        // create associative data for existing records here
        int numberOfTagsIDs = selectedIrritantTagIDsList.size();
        if (numberOfTagsIDs > 0){
            boolean associativeSuccess = databaseHelper.createIrritantTagAssociativeRecord(idOfExistingIrritantRecord, selectedIrritantTagIDsList);
            if (associativeSuccess == true) {
                Toast.makeText(ActivityAddIrritant.this, "associative records added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityAddIrritant.this, "associative records failure", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Formats the calendar data to the format required by the DB to be recognized as a datetime record.
     * @param calendar
     * @return
     */
    private CharSequence dateTimeFormatToDB(Calendar calendar) {
        CharSequence dateCharSequence = DateFormat.format("yyyy-MM-dd", calendar);
        CharSequence timeCharSequence = DateFormat.format("HH:mm:ss.sss", calendar);
        return dateCharSequence + " " + timeCharSequence;
    }

    //endregion

    //region Supporting time date methods

    /**
     * Accesses the calendar widget when date button is clicked.  Sets the calendar object to the selected date.
     * @param calendar1
     */
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

    /**
     * Accesses the clock widget when time button is clicked. Sets the calendar object to the selected time.
     * @param calendar1
     */
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

    /**
     * OnItemClick of tag recyclerview cards updates the list used to track which tags will be associated to this irritant record when saved.
     * @param irrTagModelID
     * @param tagRecordSelected
     */
    @Override
    public void onItemClick(int irrTagModelID, boolean tagRecordSelected) {
        boolean listContains = selectedIrritantTagIDsList.contains(irrTagModelID);
        if (tagRecordSelected && !listContains){
            selectedIrritantTagIDsList.add(irrTagModelID);
        }else if(!tagRecordSelected && listContains){
            selectedIrritantTagIDsList.remove(selectedIrritantTagIDsList.indexOf(irrTagModelID));
        }
    }


}
