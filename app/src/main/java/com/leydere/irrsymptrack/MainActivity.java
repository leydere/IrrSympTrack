package com.leydere.irrsymptrack;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean goToFragment2;
    Button buttonNavToGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonNavToGraphView = findViewById(R.id.buttonNavToGraphView);

        buttonNavToGraphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityGraphView.class);
                startActivity(intent);
            }
        });


        /*
        //Trying to get intent from add symptom activity to navigate to fragment 2 when loaded
        try {
            Intent intent = getIntent();
            goToFragment2 = intent.getBooleanExtra("goToFragment2", false);
            Toast.makeText(MainActivity.this, "Intent Extra = " + goToFragment2, Toast.LENGTH_SHORT).show();
            //Navigation.findNavController(FragmentFirst.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            Intent i = new Intent(MainActivity.this, FragmentSecond.class);
            startActivity(i);
        } catch (Exception e) {

        }*/

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ModelIrritant modelIrritant;
                try{
                    modelIrritant = new ModelIrritant("testTitle1", "testTimeDate1", "testSeverity1");
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "input error toast", Toast.LENGTH_SHORT).show();
                    modelIrritant = new ModelIrritant("error", "error", "error");
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                boolean success = databaseHelper.addIrritantRecord(modelIrritant);

                if (success == true) {
                    Toast.makeText(MainActivity.this, "record added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "record added failure", Toast.LENGTH_SHORT).show();
                }

            }

        });*/

    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}