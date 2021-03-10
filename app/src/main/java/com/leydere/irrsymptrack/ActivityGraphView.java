package com.leydere.irrsymptrack;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityGraphView extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    GraphView graph;
    Calendar calendar;

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        calendar = Calendar.getInstance();

        databaseHelper = new DatabaseHelper(ActivityGraphView.this);
        allSymptomsList = getAllSymptoms();
        //Toast.makeText(ActivityGraphView.this, "Title of second symptom = " + allSymptomsList.get(1).getSymTitle(), Toast.LENGTH_SHORT).show(); //WORKS!!

        //series = createLinearSeries();
        series = testLineSeries1();

        graph.addSeries(series);

    }

    // In this test series the goal is to have dates along the x-axis of the graph and severity along the y-axis of the graph.
    public LineGraphSeries<DataPoint> testLineSeries1() {
        int y,x;
        x = 0;
        series = new LineGraphSeries<DataPoint>();

        //Let's try going through each DB item one by one, setting the data to ints, and appending to series.
        //In the final version I am likely to do some of the date grouping on the DB side.

        for (int i = 0; i < allSymptomsList.size(); i++) {
            ModelSymptom modelSymptom = allSymptomsList.get(i);

            //y = sev
            int severity = 1 + Integer.valueOf(modelSymptom.getSymSeverity());
            y = severity;
            //x = date
            String date = modelSymptom.getSymTimeDate();
            //set time data to calendar - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(date));
                //Toast.makeText(ActivityGraphView.this, "HERE!", Toast.LENGTH_SHORT).show(); //resolved reaching here with Calendar get instance
                CharSequence dateCharSequence = DateFormat.format("yyyyMMdd", calendar);
                int formattedDate = Integer.parseInt(dateCharSequence.toString());
                x = formattedDate;
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, allSymptomsList.size());
        }
        return series;
    }

    //First graph series function.  Testing graph population using linear function.
    private LineGraphSeries<DataPoint> createLinearSeries(){
        int y,x;
        x = 0;
        series = new LineGraphSeries<DataPoint>();
        // This is were the math happens. x & y is set and then appended as data points to the line graph series.
        for (int i = 0; i<5; i++){
            x = x + 1;
            y = x * 2 + 1;

            series.appendData(new DataPoint(x, y), true, 5); //Youtuber says maxDataPoints attribute must equal number of data points in series
        }
        return series;
    }

    private ArrayList<ModelSymptom> getAllSymptoms(){
        ArrayList<ModelSymptom> arrayListToReturn = new ArrayList<>();
        arrayListToReturn.addAll(databaseHelper.getAllSymptoms());
        return arrayListToReturn;
    }

    //region Dummy data functions
    //remnant function - Was used to add data to Symptom tag table
    private void addDummySymTagData() {
        boolean success = databaseHelper.createDummySymptomTagData();

        if (success == true) {
            Toast.makeText(ActivityGraphView.this, "dummy data added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityGraphView.this, "dummy data added failure", Toast.LENGTH_SHORT).show();
        }
    }

    //remnant function - Was used to add data to Symptom Tag Associative table
    private void addDummySymAssociativeData() {
        boolean success = databaseHelper.createDummySymptomAssociativeData();

        if (success == true) {
            Toast.makeText(ActivityGraphView.this, "dummy data added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ActivityGraphView.this, "dummy data added failure", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
}
