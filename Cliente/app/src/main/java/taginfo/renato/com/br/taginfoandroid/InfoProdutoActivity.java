package taginfo.renato.com.br.taginfoandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class InfoProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_produto);

        BarChart chart = (BarChart) findViewById(R.id.chartLine);


        chart.setDrawGridBackground(false);

        // no description text


        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        //chart.setBackgroundColor(Color.GRAY);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        List<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry(1f, 10f));

        List<BarEntry> entries1 = new ArrayList<BarEntry>();

        entries1.add(new BarEntry(2f, 20f));

        List<BarEntry> entries2 = new ArrayList<BarEntry>();

        entries2.add(new BarEntry(3f, 30f));


        BarDataSet set1, set2, set3;

        set1 = new BarDataSet(entries, "Company A");
        // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
        // ColorTemplate.FRESH_COLORS));
        set1.setColor(Color.rgb(104, 241, 175));
        set2 = new BarDataSet(entries1, "Company B");
        set2.setColor(Color.rgb(164, 0, 251));
        set3 = new BarDataSet(entries2, "Company C");
        set3.setColor(Color.rgb(0, 247, 158));


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        // BarDataSet dataSet = new BarDataSet(entries,"Coisas");

        //LineDataSet dataSet = new LineDataSet(entries, "Valores Aliatorios");

        // dataSet.setColor(Color.BLUE);
        BarData barData = new BarData(dataSets);
        barData.setValueTextColor(Color.GREEN);
        barData.setDrawValues(true);

        chart.setData(barData);
        chart.invalidate(); // refresh

        chart.notifyDataSetChanged();
        chart.getData().notifyDataChanged();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
    }
}
