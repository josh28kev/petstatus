package com.bssdpg.petstatus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Actividad_respuesta_notificacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_respuesta_notificacion);
        /*Intent callingIntent = getIntent();
        String aa = callingIntent.getStringExtra("aa");
        identificador_alarma = callingIntent.getIntExtra("id", 0);
        Toast.makeText(getApplicationContext(), aa, Toast.LENGTH_SHORT).show();*/
        //identificador_alarma = 0;

        //OnclickDelButton(R.id.button1);
        //OnclickDelButton(R.id.button2);
        finish();
        //Este reinicio es necesario para que funcione la primera vez el reinicio de alarma
    }

    /*public void OnclickDelButton(int ref) {

        // Ejemplo  OnclickDelButton(R.id.MiButton);
        // 1 Doy referencia al Button
        View view =findViewById(ref);
        Button miButton = (Button) view;
        //  final String msg = miButton.getText().toString();
        // 2.  Programar el evento onclick
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(msg.equals("Texto")){Mensaje("Texto en el botón ");};
                switch (v.getId()) {

                    case R.id.button1:
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(getApplicationContext(),
                                Actividad_respuesta_notificacion.class/*this.getClass()*//*);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getApplicationContext(), identificador_alarma, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.cancel(pendingIntent);
                        finalizar_activity();
                        break;

                    case R.id.button2:
                        finalizar_activity();
                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelButton

    private void finalizar_activity() {
        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        startActivity(intento);
    }*/

    private int identificador_alarma;
}
