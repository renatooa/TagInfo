package taginfo.renato.com.br.taginfoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import taginfo.renato.com.br.taginfoservice.modelo.InformacaoProduto;

public class InfoProdutoActivity extends AppCompatActivity {

    private InformacaoProduto informacaoProduto;
    private TextToSpeech tts;
    private BarChart chart;
    private TextView textNome;
    private TextView textValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_produto);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        recuperarParametros();

        criarViews();

        popularViews();
    }

    private void recuperarParametros() {

        Intent intentOrigem = getIntent();
        Bundle bundle = intentOrigem.getExtras();

        if (bundle != null && !bundle.isEmpty()) {
            informacaoProduto = (InformacaoProduto) bundle.getSerializable(InformacaoProduto.class.getName());
        }
    }


    private void criarViews() {
        chart = (BarChart) findViewById(R.id.chartLine);
        textNome = (TextView) findViewById(R.id.produtoNome);
        textValor = (TextView) findViewById(R.id.produtoValor);
    }

    private void popularViews() {

        textNome.setText(informacaoProduto.getNome());
        textValor.setText(Double.toString(informacaoProduto.getValorVenda()));

        configurarGrafico();
    }

    private void configurarGrafico() {

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        chart.setData(criarBarData());

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        chart.invalidate();
        chart.notifyDataSetChanged();
        chart.getData().notifyDataChanged();
    }

    private BarData criarBarData() {

        List<BarEntry> entriesVenda = Arrays.asList(new BarEntry(1f, (float) informacaoProduto.getVendas(), getString(R.string.texto_venda)));

        List<BarEntry> entriesEstoque = Arrays.asList(new BarEntry(2f, (float) informacaoProduto.getEstoque(), getString(R.string.texto_estoque)));

        List<BarEntry> entriesCompra = Arrays.asList(new BarEntry(3f, (float) informacaoProduto.getQuantidadeAReceber(), getString(R.string.texto_compra)));

        BarDataSet setEstoque, setVenda, setCompra;

        setVenda = new BarDataSet(entriesVenda, getString(R.string.texto_venda));
        setVenda.setColor(Color.rgb(71, 172, 255));

        setEstoque = new BarDataSet(entriesEstoque, getString(R.string.texto_estoque));
        setEstoque.setColor(Color.rgb(53, 194, 232));

        setCompra = new BarDataSet(entriesCompra, getString(R.string.texto_compra));
        setCompra.setColor(Color.rgb(59, 255, 250));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(setVenda);
        dataSets.add(setEstoque);
        dataSets.add(setCompra);

        BarData barData = new BarData(dataSets);
        barData.setValueTextColor(Color.BLACK);
        barData.setDrawValues(true);

        return barData;
    }

    public void narrarValorProduto(View view) {

        String texto = getString(R.string.texto_valor_produto, informacaoProduto.getNome(), informacaoProduto.getValorVenda());

        tts.speak(texto, TextToSpeech.QUEUE_ADD, null);
    }
}
