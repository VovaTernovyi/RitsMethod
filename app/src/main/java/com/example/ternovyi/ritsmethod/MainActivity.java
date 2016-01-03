package com.example.ternovyi.ritsmethod;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //
    public final String TAG = "RitsMethod";

    public EditText editTextN;
    public int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = getApplicationContext();

                editTextN = (EditText) findViewById(R.id.textN);
                if (editTextN.getText().length() == 0) {
                    Toast toast = Toast.makeText(context, "N is not entered", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                } else {
                    try {
                        n = Integer.parseInt(editTextN.getText().toString());
                    } catch (NumberFormatException ex) {
                        Toast toast = Toast.makeText(context, "Incorrect format of n!", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
                calculation();
            }
        });

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

    static double function(double t)
    {
        return t - Math.sin(t);
    }

    public void calculation() {

        Func p = new Func() {
            @Override
            public double funcExecute(double x) {
                return x*x + 1;
            }
        };

        Func q = new Func() {
            @Override
            public double funcExecute(double x) {
                return Math.pow(x, 2);
            }
        };

        Func f = new Func() {
            @Override
            public double funcExecute(double x) {
                return 2*x*Math.cos(x) + Math.pow(x,3) - 2*x - Math.sin(x)*(2*Math.pow(x,2) + 1);
            }
        };

        //int n = 25;
        double down = 0;
        double up = 2*Math.PI;
        SturmLiouvilleProblem slp = new SturmLiouvilleProblem(f, p, q, down, up);
        double[] sol = slp.SolveWithFiniteElementMethod(n);
        Log.e(TAG, "Solution :");
        double h = (up-down) / n;
        double[] temp = new double[n];
        temp[0] = down + h;
        for (int j = 1; j < n; j++)
        {
            temp[j] = temp[j-1] + h;
        }
        for (int i = 0; i < n; i++)
        {
            Log.e(TAG, "x= {0} " + temp[i] + "y= {1}" + sol[i] + "Y_Tochne={2}" + function(temp[i]));
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        Random random = new Random();

        DataPoint[] dataPoint1 = new DataPoint[n + 1];
        DataPoint[] dataPoint2 = new DataPoint[n + 1];

        dataPoint1[0] = new DataPoint(0, 0);
        dataPoint2[0] = new DataPoint(0, 0);

        for (int i = 1; i < n + 1; i++) {
            dataPoint1[i] = new DataPoint(temp[i-1], sol[i-1]);
            dataPoint2[i] = new DataPoint(temp[i-1], function(temp[i-1]));
        }

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPoint1);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoint2);

        series1.setColor(Color.RED);
        graph.addSeries(series1);
        series1.setTitle("F");

        series2.setColor(Color.BLUE);
        graph.addSeries(series2);
        series2.setTitle("T");


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }
}
