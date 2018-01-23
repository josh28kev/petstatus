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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Actividad_editarDueno extends AppCompatActivity {
String valorSpiner = "Masculino";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Dueño dueño =new Dueño();
    String idDueño ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editar_dueno);
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        final String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");
        dueño = new Dueño(nombre,correo,id,"No especificado",0,"No especificado");
idDueño =id;
        getSupportActionBar().setTitle("");
        //Posicionandose en la información del dueño en BD
        myRef = database.getReference("Dueño");
        CargarSpinner();
        final List<Dueño> dueños1=new ArrayList<Dueño>();

        //Obteniendo la info del dueño desde la BD
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Dueño d = dataSnapshot.getValue(Dueño.class);
                if (d.getId().equals(id)) {

                    TextView nombre = (TextView) findViewById(R.id.editText_nombreEditDueno);
                    Spinner sexo = (Spinner) findViewById(R.id.spinner_sexo);
                    TextView direccion = (TextView) findViewById(R.id.editText_editDireccDueno);
                    TextView telefono = (TextView) findViewById(R.id.editText_editTelDueno);

                    nombre.setText(d.getNombre());


                    direccion.setText(d.getDireccion());
                    telefono.setText(d.getTelefono() + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });


        // alambramos el Button
        Button MiButton = (Button) findViewById(R.id.button_guardarEditDueno);

        //Programamos el evento onclick

        MiButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

              editar();


            }

        });
        }



        private void CargarSpinner() {//Dibujando el Spinner
        Spinner s1;
        final String[] tipos = {
                "Masculino",
                "Femenino"

        };


        s1 = (Spinner) findViewById(R.id.spinner_sexoEdDueno);
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


        TextView nombre = (TextView) findViewById(R.id.editText_nombreEditDueno);
        TextView direccion = (TextView) findViewById(R.id.editText_editDireccDueno);
        TextView telefono = (TextView) findViewById(R.id.editText_editTelDueno);

        if(nombre.getText().length()>0){
            if(direccion.getText().length()>0){
                if(telefono.length()>0){
                    myRef = database.getReference("Dueño").child(idDueño).child("nombre");
                    myRef.setValue(nombre.getText().toString());
                  DatabaseReference  myRef2 = database.getReference("Dueño").child(idDueño).child("direccion");
                    myRef2.setValue(direccion.getText().toString());
                    DatabaseReference  myRef3 = database.getReference("Dueño").child(idDueño).child("sexo");
                    myRef3.setValue(valorSpiner);
                    DatabaseReference myRef4 = database.getReference("Dueño").child(idDueño).child("telefono");
                    myRef4.setValue(Integer.parseInt(telefono.getText().toString()));
                    Mensaje("Dueño modificado correctamente");
                    Intent intento = new Intent(getApplicationContext(), Actividad_detalleDueno.class);
                    startActivity(intento);
                }
            }
            else{

                Mensaje("Debe digitar la dirección del dueño");
            }

        }else{

            Mensaje("Debe digitar el nombre del dueño");
        }


    }

    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Actividad_detalleDueno.class);
        startActivity(intento);
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
