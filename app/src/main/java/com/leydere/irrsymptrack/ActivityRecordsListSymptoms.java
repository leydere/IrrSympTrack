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

/**
 * ActivityRecordsListSymptoms displays all symptom records in a recycler view.  Record cards can be clicked to edit records.  FAB can be clicked to create a new record.  Menu navigation is enabled.
 */
public class ActivityRecordsListSymptoms extends AppCompatActivity {

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;
    RecyclerView symptomRecyclerView;
    FloatingActionButton fab_navToAddSymptom;

    /**
     * OnCreate the FAB onClickListener and recyclerview adapter are set. The adapter populates the recyclerview and enables its onClickListener.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_list_symptoms);
        Toolbar toolbar = findViewById(R.id.symptomRecordsToolbar);
        setSupportActionBar(toolbar);

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

    /**
     * Returns list of symptoms to be displayed in the recyclerview.
     * @return
     */
    private ArrayList<ModelSymptom> getAllSymptoms(){
        ArrayList<ModelSymptom> arrayListToReturn = new ArrayList<>();
        arrayListToReturn.addAll(databaseHelper.getAllSymptoms());
        return arrayListToReturn;
    }

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterSymptomList.java for more details.
     */
    private void setAdapter() {
        AdapterSymptomList adapter = new AdapterSymptomList(allSymptomsList, ActivityRecordsListSymptoms.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityRecordsListSymptoms.this);
        symptomRecyclerView.setLayoutManager(layoutManager);
        symptomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomRecyclerView.setAdapter(adapter);
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
            Intent intent = new Intent(ActivityRecordsListSymptoms.this, ActivityMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_irritant_records) {
            Intent intent = new Intent(ActivityRecordsListSymptoms.this, ActivityRecordsListIrritants.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_symptom_records) {
            Toast.makeText(ActivityRecordsListSymptoms.this, "Already at the symptom records page.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.menu_generate_graphs) {
            Intent intent = new Intent(ActivityRecordsListSymptoms.this, ActivityGraphView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}