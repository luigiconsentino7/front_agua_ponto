package com.example.projeto_aguaponto.model;

public class AdicionarMl {
    private int adicionarMl = 0;
    private int mlAtual = 0;
    private int mlNovo = 0;

    public int Somar(int mlAtual, int adicionarMl){
        mlNovo = adicionarMl + mlAtual;
        return mlNovo;
    }

    public int ResultadoMlAtual(){
        return mlNovo;
    }
}

