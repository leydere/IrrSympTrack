package com.leydere.irrsymptrack;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityGraphView extends AppCompatActivity {

    PointsGraphSeries<DataPoint> series1, series2;
    GraphView graph;
    Calendar calendar;

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        graph = findViewById(R.id.graph);
        series1 = new PointsGraphSeries<DataPoint>();
        series2 = new PointsGraphSeries<DataPoint>();
        calendar = Calendar.getInstance();

        // Locks the y window to 0 - 5
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        // Lock x window range.  Still in useless date data format.
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(20210301);
        graph.getViewport().setMaxX(20210308);
        //graph title
        graph.setTitle("Irr-Sym Sev v. Date");

        databaseHelper = new DatabaseHelper(ActivityGraphView.this);

        //Toast.makeText(ActivityGraphView.this, "Title of second symptom = " + allSymptomsList.get(1).getSymTitle(), Toast.LENGTH_SHORT).show(); //WORKS!!

        series1 = testPointSymptomSeries(3);
        graph.addSeries(series1);
        series1.setShape(PointsGraphSeries.Shape.POINT);
        series1.setColor(Color.BLUE);

        //So the second series cannot be created until the first series is plotted.
        series2 = testPointIrritantSeries(3);
        graph.addSeries(series2);
        series2.setShape(PointsGraphSeries.Shape.POINT);
        series2.setColor(Color.RED);

    }

    //Returns selected irritant records.  Modeled after symptom function of the same purpose.
    public PointsGraphSeries<DataPoint> testPointIrritantSeries(int tagId) {
        int y,x;
        x = 0;
        series1 = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelIrritant> selectedIrritantsList = new ArrayList<>();
        selectedIrritantsList.addAll(databaseHelper.getSelectedIrritants(tagId));
        //Believe I have created a functional query that returns the desired subset of results.

        for (int i = 0; i < selectedIrritantsList.size(); i++) {
            ModelIrritant modelIrritant = selectedIrritantsList.get(i);

            //Toast.makeText(ActivityGraphView.this, modelSymptom.getSymTitle(), Toast.LENGTH_SHORT).show();

            //y = sev
            int severity = 1 + Integer.valueOf(modelIrritant.getIrrSeverity());
            y = severity;
            //x = date
            String date = modelIrritant.getIrrTimeDate();
            //set time data to calendar - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(date));
                //Toast.makeText(ActivityGraphView.this, "HERE!", Toast.LENGTH_SHORT).show(); //resolved reaching here with Calendar get instance
                CharSequence dateCharSequence = DateFormat.format("yyyyMMdd", calendar);
                int formattedDate = Integer.parseInt(dateCharSequence.toString());
                x = formattedDate;
                //Toast.makeText(ActivityGraphView.this, dateCharSequence, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            series1.appendData(new DataPoint(x, y), true, selectedIrritantsList.size());
        }

        return series1;
    }

    //This test function should be close to the final function needed.  Severity = y-axis, date = x-axis, filtered by selected tag.
    //TODO: Almost there.  This functions does what is needed for pulling the correct records.  Time data is still unusable.  Graph still needs improvement.
    public PointsGraphSeries<DataPoint> testPointSymptomSeries(int tagId) {
        int y,x;
        x = 0;
        series1 = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelSymptom> selectedSymptomsList = new ArrayList<>();
        selectedSymptomsList.addAll(databaseHelper.getSelectedSymptoms(tagId));
        //Believe I have created a functional query that returns the desired subset of results.

        for (int i = 0; i < selectedSymptomsList.size(); i++) {
            ModelSymptom modelSymptom = selectedSymptomsList.get(i);

            //Toast.makeText(ActivityGraphView.this, modelSymptom.getSymTitle(), Toast.LENGTH_SHORT).show();

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
                //Toast.makeText(ActivityGraphView.this, dateCharSequence, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            series1.appendData(new DataPoint(x, y), true, selectedSymptomsList.size());
        }

        return series1;
    }

    // In this test series the goal is to have dates along the x-axis of the graph and severity along the y-axis of the graph.
    public LineGraphSeries<DataPoint> testLineSeries1() {
        int y,x;
        x = 0;
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();

        //Let's try going through each DB item one by one, setting the data to ints, and appending to series.
        //In the final version I am likely to do some of the date grouping on the DB side.
        allSymptomsList.addAll(databaseHelper.getAllSymptoms()); //shortcut around redundant getAllSymptoms method
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
            this.series1.appendData(new DataPoint(x, y), true, allSymptomsList.size());
        }
        return series1;
    }

    //First graph series function.  Testing graph population using linear function.
    private LineGraphSeries<DataPoint> createLinearSeries(){
        int y,x;
        x = 0;
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
        // This is were the math happens. x & y is set and then appended as data points to the line graph series.
        for (int i = 0; i<5; i++){
            x = x + 1;
            y = x * 2 + 1;

            this.series1.appendData(new DataPoint(x, y), true, 5); //Youtuber says maxDataPoints attribute must equal number of data points in series
        }
        return series1;
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
