package aguapontogroup.aguaponto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import aguapontogroup.aguaponto.databinding.ActivityInstrucoesBinding;

public class InstrucoesActivity extends AppCompatActivity {

    private ActivityInstrucoesBinding mainBinding;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityInstrucoesBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mainBinding.iniciar.setOnClickListener(v -> {
            mainBinding.layoutInicial.setVisibility(View.GONE);
        });

        String[] titulos = {"Registre o seu consumo de água", "Não esqueça de beber água!"};

        String[] subTitulos = {"Registre seu consumo diário de água e acompanhe com nosso relatório.", "Sabemos que você esquece de tomar água. Estamos aqui para te lembrar!"};

        int[] fotos = {R.drawable.img2, R.drawable.img3};

        mainBinding.continuar.setOnClickListener(v -> {
            try {
                mainBinding.img.setImageResource(fotos[index]);
                mainBinding.titulo.setText(titulos[index]);
                mainBinding.subtitulo.setText(subTitulos[index]);

                index++;

            } catch (ArrayIndexOutOfBoundsException e) {
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }

        });

    }
}