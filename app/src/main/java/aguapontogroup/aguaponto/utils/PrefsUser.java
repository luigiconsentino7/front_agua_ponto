package aguapontogroup.aguaponto.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUser {

    public static SharedPreferences.Editor getEditorUsers(Context context){
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit();
    }

    public static SharedPreferences getPrefsUsers(Context context){
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }
}
