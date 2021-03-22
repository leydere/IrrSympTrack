package com.leydere.irrsymptrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectIrritantTags extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<ModelIrritantTag> availableIrritantTagsList;
    RecyclerView irritantTagAvailableRecyclerView;
    EditText irritantTagTitleEditText;
    Button addNewIrritantTagRecordButton;
    FloatingActionButton fabReturnSelectedIrrTagRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_irritant_tags);

        databaseHelper = new DatabaseHelper(ActivitySelectIrritantTags.this);
        irritantTagAvailableRecyclerView = findViewById(R.id.irrTagAvailableRecyclerView);
        irritantTagTitleEditText = findViewById(R.id.irrTagTitleEditText);
        addNewIrritantTagRecordButton = findViewById(R.id.addNewIrrTagRecordButton);
        fabReturnSelectedIrrTagRecord = findViewById(R.id.fabReturnSelectedIrrTagRecord);

        availableIrritantTagsList = getAllIrritantTags();
        setIrritantTagsAvailableAdapter();

        addNewIrritantTagRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click check to see if record exists already, if not create new record, update recycler view, and clear contents of text edit field.

                //check for blank text edit field
                boolean textFieldIsBlank;
                if (irritantTagTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(ActivitySelectIrritantTags.this, "text field is blank", Toast.LENGTH_SHORT).show();
                    textFieldIsBlank = true;
                } else {
                    textFieldIsBlank = false;
                }

                //if field is not blank check to see if record exists
                boolean recordExists;
                if (!textFieldIsBlank){
                    recordExists = doesIrritantTagRecordAlreadyExist(irritantTagTitleEditText.getText().toString());
                    if(recordExists){
                        Toast.makeText(ActivitySelectIrritantTags.this, "record exists, please enter unique tag", Toast.LENGTH_SHORT).show();
                        irritantTagTitleEditText.getText().clear();
                    }
                }
                else{recordExists = true;}

                //if text field is not blank and record does not exist create new record, clear text field, and update recycler view
                try{
                    if (!textFieldIsBlank && !recordExists){
                        //create model to go into DB
                        ModelIrritantTag modelIrritantTag;
                        try{
                            modelIrritantTag = new ModelIrritantTag(irritantTagTitleEditText.getText().toString());
                        }
                        catch (Exception e) {
                            Toast.makeText(ActivitySelectIrritantTags.this, "input error", Toast.LENGTH_SHORT).show();
                            modelIrritantTag = new ModelIrritantTag("error");
                        }

                        boolean success = databaseHelper.addIrritantTagRecord(modelIrritantTag);

                        if (success == true) {
                            Toast.makeText(ActivitySelectIrritantTags.this, "record added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivitySelectIrritantTags.this, "record added failure", Toast.LENGTH_SHORT).show();
                        }
                        // clear text view and update recycler view
                        irritantTagTitleEditText.getText().clear();
                        availableIrritantTagsList = getAllIrritantTags();
                        setIrritantTagsAvailableAdapter();

                    }
                }catch (Exception e){
                    Toast.makeText(ActivitySelectIrritantTags.this, "exception catch", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean doesIrritantTagRecordAlreadyExist(String inputText){
        List<ModelIrritantTag> allIrritantTagsList = getAllIrritantTags();
        for (ModelIrritantTag var : allIrritantTagsList) {
            String titleFound = var.getIrrTagTitle();
            if (titleFound.equals(inputText)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<ModelIrritantTag> getAllIrritantTags(){
        ArrayList<ModelIrritantTag> arrayListToReturn = new ArrayList<ModelIrritantTag>();
        arrayListToReturn.addAll(databaseHelper.getAllIrritantTags());
        return arrayListToReturn;
    }

    private void setIrritantTagsAvailableAdapter() {
        AdapterTagIrritantAvailable adapter = new AdapterTagIrritantAvailable(availableIrritantTagsList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        irritantTagAvailableRecyclerView.setLayoutManager(layoutManager);
        irritantTagAvailableRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantTagAvailableRecyclerView.setAdapter(adapter);
    }
}
