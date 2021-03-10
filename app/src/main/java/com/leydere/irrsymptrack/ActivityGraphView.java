package com.leydere.irrsymptrack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ActivityGraphView extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();

        series = createLinearSeries();

        graph.addSeries(series);

    }

    // In this test series the goal is to have dates along the x-axis of the graph and severity along the y-axis of the graph.
    public LineGraphSeries<DataPoint> testLineSeries1() {
        int y,x;
        x = 0;
        series = new LineGraphSeries<DataPoint>();



        return series;
    }

    public LineGraphSeries<DataPoint> createLinearSeries(){
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
}
