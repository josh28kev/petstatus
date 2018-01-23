package com.bssdpg.petstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.auth.api.Auth;

public class Actividad_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
      if(id.equals("invalido")){
          Intent intento = new Intent(getApplicationContext(), Actividad_sesion.class);
          startActivity(intento);
      }

      else {
          Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
          startActivity(intento);
      }
    }

    public void OnclickDelButton(int ref) {
        View view =findViewById(ref);
        Button miButton = (Button) view;
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.b_iniciar_sesion:{
                        Intent intento = new Intent(getApplicationContext(), Actividad_sesion.class);
                        startActivity(intento);
                        break;
                    }
                    default: break;
                }
            }
        });
    }
}
