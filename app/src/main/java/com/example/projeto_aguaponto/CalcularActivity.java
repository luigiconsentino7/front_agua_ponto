package com.example.projeto_aguaponto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_aguaponto.model.CalcularIngestaoDiaria;

import java.text.NumberFormat;
import java.util.Locale;

public class CalcularActivity extends AppCompatActivity {
    private EditText edit_peso;
    private EditText edit_idade;
    private Button bt_calcular;
    private TextView txt_resultado_ml;

    private CalcularIngestaoDiaria calcularIngestaoDiaria;
    private double resultadoMl = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular);


        IniciarComponentes();

        CalcularIngestaoDiaria calcularIngestaoDiaria = new CalcularIngestaoDiaria();


        bt_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peso = edit_peso.getText().toString();
                String idade = edit_idade.getText().toString();

                if (peso.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.toast_informe_peso, Toast.LENGTH_SHORT).show();
                } else if (idade.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.toast_informe_idade, Toast.LENGTH_SHORT).show();
                }else{
                    double pesoDigitadoConvertido = Double.parseDouble(edit_peso.getText().toString());
                    int idadeDigitadaConvertida = Integer.parseInt(edit_idade.getText().toString());

                    calcularIngestaoDiaria.CalcularTotalMl(pesoDigitadoConvertido, idadeDigitadaConvertida);
                    resultadoMl = calcularIngestaoDiaria.ResultadoMl();
                    NumberFormat formatar = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
                    formatar.setGroupingUsed(false);
                    txt_resultado_ml.setText(formatar.format(resultadoMl));

                }
            }
        });
    }

    public void IniciarComponentes(){
        edit_peso = findViewById(R.id.edit_peso);
        edit_idade = findViewById(R.id.edit_idade);
        bt_calcular = findViewById(R.id.bt_calcular);
        txt_resultado_ml = findViewById(R.id.txt_resultado_ml);
    }
}
