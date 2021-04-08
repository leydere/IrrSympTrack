package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActivityRecordsListSymptoms extends AppCompatActivity {

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;
    RecyclerView symptomRecyclerView;
    FloatingActionButton fab_navToAddSymptom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_list_symptoms);

        symptomRecyclerView = findViewById(R.id.symptomRecyclerView);
        fab_navToAddSymptom = findViewById(R.id.fab_navToAddSymptom);

        databaseHelper = new DatabaseHelper(ActivityRecordsListSymptoms.this);
        allSymptomsList = getAllSymptoms();

        setAdapter();

        fab_navToAddSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRecordsListSymptoms.this, ActivityAddSymptom.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<ModelSymptom> getAllSymptoms(){
        ArrayList<ModelSymptom> arrayListToReturn = new ArrayList<>();
        arrayListToReturn.addAll(databaseHelper.getAllSymptoms());
        return arrayListToReturn;
    }

    private void setAdapter() {
        AdapterSymptomList adapter = new AdapterSymptomList(allSymptomsList, ActivityRecordsListSymptoms.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityRecordsListSymptoms.this);
        symptomRecyclerView.setLayoutManager(layoutManager);
        symptomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomRecyclerView.setAdapter(adapter);
    }

}