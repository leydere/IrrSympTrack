package com.leydere.irrsymptrack;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * ActivityAddSymptom is used to create new symptom records. Edit record variant is accessed if symptom record id is passed to activity as an extra.
 * ActivityNewSymptomTags is accessible through this activity. FAB creates new record or updates existing record depending
 * on which variant of the activity has been selected.
 */
public class ActivityAddSymptom extends AppCompatActivity implements AdapterTagIrritantSelection.OnItemClickListener{

    ArrayList<ModelSymptomTag> symptomTagsList;
    ArrayList<Integer> selectedSymptomTagIDsList;
    Button dateButton, timeButton, newSymptomTagsButton;
    TextView dateTextView, timeTextView, addSymptomToolbarText;
    EditText editTextSymptomTitle;
    FloatingActionButton fabAddSymptomRecord;
    Calendar calendar = Calendar.getInstance();
    int radioSymIdSelected;
    RadioGroup radioGroupSymptom;
    RadioButton radioButtonSymLow;
    RadioButton radioButtonSymMid;
    RadioButton radioButtonSymHigh;
    int idOfExistingSymptomRecord;
    DatabaseHelper databaseHelper;
    RecyclerView symptomTagSelectionRecyclerView;

    /**
     * OnCreate recycler view is populated with existing symptom tags, fields for input are set to existing record if record id was passed to activity.
     * Otherwise time and date default to current calendar instance and other fields remain blank.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        newSymptomTagsButton = findViewById(R.id.newSymptomTagsButton);
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
        symptomTagSelectionRecyclerView = findViewById(R.id.symptomTagSelectionRecyclerView);

        databaseHelper = new DatabaseHelper(ActivityAddSymptom.this);
        selectedSymptomTagIDsList = new ArrayList<>();

        Intent intent = getIntent(); // this is for intent sent from AdapterSymptomList
        idOfExistingSymptomRecord = intent.getIntExtra("id", -1); //Based on this if idFromSymptom list > -1 you can treat this as an edit.  Otherwise treat as create new.

        // if else statement that determines if to display a record or start with blank
        // if statement that determines if to display a record or start with blank
        if (idOfExistingSymptomRecord > -1) {
            addSymptomToolbarText.setText("Edit Existing Symptom Record");
            ModelSymptom symptomToEdit = databaseHelper.getSingleSymptomRecord(idOfExistingSymptomRecord);
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
                int i = symptomToEdit.getSymSeverity();
                if (i == 1){
                    radioButtonSymLow.setChecked(true);
                    radioSymIdSelected = 1;
                } else if (i == 2){
                    radioButtonSymMid.setChecked(true);
                    radioSymIdSelected = 2;
                } else if (i == 3){
                    radioButtonSymHigh.setChecked(true);
                    radioSymIdSelected = 3;
                }
            } catch (Exception e) {
                Toast.makeText(ActivityAddSymptom.this, "input error from severity", Toast.LENGTH_SHORT).show();
            }
            //populates selected tags id list with associated symptom tag Id's here
            selectedSymptomTagIDsList = databaseHelper.getTagIDsAssociatedToThisSymptomRecord(idOfExistingSymptomRecord);
        }
        // else create a new record
        else{
            addSymptomToolbarText.setText("Add New Symptom Record");
            //set date text
            CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
            dateTextView.setText(dateCharSequence);
            //set time text
            CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar);
            timeTextView.setText(timeCharSequence);
        }

        //list to support recycler view
        symptomTagsList = new ArrayList<>();
        symptomTagsList.addAll(databaseHelper.getAllSymptomTags());
        setSymptomTagsSelectionAdapter();

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

        newSymptomTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddSymptom.this, ActivityNewSymptomTags.class);
                intent.putExtra("id", idOfExistingSymptomRecord);
                startActivity(intent);
            }
        });

        fabAddSymptomRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (idOfExistingSymptomRecord > -1){
                    updateExistingSymptomRecordFAB(calendar, selectedSymptomTagIDsList);
                }else {
                    addSymptomRecordFAB(calendar, selectedSymptomTagIDsList);
                }
                Intent intent = new Intent(ActivityAddSymptom.this, ActivityRecordsListSymptoms.class);
                startActivity(intent);
            }
        });

        radioGroupSymptom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonSymLow){
                    radioSymIdSelected = 1;
                }
                else if (checkedId==R.id.radioButtonSymMid){
                    radioSymIdSelected = 2;
                }
                else if (checkedId==R.id.radioButtonSymHigh){
                    radioSymIdSelected = 3;
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
        symptomTagsList = new ArrayList<>();
        symptomTagsList.addAll(databaseHelper.getAllSymptomTags());
        setSymptomTagsSelectionAdapter();

    }

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterTagSymptomSelection.java for more details.
     */
    private void setSymptomTagsSelectionAdapter() {
        AdapterTagSymptomSelection adapter = new AdapterTagSymptomSelection(selectedSymptomTagIDsList, symptomTagsList, this, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        symptomTagSelectionRecyclerView.setLayoutManager(layoutManager);
        symptomTagSelectionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomTagSelectionRecyclerView.setAdapter(adapter);
    }

    //region FAB supporting functions

    /**
     * Creates symptom model object, adds object to DB as new record.  Success and failure toasts for symptom record added and associative tag records added.
     * Toast for failure to create model object.
     * @param calendar
     * @param selectedSymptomTagIDsList
     */
    private void addSymptomRecordFAB(Calendar calendar, ArrayList<Integer> selectedSymptomTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelSymptom modelSymptom;
        try{
            modelSymptom = new ModelSymptom(editTextSymptomTitle.getText().toString(), dateTimeString, radioSymIdSelected, "");
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddSymptom.this, "input error", Toast.LENGTH_SHORT).show();
            modelSymptom = new ModelSymptom("error", "error", -1, "error");
        }

        //adding record now returns the new modelID instead of boolean & a -1 if it fails
        int returnedID = databaseHelper.addSymptomRecord(modelSymptom);
        if (returnedID != -1) {
            Toast.makeText(ActivityAddSymptom.this, "record added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddSymptom.this, "record added failure", Toast.LENGTH_SHORT).show();
        }

        // create associative data for new records here - uses symptom ID of newly created record & list of selected tag IDs
        int numberOfTagsIDs = selectedSymptomTagIDsList.size();
        if (numberOfTagsIDs > 0 && returnedID != -1){
            boolean associativeSuccess = databaseHelper.createSymptomTagAssociativeRecord(returnedID, selectedSymptomTagIDsList);
            if (associativeSuccess == true) {
                Toast.makeText(ActivityAddSymptom.this, "associative records added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityAddSymptom.this, "associative records failure", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Creates symptom model object, updates object to existing record in DB.  Success and failure toasts for symptom record updated and associative tag records updated.
     * Toast for failure to create model object.
     * @param calendar
     * @param selectedSymptomTagIDsList
     */
    private void updateExistingSymptomRecordFAB(Calendar calendar, ArrayList<Integer> selectedSymptomTagIDsList) {

        //format dateTime for DB
        String dateTimeString = dateTimeFormatToDB(calendar).toString();

        //create model to go into DB
        ModelSymptom modelSymptom;
        try{
            modelSymptom = new ModelSymptom(idOfExistingSymptomRecord, editTextSymptomTitle.getText().toString(), dateTimeString, radioSymIdSelected, "");
        }
        catch (Exception e) {
            Toast.makeText(ActivityAddSymptom.this, "input error", Toast.LENGTH_SHORT).show();
            modelSymptom = new ModelSymptom(-1,"error", "error", -1, "error");
        }

        boolean success = databaseHelper.updateExistingSymptomRecord(modelSymptom);

        if (success == true) {
            Toast.makeText(ActivityAddSymptom.this, "record updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityAddSymptom.this, "record updated failure", Toast.LENGTH_SHORT).show();
        }

        // create associative data for existing records here
        int numberOfTagsIDs = selectedSymptomTagIDsList.size();
        if (numberOfTagsIDs > 0){
            boolean associativeSuccess = databaseHelper.createSymptomTagAssociativeRecord(idOfExistingSymptomRecord, selectedSymptomTagIDsList);
            if (associativeSuccess == true) {
                Toast.makeText(ActivityAddSymptom.this, "associative records added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityAddSymptom.this, "associative records failure", Toast.LENGTH_SHORT).show();
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
     * OnItemClick of tag recyclerview cards updates the list used to track which tags will be associated to this symptom record when saved.
     * @param symTagModelID
     * @param tagRecordSelected
     */
    @Override
    public void onItemClick(int symTagModelID, boolean tagRecordSelected) {
        boolean listContains = selectedSymptomTagIDsList.contains(symTagModelID);
        if (tagRecordSelected && !listContains){
            selectedSymptomTagIDsList.add(symTagModelID);
        }else if(!tagRecordSelected && listContains){
            selectedSymptomTagIDsList.remove(selectedSymptomTagIDsList.indexOf(symTagModelID));
        }
    }

}
