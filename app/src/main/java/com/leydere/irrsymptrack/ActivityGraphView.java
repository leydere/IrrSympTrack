package com.leydere.irrsymptrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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

/**
 * ActivityGraphView allows user to select records to be displayed in a graph. FAB populates graph with selected data. Menu navigation is enabled.
 */
public class ActivityGraphView extends AppCompatActivity implements AdapterGraphTagIrritant.OnItemClickListener, AdapterGraphTagSymptom.OnItemClickListener {

    PointsGraphSeries<DataPoint> symptomSeries, irritantSeries;
    LineGraphSeries<DataPoint> symptomLineSeries, irritantLineSeries;
    GraphView graph;
    Calendar calendar, startCalendar, endCalendar;
    Button startDateButton, endDateButton;
    FloatingActionButton fabPopulateGraph;
    TextView startDateTextView, endDateTextView, irritantSelectedTextView, symptomSelectedTextView;
    RecyclerView irritantGraphRecyclerView, symptomGraphRecyclerView;
    ArrayList<ModelIrritantTag> irritantTagsList;
    ArrayList<ModelSymptomTag> symptomTagsList;
    ArrayList<ModelDataPoint> symptomDataPoints, irritantDataPoints;
    int selectedIrritantTagID, selectedSymptomTagID;
    final int millisecondsInADay = 86400000;

    DatabaseHelper databaseHelper;

    /**
     * OnCreate onClickListeners are set, recycler views are populated with existing tag records and start and end date are defaulted to today's date.
     * @param savedInstanceState
     */
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
        symptomLineSeries = new LineGraphSeries<DataPoint>();
        irritantLineSeries = new LineGraphSeries<DataPoint>();
        symptomDataPoints = new ArrayList<ModelDataPoint>();
        irritantDataPoints = new ArrayList<ModelDataPoint>();
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

