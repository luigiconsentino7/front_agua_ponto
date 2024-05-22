package aguapontogroup.aguaponto;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aguapontogroup.aguaponto.databinding.ActivityRelatorioBinding;
import aguapontogroup.aguaponto.adapter.AdapterAguaDiaria;
import aguapontogroup.aguaponto.model.BodyGetRotinas;
import aguapontogroup.aguaponto.model.ConsumoAgua;
import aguapontogroup.aguaponto.model.RotinaModel;
import aguapontogroup.aguaponto.retrofitUtils.RetrofitUtil;
import aguapontogroup.aguaponto.utils.HackUtil;
import aguapontogroup.aguaponto.utils.PrefsUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatorioActivity extends AppCompatActivity {

    private ActivityRelatorioBinding mainBinding;
    private List<RotinaModel> rotinasDiaria = new ArrayList<>();
    private List<RotinaModel> todasRotinasDiaria = new ArrayList<>();
    private AdapterAguaDiaria adapterAguaDiaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRelatorioBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainBinding.voltar.setOnClickListener(v -> finish());

        configurarRecyclerView();

    }

    private void configurarRecyclerView() {
        mainBinding.recy.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.recy.setHasFixedSize(true);
        adapterAguaDiaria = new AdapterAguaDiaria(rotinasDiaria, this);
        mainBinding.recy.setAdapter(adapterAguaDiaria);

        RetrofitUtil.createServiceApi(RetrofitUtil.createRetrofit()).getRotinas(PrefsUser.getPrefsUsers(this).getInt("id", 0)).enqueue(new Callback<BodyGetRotinas>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
            @Override
            public void onResponse(Call<BodyGetRotinas> call, Response<BodyGetRotinas> response) {
                if (response.isSuccessful()) {

                    todasRotinasDiaria.addAll(response.body().get$values());
                    configurarGrafico();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false);

                    float mediaAgua = 0.0f;
                    int divisor = 1;
                    Date ultimoRotina = null;
                    for (RotinaModel rotina : todasRotinasDiaria) {


                        try {
                            Date dataRotinaAtual = dateFormat.parse(rotina.getIngestao());

                            if (ultimoRotina == null) {
                                ultimoRotina = dataRotinaAtual;
                            }

                            if (!ultimoRotina.equals(dataRotinaAtual)) {
                                divisor++;
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        mediaAgua += rotina.getMlIngerido();

                        if (HackUtil.isToday(rotina.getIngestao())) {
                            rotinasDiaria.add(rotina);
                        }
                    }


                    mainBinding.textMedia.setText(Html.fromHtml("Sua média semanal de consumo foi de <b>" + String.format("%.2fL", (mediaAgua / divisor) / 1000) + "</b>"));


                    adapterAguaDiaria.notifyDataSetChanged();
                } else {
                    Toast.makeText(RelatorioActivity.this, "Problema no servidor...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BodyGetRotinas> call, Throwable throwable) {
                Toast.makeText(RelatorioActivity.this, "Problema de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configurarGrafico() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        List<Map<String, Object>> ingestoes = new ArrayList<>();

        // Converter todasRotinasDiaria para uma lista de ingestões
        todasRotinasDiaria.forEach(rotinaModel -> {
            ingestoes.add(Map.of(
                    "ingestao", rotinaModel.getIngestao(),
                    "mlIngerido", rotinaModel.getMlIngerido()
            ));
        });

        // Calcular o consumo de água com base nas ingestões
        List<ConsumoAgua> consumoAgua = ConsumoAgua.calcularConsumoAgua(ingestoes);

        // Preencher as entradas para o gráfico
        consumoAgua.forEach(c -> {
            entries.add(new BarEntry(c.getDia(), (float) c.getQuantidadeDeAguaDoDia() / 1000));
        });

        // Configurar o conjunto de dados
        BarDataSet barDataSet = new BarDataSet(entries, "Quantidade de Água Ingerida em Litros");
        barDataSet.setColor(ContextCompat.getColor(this, R.color.ciano));

        // Criar os dados do gráfico
        BarData barData = new BarData(barDataSet);

        // Customizar o eixo X
        XAxis xAxis = mainBinding.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Configurar os dados no gráfico
        mainBinding.chart.setData(barData);
        mainBinding.chart.setFitBars(true);

        // Atualizar o gráfico
        mainBinding.chart.invalidate();
    }
}