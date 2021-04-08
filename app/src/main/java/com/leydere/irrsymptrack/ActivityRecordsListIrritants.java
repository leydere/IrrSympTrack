package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        Toolbar toolbar = findViewById(R.id.irritantRecordsToolbar);
        setSupportActionBar(toolbar);

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

    //region Menu support
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main_page) {
            Intent intent = new Intent(ActivityRecordsListIrritants.this, ActivityMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_irritant_records) {
            Toast.makeText(ActivityRecordsListIrritants.this, "Already at the irritant records page.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.menu_symptom_records) {
            Intent intent = new Intent(ActivityRecordsListIrritants.this, ActivityRecordsListSymptoms.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_generate_graphs) {
            Intent intent = new Intent(ActivityRecordsListIrritants.this, ActivityGraphView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}