package com.leydere.irrsymptrack;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityGraphView extends AppCompatActivity {

    PointsGraphSeries<DataPoint> series1, series2;
    GraphView graph;
    Calendar calendar, startCalendar, endCalendar;
    Button startDateButton, endDateButton;
    FloatingActionButton fabPopulateGraph;
    TextView startDateTextView, endDateTextView, irritantSelectedTextView, symptomSelectedTextView;
    RecyclerView irritantGraphRecyclerView, symptomGraphRecyclerView;

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        fabPopulateGraph = findViewById(R.id.fabPopulateGraph);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        irritantSelectedTextView = findViewById(R.id.irritantSelectedTextView);
        symptomSelectedTextView = findViewById(R.id.symptomSelectedTextView);
        irritantGraphRecyclerView = findViewById(R.id.irritantGraphRecyclerView);
        symptomGraphRecyclerView = findViewById(R.id.symptomGraphRecyclerView);
        graph = findViewById(R.id.graph);
        series1 = new PointsGraphSeries<DataPoint>();
        series2 = new PointsGraphSeries<DataPoint>();
        calendar = Calendar.getInstance();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        databaseHelper = new DatabaseHelper(ActivityGraphView.this);

        graph.setTitle("Irr-Sym Sev v. Date");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ActivityGraphView.this));

        //set date text
        CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
        startDateTextView.setText(dateCharSequence);
        endDateTextView.setText(dateCharSequence);


        //TODO: The actual graph population logic will be called from the FAB click listener.
        series1 = symptomGraphPopulationWithUserInput(3, "2021-03-21", "2021-03-28");
        series2 = irritantGraphPopulationWithUserInput(3, "2021-03-21", "2021-03-28");
        series1.setShape(PointsGraphSeries.Shape.POINT);
        series1.setColor(Color.BLUE);
        series2.setShape(PointsGraphSeries.Shape.POINT);
        series2.setColor(Color.RED);
        graph.addSeries(series1);
        graph.addSeries(series2);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCalendarWidget(startCalendar, startDateTextView);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCalendarWidget(endCalendar, endDateTextView);
            }
        });

        fabPopulateGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public PointsGraphSeries<DataPoint> symptomGraphPopulationWithUserInput(int tagId, String startDateRange, String endDateRange) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedSymptomsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        for (ModelDataPoint modelDataPoint : selectedDataPointList) {
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from irritant calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedDataPointList.size());
        }
        return series;
    }

    public PointsGraphSeries<DataPoint> irritantGraphPopulationWithUserInput(int tagId, String startDateRange, String endDateRange) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedIrritantsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        for (ModelDataPoint modelDataPoint : selectedDataPointList) {
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from irritant calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedDataPointList.size());
        }
        return series;
    }

    private void handleCalendarWidget(Calendar calendar, TextView textView) {

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
                textView.setText(dateCharSequence);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    //region Unused experimental clutter functions

    public PointsGraphSeries<DataPoint> testPointSymptomSeriesDpModel(int tagId) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedSymptomsDateSpecificNoClock(tagId);

        for (ModelDataPoint modelDataPoint : selectedDataPointList) {
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from irritant calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedDataPointList.size());
        }

        return series;
    }

    public PointsGraphSeries<DataPoint> testPointIrritantSeriesDpModel(int tagId) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedIrritantsDateSpecificNoClock(tagId);

        for (ModelDataPoint modelDataPoint : selectedDataPointList) {
            //Toast.makeText(ActivityGraphView.this, "model date = " + modelDataPoint.getDpDate() + " model severity sum = " + modelDataPoint.getDpSeverity(), Toast.LENGTH_SHORT).show();
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //Toast.makeText(ActivityGraphView.this, "model severity sum = " + y, Toast.LENGTH_SHORT).show();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
                //Toast.makeText(ActivityGraphView.this, "model date = " + x, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from irritant calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedDataPointList.size());
        }

        return series;
    }



    //Returns selected irritant records.  Modeled after symptom function of the same purpose.
    public PointsGraphSeries<DataPoint> testPointIrritantSeries(int tagId) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelIrritant> selectedIrritantsList = databaseHelper.getSelectedIrritants(tagId);

        for (ModelIrritant modelIrritant : selectedIrritantsList) {
            //y = sev
            int severity = Integer.valueOf(modelIrritant.getIrrSeverity());
            y = severity;
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelIrritant.getIrrTimeDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedIrritantsList.size());
        }

        return series;
    }



    //This test function should be close to the final function needed.  Severity = y-axis, date = x-axis, filtered by selected tag.
    public PointsGraphSeries<DataPoint> testPointSymptomSeries(int tagId) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelSymptom> selectedSymptomsList = databaseHelper.getSelectedSymptoms(tagId);

        for (ModelSymptom modelSymptom : selectedSymptomsList) {
            //y = sev
            int severity = Integer.valueOf(modelSymptom.getSymSeverity());
            y = severity;
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelSymptom.getSymTimeDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedSymptomsList.size());
        }

        return series;
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
            int severity = Integer.valueOf(modelSymptom.getSymSeverity());
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
    private PointsGraphSeries<DataPoint> createLinearSeries(){
        int y,x;
        x = 0;
        PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>();
        // This is were the math happens. x & y is set and then appended as data points to the line graph series.
        for (int i = 0; i<6; i++){
            x = x + 1;
            y = x * 2 + 1;

            this.series1.appendData(new DataPoint(x, y), true, 6); //Youtuber says maxDataPoints attribute must equal number of data points in series
        }
        return series1;
    }
    //endregion
}
