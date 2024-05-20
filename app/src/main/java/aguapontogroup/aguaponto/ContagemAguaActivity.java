package aguapontogroup.aguaponto;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;

import aguapontogroup.aguaponto.databinding.ActivityContagemAguaBinding;
import aguapontogroup.aguaponto.databinding.DefinirAguaIngeridaLayoutBinding;
import aguapontogroup.aguaponto.model.BodyPostRotina;
import aguapontogroup.aguaponto.model.RotinaModel;
import aguapontogroup.aguaponto.model.UsuarioModel;
import aguapontogroup.aguaponto.retrofitUtils.RetrofitUtil;
import aguapontogroup.aguaponto.utils.PrefsUser;
import aguapontogroup.aguaponto.utils.HackUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContagemAguaActivity extends AppCompatActivity {

    private ActivityContagemAguaBinding mainBinding;

    private Dialog dialogAddAgua;

    private int metadeAgua = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityContagemAguaBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        metadeAgua = PrefsUser.getPrefsUsers(ContagemAguaActivity.this).getInt("meta", 0);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainBinding.adicionarAgu.setOnClickListener(v -> dialogAddAgua.show());

        configurarDialogAgua();

        atualizarMl();
    }

    private void configurarDialogAgua() {
        AlertDialog.Builder b = new AlertDialog.Builder(ContagemAguaActivity.this);

        DefinirAguaIngeridaLayoutBinding viewDialog = DefinirAguaIngeridaLayoutBinding.inflate(getLayoutInflater());

        viewDialog.confirmar.setOnClickListener(v -> {

            BodyPostRotina rotinaModel = new BodyPostRotina();

            Toast.makeText(this, "Cadastrando...", Toast.LENGTH_SHORT).show();

            int mlIngerida = 0;

            try {
                mlIngerida = Integer.parseInt(viewDialog.mlField.getEditText().getText().toString());
            } catch (Exception ignored) {
            }

            if (mlIngerida == 0) {
                Toast.makeText(this, "Insira um valor válido!", Toast.LENGTH_SHORT).show();
            } else {
                rotinaModel.setMlIngerido(mlIngerida);
                rotinaModel.setUsuarioId(PrefsUser.getPrefsUsers(ContagemAguaActivity.this).getInt("id", 0));

                RetrofitUtil.createServiceApi(
                        RetrofitUtil.createRetrofit()
                ).postRotina(rotinaModel).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            dialogAddAgua.dismiss();
                            viewDialog.mlField.getEditText().setText("");
                            atualizarMl();
                        } else {
                            Toast.makeText(ContagemAguaActivity.this, "Problema no servidor.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(ContagemAguaActivity.this, "Problema de conexão.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


//        rotinaModel.setMlIngerido(); MayBen#2023
        b.setView(viewDialog.getRoot());
        dialogAddAgua = b.create();
    }

    @SuppressLint("NonConstantResourceId")
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.Relatorio) {
                startActivity(new Intent(this, RelatorioActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Config) {
                Intent i = new Intent(this, CadastroActivity.class);
                i.putExtra("update", true);
                startActivity(i);
                return true;
            } else if (item.getItemId() == R.id.deslogar) {
                finish();
                PrefsUser.getEditorUsers(this).putInt("id", 0).apply();
                startActivity(new Intent(this, LoginActivity.class));
            }
            return false;
        });

        popupMenu.show();
    }

    private void atualizarMl() {
        mainBinding.aguaCounter.setText("...");
        RetrofitUtil.createServiceApi(
                RetrofitUtil.createRetrofit()
        ).getUser(PrefsUser.getPrefsUsers(this).getInt("id", 0)).enqueue(new Callback<UsuarioModel>() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                if (response.isSuccessful()) {

                    mainBinding.saudacao.setText("Olá, " + response.body().getNome());
                    mainBinding.metaText.setText("Sua meta é\n" + metadeAgua + "ml");
                    int counter = 0;
                    for (RotinaModel rotinaModel : response.body().getRotinas().get$values()) {
                        if (HackUtil.isToday(rotinaModel.getIngestao())) {
                            counter += rotinaModel.getMlIngerido();
                        }
                    }

                    if (counter >= metadeAgua && !HackUtil.isToday(
                            PrefsUser.getPrefsUsers(ContagemAguaActivity.this).getString("dataMetaBatida", "")
                    )) {
                        AlertDialog.Builder b = new AlertDialog.Builder(ContagemAguaActivity.this);
                        b.setTitle("Parabéns!");
                        b.setMessage("A meta de consumo de água de hoje foi atingida!");
                        b.setPositiveButton("Ok", null);
                        b.create().show();
                        PrefsUser.getEditorUsers(ContagemAguaActivity.this).putString("dataMetaBatida",
                                new SimpleDateFormat("yyyy-MM-dd").format(new Date())).apply();
                    }
                    mainBinding.aguaCounter.setText(counter + "");
                } else {
                    mainBinding.aguaCounter.setText("error");
                }
            }

            @Override
            public void onFailure(Call<UsuarioModel> call, Throwable throwable) {
                mainBinding.aguaCounter.setText("error");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarMl();
    }
}