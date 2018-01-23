package com.bssdpg.petstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Actividad_detalleDueno extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Dueño dueño;


    static Dueño auxiliar = new  Dueño();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalle_dueno);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), Actividad_editarDueno.class);
                startActivity(intento);
            }
        });
        getSupportActionBar().setTitle("");
        //Obteniendo los datos de usuario
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        final String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");
        TextView error = (TextView) findViewById(R.id.textView12);
        error.setText("Error");
        dueño = new Dueño(nombre,correo,id,"No especificado",0,"No especificado");

        //Posicionandose en la información del dueño en BD
        myRef = database.getReference("Dueño");

        final List<Dueño> dueños1=new ArrayList<Dueño>();

        //Obteniendo la info del dueño desde la BD
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Dueño d = dataSnapshot.getValue(Dueño.class);
                if(d.getId().equals(id)) {
                    TextView error = (TextView) findViewById(R.id.textView12);
                    error.setText("Detalles");
                    TextView nombre = (TextView) findViewById(R.id.nombreDUeno);
                    TextView sexo = (TextView) findViewById(R.id.textView_sexoDueno);
                    TextView direccion = (TextView) findViewById(R.id.textView_direccionDueno);
                    TextView telefono = (TextView) findViewById(R.id.textView_telefonoDueno);

                    nombre.setText(d.getNombre());
                    sexo.setText(d.getSexo());
                    direccion.setText(d.getDireccion());
                    telefono.setText(d.getTelefono()+"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        startActivity(intento);
    }
}
