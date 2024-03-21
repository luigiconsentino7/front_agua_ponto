package com.example.projeto_aguaponto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_aguaponto.model.AdicionarMl;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //definição das variáveis
    private Button bt_adicionar_agua;
    private AdicionarMl adicionarMl;
    private TextView txt_resultado_ml_main;

    private int resultado_ml_atual = 0;
    private int adicionar_agua = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Criação do metodo que agrupa a lógica
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //referencia ao activity main

        IniciarComponentes(); //chamando a função que inicia os componentes *foi criado no fim do código

        AdicionarMl adicionarMl = new AdicionarMl(); //Criação do objeto adicionarMl

        bt_adicionar_agua.setOnClickListener(new View.OnClickListener() { //Referenciando o botão que será clicado
            @Override
            public void onClick(View v) { //metodo que executa uma ação ao clicar no botão

                adicionarMl.Somar(resultado_ml_atual, adicionar_agua);
                resultado_ml_atual = adicionarMl.ResultadoMlAtual();
                NumberFormat formatar = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
                formatar.setGroupingUsed(false);
                txt_resultado_ml_main.setText(formatar.format(resultado_ml_atual));
            }
        });

    }
    public void IniciarComponentes(){ //criação do metodo de iniciar componentes
        bt_adicionar_agua = findViewById(R.id.bt_add_ingestao);
        txt_resultado_ml_main = findViewById(R.id.txt_resultado_ml_main);
    }
}
