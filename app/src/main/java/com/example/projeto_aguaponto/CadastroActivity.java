package com.example.projeto_aguaponto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }
    TextView campoNome, campoSobreNome, campoData, campoPeso, campoAltura, campoAcorda, campoDorme;
    String nome, sobrenome;
    Date dataNascimento;
    Float peso, altura;
    Time hracorda, hrdorme;

    public void Cadastrar(View view)
    {
        campoNome = findViewById(R.id.campoNome);
        campoSobreNome = findViewById(R.id.campoSobreNome);
        campoData = findViewById(R.id.campoData);
        campoPeso = findViewById(R.id.campoPeso);
        campoAltura = findViewById(R.id.campoAltura);
        campoAcorda = findViewById(R.id.campoAcorda);
        campoDorme = findViewById(R.id.campoDorme);

        nome = campoNome.getText().toString();
        sobrenome = campoSobreNome.getText().toString();
        String pesoString = campoPeso.getText().toString();
        String alturaString = campoAltura.getText().toString();
        peso = Float.parseFloat(pesoString);
        altura = Float.parseFloat(alturaString);
        String dataString = campoData.getText().toString(); // Supondo que campoData seja um EditText
        // Converta a string para um objeto Date, se necessário
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            dataNascimento = sdf.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}