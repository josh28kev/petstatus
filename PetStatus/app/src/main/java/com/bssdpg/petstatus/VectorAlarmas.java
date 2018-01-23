package com.bssdpg.petstatus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class VectorAlarmas {

    //                                      No usado

    /*private static ArrayList<Alarma> alarmas;
    private AlarmManager alarmManager;
    private SharedPreferences sharedPreferences;
    private Context applicationContext;

    public VectorAlarmas(AlarmManager alarmManager, Context applicationContext) {
        this.alarmManager = alarmManager;
        Toast.makeText(applicationContext, "Alarm manager", Toast.LENGTH_SHORT).show();
        this.applicationContext = applicationContext;

        sharedPreferences = applicationContext.getSharedPreferences("Preference",Context.MODE_PRIVATE);
        Toast.makeText(applicationContext, "Shared preferences", Toast.LENGTH_SHORT).show();
        cargarAlarmas();
    }

    public void almacenarAlarma(Alarma a) {
        alarmas.add(a);

        guardarAlarmas();
    }

    public void eliminarAlarma(int n, int tipo, String dueño, String mascota) {
        if(tipo == 1) {//Posición en el vector
            int contador = 0;
            Alarma aux;
            boolean f = false;
            for(int i = 0; i < alarmas.size() && !f; i++) {
                aux = alarmas.get(i);
                if(aux.getDueño() == dueño && aux.getMascota() == mascota) {
                    if(contador == aux.getNumeroElemento()) {
                        Alarma e = alarmas.get(i);
                        PendingIntent in = e.getIntent();
                        alarmManager.cancel(in);

                        alarmas.remove(n);
                        f = true;
                    }
                    contador++;
                }
            }
        }
        else {
            boolean f = false;
            for(int i = 0; i < alarmas.size() && !f ; i++) {
                if(alarmas.get(i).getIdentificador() == n) {
                    Alarma e = alarmas.get(i);
                    PendingIntent in = e.getIntent();
                    alarmManager.cancel(in);

                    alarmas.remove(i);
                    f = true;
                }
            }
        }

        guardarAlarmas();
    }

    private void guardarAlarmas() {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        int c = 0;
        prefsEditor.putInt("size",alarmas.size());
        for(Alarma a: alarmas) {
            String json = gson.toJson(a);
            prefsEditor.putString("MyObject"+Integer.toString(c++), json);
        }
        prefsEditor.commit();
    }

    private void cargarAlarmas() {
        try {
            Gson gson = new Gson();
            int size = sharedPreferences.getInt("size", -1);
            if(size == -1) {
                alarmas = new ArrayList<>();
            }
            else {
                alarmas = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    String json = sharedPreferences.getString("MyObject"+Integer.toString(i++), "");
                    alarmas.add(gson.fromJson(json, Alarma.class));
                }
            }
        } catch (Exception e) {
            Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
}
