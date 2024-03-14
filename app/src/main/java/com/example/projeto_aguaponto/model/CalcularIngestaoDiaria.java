package com.example.projeto_aguaponto.model;

public class CalcularIngestaoDiaria {

    private int ml_jovem = 40;
    private int ml_adulto = 35;
    private int ml_idoso = 30;
    private int ml_mais_de_66anos = 25;
    private double resultado_ml = 0;
    private double resultado_total_ml = 0;

    public double CalcularTotalMl(double peso, int idade){
        if(idade <= 17){
            resultado_ml = peso * ml_jovem;
            resultado_total_ml = resultado_ml;
        }else if(idade < 18 && idade <= 55){
            resultado_ml = peso * ml_adulto;
        } else if (idade < 56 && idade <= 65) {
            resultado_ml = peso * ml_idoso;
            resultado_total_ml = resultado_ml;
        }else {
            resultado_ml = peso * ml_mais_de_66anos;
            resultado_total_ml = resultado_ml;
        }

        return resultado_total_ml;
    }

    public double ResultadoMl(){
        return resultado_total_ml;
    }
}
