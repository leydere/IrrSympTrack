package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityNewSymptomTags is used to create new symptom tag records.  Add button creates new record. FAB returns to ActivityAddSymptom.
 */
public class ActivityNewSymptomTags extends AppCompatActivity implements AdapterTagSymptomList.OnItemClickListener {

    DatabaseHelper databaseHelper;
    ArrayList<ModelSymptomTag> symptomTagsList;
    RecyclerView symptomTagListRecyclerView;
    EditText symptomTagTitleEditText;
    Button addNewSymptomTagRecordButton;
    FloatingActionButton fabReturnSelectedSymTagRecord;
    int idOfExistingSymptomRecord;

    /**
     * OnCreate recycler view is populated with existing symptom tags and on click listeners are set.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_symptom_tags);

        databaseHelper = new DatabaseHelper(ActivityNewSymptomTags.this);
        symptomTagListRecyclerView = findViewById(R.id.symTagListRecyclerView);
        symptomTagTitleEditText = findViewById(R.id.symTagTitleEditText);
        addNewSymptomTagRecordButton = findViewById(R.id.addNewSymTagRecordButton);
        fabReturnSelectedSymTagRecord = findViewById(R.id.fabReturnSelectedSymTagRecord);

        Intent intent = getIntent();
        idOfExistingSymptomRecord = intent.getIntExtra("id", -1); //record Id from add record activity - brought here to be passed back if using navigate button

        symptomTagsList = new ArrayList<>();
        symptomTagsList.addAll(databaseHelper.getAllSymptomTags());
        setSymptomTagsListAdapter();

        addNewSymptomTagRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click check to see if record exists already, if not create new record, update recycler view, and clear contents of text edit field.

                //check for blank text edit field
                boolean textFieldIsBlank;
                if (symptomTagTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(ActivityNewSymptomTags.this, "text field is blank", Toast.LENGTH_SHORT).show();
                    textFieldIsBlank = true;
                } else {
                    textFieldIsBlank = false;
                }

                //if field is not blank check to see if record exists
                boolean recordExists;
                if (!textFieldIsBlank){
                    recordExists = doesSymptomTagRecordAlreadyExist(symptomTagTitleEditText.getText().toString());
                    if(recordExists){
                        Toast.makeText(ActivityNewSymptomTags.this, "record exists, please enter unique tag", Toast.LENGTH_SHORT).show();
                        symptomTagTitleEditText.getText().clear();
                    }
                }
                else{recordExists = true;}

                //if text field is not blank and record does not exist create new record, clear text field, and update recycler view
                try{
                    if (!textFieldIsBlank && !recordExists){
                        //create model to go into DB
                        ModelSymptomTag modelSymptomTag;
                        try{
                            modelSymptomTag = new ModelSymptomTag(symptomTagTitleEditText.getText().toString());
                        }
                        catch (Exception e) {
                            Toast.makeText(ActivityNewSymptomTags.this, "input error", Toast.LENGTH_SHORT).show();
                            modelSymptomTag = new ModelSymptomTag("error");
                        }

                        boolean success = databaseHelper.addSymptomTagRecord(modelSymptomTag);

                        if (success == true) {
                            Toast.makeText(ActivityNewSymptomTags.this, "record added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityNewSymptomTags.this, "record added failure", Toast.LENGTH_SHORT).show();
                        }
                        // clear text view and update recycler view
                        symptomTagTitleEditText.getText().clear();
                        symptomTagsList = getAllSymptomTags();
                        setSymptomTagsListAdapter();

                    }
                }catch (Exception e){
                    Toast.makeText(ActivityNewSymptomTags.this, "exception catch", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabReturnSelectedSymTagRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNewSymptomTags.this, ActivityAddSymptom.class);
                intent.putExtra("id", idOfExistingSymptomRecord);
                ActivityNewSymptomTags.this.startActivity(intent);
            }
        });
    }

    /**
     * Checks for duplicate records to support duplicate records not being created.
     * @param inputText
     * @return
     */
    public boolean doesSymptomTagRecordAlreadyExist(String inputText){
        List<ModelSymptomTag> allSymptomTagsList = getAllSymptomTags();
        for (ModelSymptomTag var : allSymptomTagsList) {
            String titleFound = var.getSymTagTitle();
            if (titleFound.equals(inputText)){
                return true;
            }
        }
        return false;
    }

    //TODO: see if can remove this after editing above function
    //redundant as only second line in function serves a purpose.  Creates a new list to return a list.
    private ArrayList<ModelSymptomTag> getAllSymptomTags(){
        ArrayList<ModelSymptomTag> arrayListToReturn = new ArrayList<>();
        arrayListToReturn.addAll(databaseHelper.getAllSymptomTags());
        return arrayListToReturn;
    }

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterTagSymptomList.java for more details.
     */
    private void setSymptomTagsListAdapter() {
        AdapterTagSymptomList adapter = new AdapterTagSymptomList(symptomTagsList, this, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.HORIZONTAL);
        symptomTagListRecyclerView.setLayoutManager(layoutManager);
        symptomTagListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomTagListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        //TODO: Remove onItemClick and test functionality.
    }

}
