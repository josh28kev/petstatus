package com.bssdpg.petstatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Actividad_primerosAuxiliosDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_primeros_auxilios_detalle);
        Intent callingIntent = getIntent();
        String tipo = callingIntent.getStringExtra("tipo");
        String detalle = callingIntent.getStringExtra("detalle");
        cargarArchivo(tipo,detalle);
        //Mensaje(DeInputStreamAString(miarchivo));
    }

    private String DeInputStreamAString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {sb.append(line+"\n");}
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {br.close();}
                catch (IOException e)
                {e.printStackTrace();}
            }
        }
        return sb.toString();
    }

    private  void cargarArchivo(String archivo, String detalle){
        TextView Mi_edittext = (TextView) findViewById(R.id.txt_textoPA);
        TextView Mi_edittext2 = (TextView) findViewById(R.id.txt_detallePrimerosAux);
        switch (archivo){

            case "n0":
                InputStream miarchivo = getResources().openRawResource(R.raw.n0);
                Mi_edittext.setText(DeInputStreamAString(miarchivo));
                Mi_edittext2.setText(detalle);
                break;
            case "n1":
                InputStream miarchivo2 = getResources().openRawResource(R.raw.n1);
                Mi_edittext.setText(DeInputStreamAString(miarchivo2));
                Mi_edittext2.setText(detalle);
                break;
            case "n2":
                InputStream miarchivo3 = getResources().openRawResource(R.raw.n2);
                Mi_edittext.setText(DeInputStreamAString(miarchivo3));
                Mi_edittext2.setText(detalle);
                break;
            case "n3":
                InputStream miarchivo4 = getResources().openRawResource(R.raw.n3);
                Mi_edittext.setText(DeInputStreamAString(miarchivo4));
                Mi_edittext2.setText(detalle);
                break;
            case "n4":
                InputStream miarchivo5 = getResources().openRawResource(R.raw.n4);
                Mi_edittext.setText(DeInputStreamAString(miarchivo5));
                Mi_edittext2.setText(detalle);
                break;
            case "n5":
                InputStream miarchivo6 = getResources().openRawResource(R.raw.n5);
                Mi_edittext.setText(DeInputStreamAString(miarchivo6));
                Mi_edittext2.setText(detalle);
                break;
            case "n6":
                InputStream miarchivo7 = getResources().openRawResource(R.raw.n6);
                Mi_edittext.setText(DeInputStreamAString(miarchivo7));
                Mi_edittext2.setText(detalle);
                break;
            case "n7":
                InputStream miarchivo8 = getResources().openRawResource(R.raw.n7);
                Mi_edittext.setText(DeInputStreamAString(miarchivo8));
                Mi_edittext2.setText(detalle);
                break;
            default:
                Mi_edittext2.setText("Error");
                Toast.makeText(getApplicationContext(), "Hubo un error al resolver la solicitud", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Activida_primerosAuxilios.class);
        startActivity(intento);
    }

}
