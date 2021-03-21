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
                //TODO: on click check to see if record exists already, if not create new record, update recycler view, and clear contents of text edit field.
                //check for blank text edit field
                boolean textFieldIsBlank;
                if (irritantTagTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(ActivitySelectIrritantTags.this, "text field is blank", Toast.LENGTH_SHORT).show();
                    textFieldIsBlank = true;
                } else {
                    Toast.makeText(ActivitySelectIrritantTags.this, irritantTagTitleEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    textFieldIsBlank = false;
                }

                //if field is not blank check to see if record exists
                boolean recordExists;
                if (!textFieldIsBlank){
                    recordExists = databaseHelper.doesIrritantTagRecordAlreadyExist(irritantTagTitleEditText.getText().toString());
                }
                else{recordExists = true;}

                //if text field is not blank and record does not exist create new record
                try{
                    if (!textFieldIsBlank && !recordExists){
                        Toast.makeText(ActivitySelectIrritantTags.this, "reached top try if", Toast.LENGTH_SHORT).show();
                        //TODO: This is where the add new irr-tag record will go.  Probably a dbhelper method.
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
            Toast.makeText(ActivitySelectIrritantTags.this, "|" + titleFound + "| =? |" + inputText + "|", Toast.LENGTH_SHORT).show();
            if (titleFound.equals(inputText)){
                Toast.makeText(ActivitySelectIrritantTags.this, "reached pre-return", Toast.LENGTH_SHORT).show();
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
