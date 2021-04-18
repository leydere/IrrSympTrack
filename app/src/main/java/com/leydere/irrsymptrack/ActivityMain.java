package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

/**
 * ActivityMain class is the landing page activity.  Contains buttons for navigation to other activities only.
 */
public class ActivityMain extends AppCompatActivity {

    Button buttonNavToGraphView, buttonNavToIrritantRecords, buttonNavToSymptomRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        buttonNavToGraphView = findViewById(R.id.buttonNavToGraphView);
        buttonNavToIrritantRecords = findViewById(R.id.buttonNavToIrritantRecords);
        buttonNavToSymptomRecords = findViewById(R.id.buttonNavToSymptomRecords);

        buttonNavToGraphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityGraphView.class);
                startActivity(intent);
            }
        });

        buttonNavToIrritantRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityRecordsListIrritants.class);
                startActivity(intent);
            }
        });

        buttonNavToSymptomRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityRecordsListSymptoms.class);
                startActivity(intent);
            }
        });


    }

    //region Menu support
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main_page) {
            Toast.makeText(ActivityMain.this, "Already at the main page.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.menu_irritant_records) {
            Intent intent = new Intent(ActivityMain.this, ActivityRecordsListIrritants.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_symptom_records) {
            Intent intent = new Intent(ActivityMain.this, ActivityRecordsListSymptoms.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_generate_graphs) {
            Intent intent = new Intent(ActivityMain.this, ActivityGraphView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}