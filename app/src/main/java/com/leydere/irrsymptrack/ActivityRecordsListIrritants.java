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

public class ActivityRecordsListIrritants extends AppCompatActivity {

    ArrayList<ModelIrritant> allIrritantsList;
    DatabaseHelper databaseHelper;
    RecyclerView irritantRecyclerView;
    FloatingActionButton fab_navToAddIrritant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_list_irritants);

        irritantRecyclerView = findViewById(R.id.irritantRecyclerView);
        fab_navToAddIrritant = findViewById(R.id.fab_navToAddIrritant);

        databaseHelper = new DatabaseHelper(ActivityRecordsListIrritants.this);
        allIrritantsList = getAllIrritants();

        setAdapter();

        fab_navToAddIrritant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRecordsListIrritants.this, ActivityAddIrritant.class);
                startActivity(intent);
            }
        });


    }

   private ArrayList<ModelIrritant> getAllIrritants(){
        ArrayList<ModelIrritant> arrayListToReturn = new ArrayList<ModelIrritant>();
        arrayListToReturn.addAll(databaseHelper.getAllIrritants());
        return arrayListToReturn;
    }

    private void setAdapter() {
        AdapterIrritantList adapter = new AdapterIrritantList(allIrritantsList, ActivityRecordsListIrritants.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityRecordsListIrritants.this);
        irritantRecyclerView.setLayoutManager(layoutManager);
        irritantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantRecyclerView.setAdapter(adapter);
    }


}