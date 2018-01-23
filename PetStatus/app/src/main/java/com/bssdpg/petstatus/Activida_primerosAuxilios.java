package com.bssdpg.petstatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activida_primerosAuxilios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activida_primeros_auxilios);
        LlenarListView();
        DandoClickALosItems();
    }

    private void LlenarListView() {
        String[] primerosAuxilios = {
                "Botiquín",
                "Heridas",
                "Quemaduras",
                "Convulsiones",
                "Asfixia",
                "Golpe de calor",
                "Atropello",
                "Mordeduras o Arañazos"
        };

        ArrayAdapter<String> adaptador =new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, primerosAuxilios);
        ListView milistview = (ListView) findViewById(R.id.listView_primerosAuxilios);
        milistview.setAdapter(adaptador);
    }


    public void DandoClickALosItems() {
        ListView list = (ListView) findViewById(R.id.listView_primerosAuxilios);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked,
                                    int position, long id) {
                TextView textView = (TextView) viewClicked;
                String tipo = "n" + position;
                Intent intento = new Intent(getApplicationContext(), Actividad_primerosAuxiliosDetalle.class);
                intento.putExtra("tipo", tipo);
                intento.putExtra("detalle", textView.getText().toString());
                startActivity(intento);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        startActivity(intento);
    }
}
