package com.bssdpg.petstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Actividad_editarMascota extends AppCompatActivity {
    String mascota="";
    String valorSpiner = "Masculino";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Mascota mascotaEnt = new Mascota();
String idDueño = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editar_mascota);

        Intent callingIntent = getIntent();
        mascota = callingIntent.getStringExtra("mascota");

        getSupportActionBar().setTitle("");

        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email", "invalido");
        idDueño=id;
        myRef = database.getReference("Dueño").child(id).child("Mascotas");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();

                if (key.equals(mascota)) {
                    mascotaEnt = dataSnapshot.getValue(Mascota.class);

                    TextView nombre = (TextView) findViewById(R.id.editText_editNombreMadcota);
                    TextView edad = (TextView) findViewById(R.id.editText_edadEditMascota);
                    TextView peso = (TextView) findViewById(R.id.editText_pesoEditMascota);
                    TextView raza = (TextView) findViewById(R.id.editText_razaEditMascota);

                    nombre.setText(mascotaEnt.getNombre());
                    edad.setText(mascotaEnt.getAños() + "");
                    peso.setText(mascotaEnt.getPeso() + "");
                    raza.setText(mascotaEnt.getRaza());
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


        // alambramos el Button
        Button MiButton = (Button) findViewById(R.id.button_guardarEditMascota);

        //Programamos el evento onclick

        MiButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                editar();


            }

        });

        CargarSpinner();


    }

    private void CargarSpinner() {//Dibujando el Spinner
        Spinner s1;
        final String[] tipos = {
                "Macho",
                "Hembra"

        };


        s1 = (Spinner) findViewById(R.id.spinner_sexoEditMascota);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, tipos);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                valorSpiner = tipos[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        s1.setAdapter(adapter);

    }// fin de CargarSpinner
    public void editar(){


        TextView nombre = (TextView) findViewById(R.id.editText_editNombreMadcota);
        TextView edad = (TextView) findViewById(R.id.editText_edadEditMascota);
        TextView peso = (TextView) findViewById(R.id.editText_pesoEditMascota);
        TextView raza = (TextView) findViewById(R.id.editText_razaEditMascota);

        if(nombre.getText().length()>0){
            if(edad.getText().length()>0){
                if(peso.length()>0){
                    if(raza.length()>0) {
                        myRef = database.getReference("Dueño").child(idDueño).child("Mascotas").child(mascota).child("nombre");
                        myRef.setValue(nombre.getText().toString());
                        myRef = database.getReference("Dueño").child(idDueño).child("Mascotas").child(mascota).child("edad");
                        myRef.setValue(Integer.parseInt(edad.getText().toString()));
                        myRef = database.getReference("Dueño").child(idDueño).child("Mascotas").child(mascota).child("peso");
                        myRef.setValue(Float.parseFloat(peso.getText().toString()));
                        myRef = database.getReference("Dueño").child(idDueño).child("Mascotas").child(mascota).child("raza");
                        myRef.setValue(raza.getText().toString());
                        Mensaje("Mascota modificada correctamente");
                        Intent intento = new Intent(getApplicationContext(), Actividad_detallesMascota.class);
                        intento.putExtra("mascota",mascota);
                        startActivity(intento);
                    }else{
                        Mensaje("Debe digitar la raza de la mascota");
                    }
                }
                else{
                    Mensaje("Debe digitar el peso de la mascota");
                }
            }
            else{

                Mensaje("Debe digitar la edad de la mascota");
            }

        }else{

            Mensaje("Debe digitar el nombre de la mascota");
        }

    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Actividad_detalleDueno.class);
        intento.putExtra("mascota",mascota);
        startActivity(intento);
    }
}
