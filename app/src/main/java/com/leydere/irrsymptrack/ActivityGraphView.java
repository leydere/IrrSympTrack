package com.leydere.irrsymptrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class ActivityGraphView extends AppCompatActivity implements AdapterGraphTagIrritant.OnItemClickListener, AdapterGraphTagSymptom.OnItemClickListener {

    PointsGraphSeries<DataPoint> symptomSeries, irritantSeries;
    GraphView graph;
    Calendar calendar, startCalendar, endCalendar;
    Button startDateButton, endDateButton;
    FloatingActionButton fabPopulateGraph;
    TextView startDateTextView, endDateTextView, irritantSelectedTextView, symptomSelectedTextView;
    RecyclerView irritantGraphRecyclerView, symptomGraphRecyclerView;
    ArrayList<ModelIrritantTag> irritantTagsList;
    ArrayList<ModelSymptomTag> symptomTagsList;
    int selectedIrritantTagID, selectedSymptomTagID;

    ArrayList<ModelSymptom> allSymptomsList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        Toolbar toolbar = findViewById(R.id.graphViewToolbarTop);
        setSupportActionBar(toolbar);

        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        fabPopulateGraph = findViewById(R.id.fabPopulateGraph);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        irritantSelectedTextView = findViewById(R.id.irritantSelectedTextView);
        symptomSelectedTextView = findViewById(R.id.symptomSelectedTextView);
        irritantGraphRecyclerView = findViewById(R.id.irritantGraphRecyclerView);
        symptomGraphRecyclerView = findViewById(R.id.symptomGraphRecyclerView);
        selectedIrritantTagID = -1;
        selectedSymptomTagID = -1;
        graph = findViewById(R.id.graph);
        symptomSeries = new PointsGraphSeries<DataPoint>();
        irritantSeries = new PointsGraphSeries<DataPoint>();
        calendar = Calendar.getInstance();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        databaseHelper = new DatabaseHelper(ActivityGraphView.this);

        graph.setTitle("Irr-Sym Sev v. Date");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ActivityGraphView.this));
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(45);

        //set date text
        CharSequence dateCharSequence = DateFormat.format("MM/dd/yyyy", calendar);
        startDateTextView.setText(dateCharSequence);
        endDateTextView.setText(dateCharSequence);

        //list to support recycler view
        irritantTagsList = new ArrayList<>();
        irritantTagsList.addAll(databaseHelper.getAllIrritantTags());
        setIrritantTagsSelectionAdapter();
        symptomTagsList = new ArrayList<>();
        symptomTagsList.addAll(databaseHelper.getAllSymptomTags());
        setSymptomTagsSelectionAdapter();


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
                graph.removeAllSeries();
                boolean calendarSet = false;
                CharSequence startDateCharSequence = "";
                CharSequence endDateCharSequence = "";
                int calendarComparison = startCalendar.compareTo(endCalendar);
                if (calendarComparison > 0) {
                    Toast.makeText(ActivityGraphView.this, "End date may not be before start date.  Unable to process request.", Toast.LENGTH_SHORT).show();
                } else {
                    endCalendar.add(Calendar.DATE, 1);
                    startDateCharSequence = DateFormat.format("yyyy-MM-dd", startCalendar);
                    endDateCharSequence = DateFormat.format("yyyy-MM-dd", endCalendar);
                    //TODO: Need to programmatically set the upper and lower bounds of the x-axis to correspond to the input date range.
                    //TODO: Alternatively could make the graph scrollable. Might be a reasonable solution.
                    //TODO: Possible additional formatting/override of x-axis labeler would allow for this.
                    /*
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(endCalendar.getTimeInMillis());
                    graph.getViewport().setMaxX(startCalendar.getTimeInMillis());
                    */
                    endCalendar.add(Calendar.DATE, -1);
                    calendarSet = true;
                }
                boolean tagsSelected = false;
                if (selectedIrritantTagID == -1 || selectedSymptomTagID == -1) {
                    Toast.makeText(ActivityGraphView.this, "Both an irritant and symptom tag must be selected.  Unable to process request.", Toast.LENGTH_SHORT).show();
                } else {
                    tagsSelected = true;
                }

                if (tagsSelected && calendarSet) {
                    try {
                        symptomSeries = symptomGraphPopulationWithUserInput(selectedSymptomTagID, startDateCharSequence.toString(), endDateCharSequence.toString());
                        irritantSeries = irritantGraphPopulationWithUserInput(selectedIrritantTagID, startDateCharSequence.toString(), endDateCharSequence.toString());
                        symptomSeries.setShape(PointsGraphSeries.Shape.POINT);
                        symptomSeries.setColor(Color.BLUE);
                        irritantSeries.setShape(PointsGraphSeries.Shape.POINT);
                        irritantSeries.setColor(Color.RED);
                        graph.addSeries(symptomSeries);
                        graph.addSeries(irritantSeries);
                        Toast.makeText(ActivityGraphView.this, "Graph query successful. WARNING: Queries resulting in four or less data points are known to not populate correctly to graph.", Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Toast.makeText(ActivityGraphView.this, "An issue with the calendar dates was detected. End date may not be prior to start date. Unable to process request.", Toast.LENGTH_SHORT).show();
                    }
                }
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

    private void setIrritantTagsSelectionAdapter() {
        AdapterGraphTagIrritant adapter = new AdapterGraphTagIrritant(irritantTagsList, this, this::onIrrItemClick);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        irritantGraphRecyclerView.setLayoutManager(layoutManager);
        irritantGraphRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantGraphRecyclerView.setAdapter(adapter);
    }

    private void setSymptomTagsSelectionAdapter() {
        AdapterGraphTagSymptom adapter = new AdapterGraphTagSymptom(symptomTagsList, this, this::onSymItemClick);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        symptomGraphRecyclerView.setLayoutManager(layoutManager);
        symptomGraphRecyclerView.setItemAnimator(new DefaultItemAnimator());
        symptomGraphRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onIrrItemClick(int irrTagModelID, String irrTagModelTitle) {
        selectedIrritantTagID = irrTagModelID;
        irritantSelectedTextView.setText(irrTagModelTitle);

    }

    @Override
    public void onSymItemClick(int symTagModelID, String symTagModelTitle) {
        selectedSymptomTagID = symTagModelID;
        symptomSelectedTextView.setText(symTagModelTitle);

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
            Intent intent = new Intent(ActivityGraphView.this, ActivityMain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_irritant_records) {
            Intent intent = new Intent(ActivityGraphView.this, ActivityRecordsListIrritants.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_symptom_records) {
            Intent intent = new Intent(ActivityGraphView.this, ActivityRecordsListSymptoms.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_generate_graphs) {
            Toast.makeText(ActivityGraphView.this, "Already at the generate graphs page.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

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
            this.symptomSeries.appendData(new DataPoint(x, y), true, allSymptomsList.size());
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

            this.symptomSeries.appendData(new DataPoint(x, y), true, 6); //Youtuber says maxDataPoints attribute must equal number of data points in series
        }
        return series1;
    }
    //endregion
}
