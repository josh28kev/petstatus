package com.bssdpg.petstatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actividad_registrarMascotas extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Dueño dueño;
    static Dueño auxiliar = new  Dueño();
    String valorSpiner = "Hembra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_actividad_registrar_mascotas);

        //Obteniendo los datos de usuario
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");
        dueño = new Dueño(nombre,correo,id,"No especificado",0,"No especificado");

        //Posicionandose en la información del dueño en BD
        myRef = database.getReference("Dueño");

        final List<Dueño> dueños1=new ArrayList<Dueño>();

        //Obteniendo la info del dueño desde la BD
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Dueño d = dataSnapshot.getValue(Dueño.class);
                    if(d!=null) {
                        dueños1.add(d);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

CargarSpinner();
        Button MiButton = (Button) findViewById(R.id.guardar_button);

        MiButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

              AgregarMascota(dueños1.size());
            }
        });
    }

        private void AgregarMascota(int resultado) {

            String nombre;
            String tipo;
            int edad=0;
            String raza;
            float peso;


            if(resultado==0){ //Si el dueño (usuario) no está registrado en la BD
                myRef = database.getReference("Dueño");
                Map<String, Dueño> dueños= new HashMap <String, Dueño>();
                dueños.put(dueño.getId(), dueño);
                myRef.setValue(dueños);//Guarda el dueño en la BD
            }

            //Validando y obteniendo los datos de la interfaz
           EditText nombre_editText = (EditText) findViewById(R.id.nombreMascota_editText);
            if(nombre_editText.getText().length()>0){
                nombre= nombre_editText.getText().toString();
                EditText edad_editText = (EditText) findViewById(R.id.edad_editText);
                if (edad_editText.getText().length()>0){
                    try{
                        edad  = Integer.parseInt(edad_editText.getText().toString());
                    }
                    catch(Exception e){
                        Mensaje("Número de años inválido");
                    }
                    RadioButton perroRadioB = (RadioButton ) findViewById(R.id.Perro_radioButton);
                    if(perroRadioB.isChecked()){
                        tipo="Perro";
                    }else{
                        tipo="Gato";
                    }

                    EditText peso_editText = (EditText) findViewById(R.id.editText_peso);
                    if(peso_editText.getText().length()>0) {
                        try{
                            peso=Float.parseFloat(peso_editText.getText().toString());
                        }
                        catch (Exception e){
                            Mensaje("Digitó un peso inválido");
                            return;
                        }
                        EditText raza_editText = (EditText) findViewById(R.id.editText_raza);
                        if(raza_editText.getText().length()>0 ) {
                            raza = raza_editText.getText().toString();

                            myRef = database.getReference("Dueño").child(dueño.getId()).child("Mascotas").push();
                            String id = myRef.getKey(); //Obteniendo un "primary key"
                            Map<String, Mascota> mascotas = new HashMap<String, Mascota>();

                            Mascota mascota = new Mascota(nombre, tipo, edad, id,valorSpiner ,peso, raza);
                            mascotas.put(id, mascota);//Guardando la mascota en la interfaz

                            myRef.setValue(mascota);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    MensajeOK("Mascota guardada corectamente!!");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    MensajeOK("Hubo un error!!");
                                }
                            });
                        }else{

                            Mensaje("Debe digitar la raza de la mascota");
                        }
                    }else{
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




    public void LeeObjetoEnFirebase(String nombreobj) {


        myRef = database.getReference("Dueño");
        final List<Dueño> dueños=new ArrayList<Dueño>();
        myRef.child(nombreobj).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Dueño carro = dataSnapshot.getValue(Dueño.class);
                auxiliar=carro;
                setAuxiliar(carro);
                dueños.add(carro);
                MensajeOK(carro.getNombre());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void MensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
                        startActivity(intento);
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;};
    private void CargarSpinner() {//Dibujando el Spinner
        Spinner s1;
        final String[] tipos = {
                "Hembra",
                "Macho"
        };


        s1 = (Spinner) findViewById(R.id.spinner_sexo);
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
    @Override
    public void onBackPressed() {
        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        startActivity(intento);
    }

    void setAuxiliar(Dueño d) {
        this.auxiliar = d;
    }//Esto existe por un problema de firebase

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