        /**
         * FAB generates graph.  Checks for minimum input requirements (end date not before start date, both irritant and symptom have been selected).
         * User prompted with toast if input requirements not met.  If conditions are met graph population is attempted. User receives toast message on success.
         * Known issues with bounds of x-axis not setting correctly with some inputs and graph sometimes not displaying all or any data points when <5 data points in selection.
         */
        fabPopulateGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                //TODO: possibly set default graph date range here if it resets when remove all series runs and the graph does not then populate; alt set it in the failed calendar if-toast below
                boolean calendarSet = false;
                boolean bothSeriesHaveData = false;
                CharSequence startDateCharSequence = "";
                CharSequence endDateCharSequence = "";
                int calendarComparison = startCalendar.compareTo(endCalendar);
                if (calendarComparison > 0) {
                    Toast.makeText(ActivityGraphView.this, "End date may not be before start date.  Unable to process request.", Toast.LENGTH_SHORT).show();
                } else {
                    endCalendar.add(Calendar.DATE, 1);
                    startDateCharSequence = DateFormat.format("yyyy-MM-dd", startCalendar);
                    endDateCharSequence = DateFormat.format("yyyy-MM-dd", endCalendar);
                    //****************************************************SETS THE X-AXIS UPPER AND LOWER BOUNDS TO SELECTED INPUT******************************************
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(startCalendar.getTimeInMillis() - millisecondsInADay);
                    graph.getViewport().setMaxX(endCalendar.getTimeInMillis() + millisecondsInADay);
                    //******************************************************************************************************************************************************
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
                        //here is where DB queries are run and series are returned
                        //TODO: need to check for symptom lengths and not populate graph if either is empty, toast user with relevant information, think the internal series list is not accessible to find size() directly
                        symptomDataPoints = symptomDataPointModelsBasedOnUserInput(selectedSymptomTagID, startDateCharSequence.toString(), endDateCharSequence.toString());
                        irritantDataPoints = irritantDataPointModelsBasedOnUserInput(selectedIrritantTagID, startDateCharSequence.toString(), endDateCharSequence.toString());

                        //TODO: length check before set true
                        int numberOfSymptomDataPoints = symptomDataPoints.size();
                        int numberOfIrritantDataPoints = irritantDataPoints.size();
                        //once minimum amount is determined can add check for if sum of both is greater than X then run otherwise catch
                        if (numberOfIrritantDataPoints > 0 && numberOfSymptomDataPoints > 0) {
                            bothSeriesHaveData = true;
                        } else {
                            if (numberOfIrritantDataPoints == 0) {
                                Toast.makeText(ActivityGraphView.this, "No irritant records found matching these parameters. Please select alternative inputs.", Toast.LENGTH_LONG).show();
                            }
                            if (numberOfSymptomDataPoints == 0) {
                                Toast.makeText(ActivityGraphView.this, "No symptom records found matching these parameters. Please select alternative inputs.", Toast.LENGTH_LONG).show();
                            }
                        }

                        //Toast.makeText(ActivityGraphView.this, "Graph query successful. WARNING: Queries resulting in four or less data points are known to not populate correctly to graph.", Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Toast.makeText(ActivityGraphView.this, "An issue with the data series was detected. Unable to process request.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (bothSeriesHaveData) {
                    try {
                        //TODO: make above check for series lengths and move graph population to here to run if all good
                        symptomSeries = symptomPointsGraphDataSeries(symptomDataPoints);
                        irritantSeries = irritantPointsGraphDataSeries(irritantDataPoints);

                        symptomSeries.setShape(PointsGraphSeries.Shape.POINT);
                        symptomSeries.setColor(Color.BLUE);
                        irritantSeries.setShape(PointsGraphSeries.Shape.POINT);
                        irritantSeries.setColor(Color.RED);
                        graph.addSeries(symptomSeries);
                        graph.addSeries(irritantSeries);

                        symptomLineSeries = symptomLineGraphDataSeries(symptomDataPoints);
                        irritantLineSeries = irritantLineGraphDataSeries(irritantDataPoints);
                        symptomLineSeries.setColor(Color.BLUE);
                        irritantLineSeries.setColor(Color.RED);
                        graph.addSeries(symptomLineSeries);
                        graph.addSeries(irritantLineSeries);

                    } catch (Exception e){

                    }
                }
            }
        });

    }


    //TODO: remove function
    /**
     * Requests DB query using symptom tag ID and date range.  Results should be returned as data point model objects that are derived from symptom records as severity sums grouped by dates.
     * @param tagId
     * @param startDateRange
     * @param endDateRange
     * @return
     */
    public PointsGraphSeries<DataPoint> symptomGraphPopulationWithUserInput(int tagId, String startDateRange, String endDateRange) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedSymptomsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        //TODO: This is where I could check for the number of returned records. However the function is set up to return a data point series.  So either have to try catch when this is run to avoid crash or break into two functions.
        Toast.makeText(ActivityGraphView.this, "symptom series length = " + selectedDataPointList.size(), Toast.LENGTH_SHORT).show();

        //TODO: split this method above-below; top section returns array list of data point models; check for size of list performed outside of function; that list is fed to bottom section to return a graph series

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
                Toast.makeText(ActivityGraphView.this, "input error from symptom calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, selectedDataPointList.size());
        }
        return series;
    }

    //TODO: XML comment the four new functions below, remove two unused functions
    //top half of split function above
    public ArrayList<ModelDataPoint> symptomDataPointModelsBasedOnUserInput(int tagId, String startDateRange, String endDateRange) {
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedSymptomsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        return selectedDataPointList;
    }

    //bottom half of split function above
    public PointsGraphSeries<DataPoint> symptomPointsGraphDataSeries(ArrayList<ModelDataPoint> inputList) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();

        for (ModelDataPoint modelDataPoint : inputList) {
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from symptom calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, inputList.size());
        }
        return series;
    }

    //top half of split function below
    public ArrayList<ModelDataPoint> irritantDataPointModelsBasedOnUserInput(int tagId, String startDateRange, String endDateRange) {
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedIrritantsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        return selectedDataPointList;
    }

    //bottom half of split function below
    public PointsGraphSeries<DataPoint> irritantPointsGraphDataSeries(ArrayList<ModelDataPoint> inputList) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();

        for (ModelDataPoint modelDataPoint : inputList) {
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
            series.appendData(new DataPoint(x, y), true, inputList.size());
        }
        return series;
    }

    //bottom half of split function above
    public LineGraphSeries<DataPoint> symptomLineGraphDataSeries(ArrayList<ModelDataPoint> inputList) {
        int y;
        Date x = new Date();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

        for (ModelDataPoint modelDataPoint : inputList) {
            //y = sev
            y = modelDataPoint.getDpSeverity();
            //x = date
            //set db time data to calendar format - took this from the load existing record to add symptom activity
            SimpleDateFormat dbStringToCalendar = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
            try{
                calendar.setTime(dbStringToCalendar.parse(modelDataPoint.getDpDate()));
                x = calendar.getTime();
            } catch (Exception e) {
                Toast.makeText(ActivityGraphView.this, "input error from symptom calendar", Toast.LENGTH_SHORT).show();
            }
            series.appendData(new DataPoint(x, y), true, inputList.size());
        }
        return series;
    }

    //bottom half of split function below
    public LineGraphSeries<DataPoint> irritantLineGraphDataSeries(ArrayList<ModelDataPoint> inputList) {
        int y;
        Date x = new Date();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

        for (ModelDataPoint modelDataPoint : inputList) {
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
            series.appendData(new DataPoint(x, y), true, inputList.size());
        }
        return series;
    }

    //TODO: remove function
    /**
     * Requests DB query using irritant tag ID and date range.  Results should be returned as data point model objects that are derived from irritant records as severity sums grouped by dates.
     * @param tagId
     * @param startDateRange
     * @param endDateRange
     * @return
     */
    public PointsGraphSeries<DataPoint> irritantGraphPopulationWithUserInput(int tagId, String startDateRange, String endDateRange) {
        int y;
        Date x = new Date();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
        ArrayList<ModelDataPoint> selectedDataPointList = databaseHelper.getSelectedIrritantsByDateRangeAndTagId(tagId, startDateRange, endDateRange);

        Toast.makeText(ActivityGraphView.this, "irritant series length = " + selectedDataPointList.size(), Toast.LENGTH_SHORT).show();

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

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterGraphTagIrritant.java for more details.
     */
    private void setIrritantTagsSelectionAdapter() {
        AdapterGraphTagIrritant adapter = new AdapterGraphTagIrritant(irritantTagsList, this, this::onIrrItemClick);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        irritantGraphRecyclerView.setLayoutManager(layoutManager);
        irritantGraphRecyclerView.setItemAnimator(new DefaultItemAnimator());
        irritantGraphRecyclerView.setAdapter(adapter);
    }

    /**
     * Defines settings for the recyclerview including what it is populated with and how onclick events are handled.  See AdapterGraphTagSymptom.java for more details.
     */
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

    /**
     * Accesses the calendar widget when date button is clicked.  Sets the calendar object to the selected date.
     * @param calendar
     * @param textView
     */
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

}
