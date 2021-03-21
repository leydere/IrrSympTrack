package com.leydere.irrsymptrack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivitySelectIrritantTags extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<ModelIrritantTag> availableIrritantTagsList;
    RecyclerView irritantTagAvailableRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_irritant_tags);

        databaseHelper = new DatabaseHelper(ActivitySelectIrritantTags.this);
        irritantTagAvailableRecyclerView = findViewById(R.id.irrTagAvailableRecyclerView);

        availableIrritantTagsList = getAllIrritantTags();
        setIrritantTagsAvailableAdapter();

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
