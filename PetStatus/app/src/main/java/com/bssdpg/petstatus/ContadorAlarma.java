package com.bssdpg.petstatus;

import android.content.Context;
import android.content.SharedPreferences;

public class ContadorAlarma {

    public static int getAlarma(Context applicationContext) {

        SharedPreferences prefs = applicationContext.getSharedPreferences("mispref01", applicationContext.MODE_PRIVATE);
        contador = prefs.getInt("contador", 0); //0 valor default.
        contador++;

        SharedPreferences.Editor editor = applicationContext.getSharedPreferences("mispref01", applicationContext.MODE_PRIVATE).edit();
        editor.putInt("contador", contador);
        editor.commit();

        return contador;
    }

    public static int contador = 0;
}
