package aguapontogroup.aguaponto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.getBoolean("first", true)){
            editor.putBoolean("first", false).apply();
            finish();
            startActivity(new Intent(this, InstrucoesActivity.class));
        }else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


    }
}