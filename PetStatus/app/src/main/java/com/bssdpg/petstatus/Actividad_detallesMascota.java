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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Actividad_detallesMascota extends AppCompatActivity {
    String mascota="";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Mascota mascotaEnt=new Mascota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalles_mascota2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Intent callingIntent = getIntent();
        mascota = callingIntent.getStringExtra("mascota");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), Actividad_editarMascota.class);
                intento.putExtra("mascota", mascota);
                startActivity(intento);

            }
        });


        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");

        myRef = database.getReference("Dueño").child(id).child("Mascotas");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();

                if (key.equals(mascota)) {
                    mascotaEnt = dataSnapshot.getValue(Mascota.class);

                    TextView nombre = (TextView) findViewById(R.id.textView_nombreDetalles);
                    TextView edad = (TextView) findViewById(R.id.textView_edadDetalle);
                    TextView sexo = (TextView) findViewById(R.id.textView_sexoDetallees);
                    TextView peso = (TextView) findViewById(R.id.textView_pesoDetalle);
                    TextView raza = (TextView) findViewById(R.id.textView_razaDetalle);
                    TextView tipo = (TextView) findViewById(R.id.textView_tipoDetalle);

                    nombre.setText(mascotaEnt.getNombre());
                    edad.setText(mascotaEnt.getAños() + "");
                    sexo.setText(mascotaEnt.getSexo());
                    peso.setText(mascotaEnt.getPeso() + "");
                    raza.setText(mascotaEnt.getRaza());
                    tipo.setText(mascotaEnt.getTipo());
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
