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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityNewIrritantTags extends AppCompatActivity implements AdapterTagIrritantList.OnItemClickListener {

    DatabaseHelper databaseHelper;
    ArrayList<ModelIrritantTag> irritantTagsList;
    RecyclerView irritantTagListRecyclerView;
    EditText irritantTagTitleEditText;
    Button addNewIrritantTagRecordButton;
    FloatingActionButton fabReturnSelectedIrrTagRecord;
    ArrayList<Integer> selectedIrritantTagIdsList; //so far no use I believe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_irritant_tags);

        databaseHelper = new DatabaseHelper(ActivityNewIrritantTags.this);
        irritantTagListRecyclerView = findViewById(R.id.irrTagListRecyclerView);
        irritantTagTitleEditText = findViewById(R.id.irrTagTitleEditText);
        addNewIrritantTagRecordButton = findViewById(R.id.addNewIrrTagRecordButton);
        fabReturnSelectedIrrTagRecord = findViewById(R.id.fabReturnSelectedIrrTagRecord);

        selectedIrritantTagIdsList = new ArrayList<>(); //so far no use I believe, this was meant to be part of a larger solution of passing selected data back a forth between adapter and activity
        //irritantTagsList = getAllIrritantTags();
        irritantTagsList = new ArrayList<>();
        irritantTagsList.addAll(databaseHelper.getAllIrritantTags());
        setIrritantTagsListAdapter();

        addNewIrritantTagRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click check to see if record exists already, if not create new record, update recycler view, and clear contents of text edit field.

                //check for blank text edit field
                boolean textFieldIsBlank;
                if (irritantTagTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(ActivityNewIrritantTags.this, "text field is blank", Toast.LENGTH_SHORT).show();
                    textFieldIsBlank = true;
                } else {
                    textFieldIsBlank = false;
                }

                //if field is not blank check to see if record exists
                boolean recordExists;
                if (!textFieldIsBlank){
                    recordExists = doesIrritantTagRecordAlreadyExist(irritantTagTitleEditText.getText().toString());
                    if(recordExists){
                        Toast.makeText(ActivityNewIrritantTags.this, "record exists, please enter unique tag", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ActivityNewIrritantTags.this, "input error", Toast.LENGTH_SHORT).show();
                            modelIrritantTag = new ModelIrritantTag("error");
                        }

                        boolean success = databaseHelper.addIrritantTagRecord(modelIrritantTag);

                        if (success == true) {
                            Toast.makeText(ActivityNewIrritantTags.this, "record added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityNewIrritantTags.this, "record added failure", Toast.LENGTH_SHORT).show();
                        }
                        // clear text view and update recycler view
                        irritantTagTitleEditText.getText().clear();
                        irritantTagsList = getAllIrritantTags();
                        setIrritantTagsListAdapter();

                    }
                }catch (Exception e){
                    Toast.makeText(ActivityNewIrritantTags.this, "exception catch", Toast.LENGTH_SHORT).show();
                }
            }
        });

        irritantTagListRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: can I make the card click functions work through here rather than in the adapter class. **probably just delete this function, dead-end**
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

    //redundant as only second line in function serves a purpose.  Creates a new list to return a list.
    private ArrayList<ModelIrritantTag> getAllIrritantTags(){
        ArrayList<ModelIrritantTag> arrayListToReturn = new ArrayList<ModelIrritantTag>();
        arrayListToReturn.addAll(databaseHelper.getAllIrritantTags());
        return arrayListToReturn;
    }

    private void setIrritantTagsListAdapter() {
        AdapterTagIrritantList adapter = new AdapterTagIrritantList(irritantTagsList, this, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.HORIZONTAL);
        irritantTagListRecyclerView.setLayoutManager(layoutManager);
        irritantTagListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantTagListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        //TODO: item position is passed from adapter to activity; used to add to list???
    }

}
