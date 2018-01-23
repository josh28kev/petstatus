package com.bssdpg.petstatus;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Actividad_registroNecesidad extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String mascota;
    String dueño;
    String mascotaN;
    String valorSpiner = "Alimentación";
    int posicionElemento = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_registro_necesidad);
        getSupportActionBar().setTitle("");

        //Obteniendo PutExtra
        Intent callingIntent = getIntent();
        dueño = callingIntent.getStringExtra("dueño");
        mascota = callingIntent.getStringExtra("mascota");
        posicionElemento = callingIntent.getIntExtra("cantidadElementos", -1);
        mascotaN = callingIntent.getStringExtra("mascotaN");
        //Toast.makeText(getApplicationContext(), Integer.toString(l), Toast.LENGTH_SHORT).show();

        //Obteniendo datos del usuario almacenados en Shared  Prefences
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");

        //Cargando el spinner
        CargarSpinner();

        //Alambrando  y dibujando la interfaz
        EditText hora = (EditText) findViewById(R.id.editText_horaRegistro);
        hora.setVisibility(View.INVISIBLE);
        EditText min = (EditText) findViewById(R.id.editText_minutiotosNecesidad);
        min.setVisibility(View.INVISIBLE);
        TextView horaTxt = (TextView) findViewById(R.id.txt_horaNecesidad);
        horaTxt.setVisibility(View.INVISIBLE);
        TextView minTxt = (TextView) findViewById(R.id.txt_minutosNeceasidad);
        minTxt.setVisibility(View.INVISIBLE);
        CheckBox check = (CheckBox) findViewById(R.id.checkBox_am);
        check.setVisibility(View.INVISIBLE);

        Button MiButton = (Button) findViewById(R.id.button_registroNecesiddad);
        MiButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                AgregarNecesidad();
            }
        });

        OnclickDelRadioButton(R.id.radioButton_diario);
        OnclickDelRadioButton(R.id.radioButton_mensual);
        OnclickDelRadioButton(R.id.radioButton_semanal);
    }

    private void AgregarNecesidad() {

        String frecuencia;
        String detalle="";
        final int id = ContadorAlarma.getAlarma(getApplicationContext());

        //Referenciando al dueño y la mascota respectiva en la BD
        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").push();
        String id2 = myRef.getKey();//Obteniendo un "primary key"
        Map<String, Necesidad> necesidades= new HashMap <String, Necesidad>();

        //Alambrando radio buttons
        RadioButton RBMensual = (RadioButton) findViewById(R.id.radioButton_mensual);
        RadioButton RBSemanal = (RadioButton) findViewById(R.id.radioButton_semanal);

        if(RBMensual.isChecked()){//Si la frecuencia es mensual

            frecuencia = "Mensual";
            Calendar c =Calendar.getInstance();//Obteniendo fecha y hora
            String aux=c.getTime()+"";
            String partes[]= aux.split(" ");
            String det = partes[2]+" "+(this.catalogoMeses(partes[1])+1);//Obteniendo el número de mes siguiente al actual

            Necesidad  necesidad = new Necesidad(valorSpiner,frecuencia,det, id2, Integer.toString(id));
            necesidades.put(id2, necesidad); //Los datos en firebase se guardan como un hash map

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.DAY_OF_MONTH, c2.get(Calendar.DAY_OF_MONTH));
            if(c2.get(Calendar.MONTH) == 11) {
                c2.set(Calendar.MONTH, 0);
                c2.set(Calendar.YEAR, c2.get(Calendar.YEAR)+1);
            }
            else {
                c2.set(Calendar.MONTH, c2.get(Calendar.MONTH)+1);
                c2.set(Calendar.YEAR, c2.get(Calendar.YEAR));
            }
            c2.set(Calendar.HOUR_OF_DAY,12);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            final long ms = c2.getTimeInMillis();

            //Toast.makeText(getApplicationContext(), detalle, Toast.LENGTH_LONG).show();

            myRef.setValue(necesidad);//Guardando la necesidad en la BD
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Toast.makeText(getApplicationContext(), "IDRM: "+Integer.toString(id), Toast.LENGTH_SHORT).show();
                    scheduleNotificationNoDelay(getNotification(mascotaN+" necesita su atención",
                            "Es hora de '" + valorSpiner + "'", id), ms, id);
                    //MensajeOK("Necesidad guardada corectamente!!");
                    MensajeOKreemplazo("Necesidad guardada corectamente!!");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //MensajeOK("Hubo un error!!");
                    MensajeOKreemplazo("Hubo un error!!");
                }
            });
            return;

        }else if (RBSemanal.isChecked()){//Si la  frecuencia es semanal

            frecuencia = "Semanal";
            Calendar c =Calendar.getInstance();
            String aux=c.getTime()+"";

            String partes[]= aux.split(" ");
            int fechaActual = Integer.parseInt(partes[2]);
            int numMes = this.catalogoMeses(partes[1]);
            int diaSiguiente;
            if(numMes ==1 ||numMes ==3 || numMes==5||numMes==7||numMes==8||numMes==10||numMes==12){
                if(fechaActual+7>31){
                    diaSiguiente = (fechaActual+7)-31;
                    if(numMes != 12)
                        numMes++;
                    else numMes = 1;
                }
                else{
                    diaSiguiente = (fechaActual+7);
                }
            }else if(numMes==2){
                if(fechaActual+7>28){
                    diaSiguiente = (fechaActual+7)-28;
                    numMes++;
                }else{
                    diaSiguiente = (fechaActual+7);
                }

            }
            else{
                if(fechaActual+7>30){
                    diaSiguiente = (fechaActual+7)-30;
                    numMes++;
                }
                else{
                    diaSiguiente = (fechaActual+7);
                }
            }
            String detalleAux = this.catalogoDias(partes[0])+" "+  diaSiguiente+" "+numMes;
            Necesidad  necesidad = new Necesidad(valorSpiner,frecuencia,detalleAux, id2, Integer.toString(id));
            necesidades.put(id2, necesidad);

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.DAY_OF_MONTH, diaSiguiente);
            c2.set(Calendar.MONTH, numMes-1);
            c2.set(Calendar.YEAR, c2.get(Calendar.YEAR));
            c2.set(Calendar.HOUR_OF_DAY,12);
            c2.set(Calendar.MINUTE,0);
            c2.set(Calendar.SECOND, 0);
            final long ms = c2.getTimeInMillis();

            //Toast.makeText(getApplicationContext(), detalle, Toast.LENGTH_LONG).show();

            myRef.setValue(necesidad);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //int id = ContadorAlarma.getAlarma(getApplicationContext()); No reactivar
                    //Toast.makeText(getApplicationContext(), "IDRS: "+Integer.toString(id), Toast.LENGTH_SHORT).show();
                    scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                            "Es hora de '" + valorSpiner + "'", id), ms, id);
                    //MensajeOK("Necesidad guardada corectamente!!");
                    MensajeOKreemplazo("Necesidad guardada corectamente!!");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //MensajeOK("Hubo un error!!");
                    MensajeOKreemplazo("Hubo un error!!");
                }
            });

            return;
        }

        else {//si la frecuencia es diaria
            int horaAux = 0;
            int minAux = 0;
            frecuencia = "Diaria";

            CheckBox check = (CheckBox) findViewById(R.id.checkBox_am);
            EditText hora = (EditText) findViewById(R.id.editText_horaRegistro);

            //Validando datos
            if (hora.getText().length() > 0) {

                try {
                    horaAux = Integer.parseInt(hora.getText().toString());
                    if (check.isChecked()) {
                        if (horaAux > 12) {
                            Mensaje("Formato de hora incorrecto");
                            return;
                        }
                        else {
                            if(horaAux==12&&check.isChecked()){
                                detalle="00";
                                //Mensaje(detalle)
                                ;
                             }else
                            if (horaAux < 10)
                                detalle = "0" + horaAux;
                            else if(horaAux>=10){
                                detalle = horaAux + "";
                            }

                        }
                    } else {
                        if (horaAux >= 13) {
                            Mensaje("Formato de hora incorrecto");
                            return;
                        } else {
                            if (horaAux < 13 && horaAux != 12)
                                detalle = (horaAux + 12) + "";
                            else detalle = 12 + "";
                        }

                    }
                } catch (Exception e) {
                    Mensaje("Digitó un número incorrecto");
                    return;
                }
            } else {
                Mensaje("Debe digitar la hora");
                return;
            }

            EditText min = (EditText) findViewById(R.id.editText_minutiotosNecesidad);

            if (min.getText().length() > 0) {

                try {
                    minAux = Integer.parseInt(min.getText().toString());
                   if(minAux>59||minAux<0){
                       Mensaje("Cantidad de minutos incorrecta");
                       return;
                   }else {
                       Calendar c =Calendar.getInstance();
                       String aux=c.getTime()+"";

                       String partes[]= aux.split(" ");

                       if(minAux<10)
                       { detalle =detalle+"0"+ minAux;}
                        else{
                                detalle =detalle+""+ minAux;}

                       int diaActual =Integer.parseInt(partes[2]);
                       int numMes = this.catalogoMeses(partes[1]);
                       int diaSiguiente;

                       if(numMes ==1 ||numMes ==3 || numMes==5||numMes==7||numMes==8||numMes==10||numMes==12){
                           if(diaActual+1>31){
                               diaSiguiente = (diaActual+1)-31;
                               if(numMes != 12)
                                   numMes++;
                               else numMes = 1;
                           }
                           else{
                               diaSiguiente = (diaActual+1);
                           }
                       }else if(numMes==2){
                           if(diaActual+1>28){
                               diaSiguiente = (diaActual+1)-28;
                               numMes++;
                           }else{
                               diaSiguiente = (diaActual+1);

                           }

                       }
                       else{
                           if(diaActual+1>30){
                               diaSiguiente = (diaActual+1)-30;
                               numMes++;
                           }
                           else{
                               diaSiguiente = (diaActual+1);
                           }
                       }

                       detalle=detalle+" "+diaSiguiente+" "+numMes;
                       Necesidad  necesidad = new Necesidad(valorSpiner,frecuencia,detalle, id2, Integer.toString(id));
                       necesidades.put(id2, necesidad);

                       Calendar c2 = Calendar.getInstance();
                       c2.set(Calendar.DAY_OF_MONTH, diaSiguiente);
                       c2.set(Calendar.MONTH, numMes-1);
                       c2.set(Calendar.YEAR, c2.get(Calendar.YEAR));
                       c2.set(Calendar.HOUR_OF_DAY,horaAux);
                       c2.set(Calendar.MINUTE,minAux);
                       c2.set(Calendar.SECOND, 0);
                       /*Toast.makeText(getApplicationContext(), Integer.toString(diaSiguiente)+"-"
                               +Integer.toString(numMes)+"-"+Integer.toString(c2.get(Calendar.YEAR))+" - "
                               +Integer.toString(horaAux)+":"+Integer.toString(minAux), Toast.LENGTH_LONG).show();*/
                       final long ms = c2.getTimeInMillis();
                       /*Toast.makeText(getApplicationContext(), Long.toString(ms), Toast.LENGTH_LONG).show();
                       Toast.makeText(getApplicationContext(), Long.toString(System.currentTimeMillis()), Toast.LENGTH_LONG).show();
                       Toast.makeText(getApplicationContext(), Long.toString(ms-System.currentTimeMillis()), Toast.LENGTH_LONG).show();*/

                       //Toast.makeText(getApplicationContext(), detalle, Toast.LENGTH_LONG).show();

                       myRef.setValue(necesidad);
                       myRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {

                               //int id = ContadorAlarma.getAlarma(getApplicationContext()); No reactivar
                               Toast.makeText(getApplicationContext(), "IDRD: "+Integer.toString(id), Toast.LENGTH_SHORT).show();
                               scheduleNotificationNoDelay(getNotification(mascotaN+" necesita su atención",
                                       "Es hora de '" + valorSpiner + "'", id), ms, id);
                               //MensajeOK("Necesidad guardada corectamente!!");
                               MensajeOKreemplazo("Necesidad guardada correctamente!!");
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {
                               //MensajeOK("Hubo un error!!");
                               MensajeOKreemplazo("Hubo un error!!");
                           }
                       });

                   }

                } catch (Exception e) {
                    //MensajeOK("Digitó un número incorrecto");
                    MensajeOKreemplazo("Hubo un error!!");
                    return;
                }
            } else {
                Mensaje("Debe digitar los minutos");
                return;
            }
        }

    }

    public void OnclickDelRadioButton(int ref) {


        View view =findViewById(ref);
        RadioButton miRadioButton = (RadioButton) view;

                miRadioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.radioButton_diario:
                        EditText hora = (EditText) findViewById(R.id.editText_horaRegistro);
                        hora.setVisibility(View.VISIBLE);
                        EditText min = (EditText) findViewById(R.id.editText_minutiotosNecesidad);
                        min.setVisibility(View.VISIBLE);

                        TextView horaTxt = (TextView) findViewById(R.id.txt_horaNecesidad);
                        horaTxt.setVisibility(View.VISIBLE);
                        TextView minTxt = (TextView) findViewById(R.id.txt_minutosNeceasidad);
                        minTxt.setVisibility(View.VISIBLE);

                        CheckBox check = (CheckBox) findViewById(R.id.checkBox_am);
                        check.setVisibility(View.VISIBLE);

                        break;

                    case R.id.radioButton_semanal:
                        EditText horaS = (EditText) findViewById(R.id.editText_horaRegistro);
                        horaS.setVisibility(View.INVISIBLE);
                        EditText minS = (EditText) findViewById(R.id.editText_minutiotosNecesidad);
                        minS.setVisibility(View.INVISIBLE);

                        TextView horaTxtS = (TextView) findViewById(R.id.txt_horaNecesidad);
                        horaTxtS.setVisibility(View.INVISIBLE);
                        TextView minTxtS = (TextView) findViewById(R.id.txt_minutosNeceasidad);
                        minTxtS.setVisibility(View.INVISIBLE);

                        CheckBox checkS = (CheckBox) findViewById(R.id.checkBox_am);
                        checkS.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.radioButton_mensual:
                        EditText horaM = (EditText) findViewById(R.id.editText_horaRegistro);
                        horaM.setVisibility(View.INVISIBLE);
                        EditText minM = (EditText) findViewById(R.id.editText_minutiotosNecesidad);
                        minM.setVisibility(View.INVISIBLE);

                        TextView horaTxtM = (TextView) findViewById(R.id.txt_horaNecesidad);
                        horaTxtM.setVisibility(View.INVISIBLE);
                        TextView minTxtM = (TextView) findViewById(R.id.txt_minutosNeceasidad);
                        minTxtM.setVisibility(View.INVISIBLE);

                        CheckBox checkM = (CheckBox) findViewById(R.id.checkBox_am);
                        checkM.setVisibility(View.INVISIBLE);

                        break;
                    default:
                        break;
                }// fin de casos
            }// fin del onclick
        });
    }// fin de OnclickDelRadioButton

    public void MensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder( v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
                        intento.putExtra("a",mascota);
                        intento.putExtra("b",mascotaN);
                        startActivity(intento);
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        ;};

    public void MensajeOKreemplazo(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
        intento.putExtra("a",mascota);
        intento.putExtra("b", mascotaN);
        startActivity(intento);
    }

    private void CargarSpinner() {//Dibujando el Spinner
        Spinner s1;
        final String[] tipos = {
                "Alimentación",
                "Agua",
                "Baño",
                "Paseo",
                "Visita al veterinario",
                "Entretenimiento",
                "Desparasitación intestinal",
                "Desparasitación del pelaje",
                "Vacunas",
                "Caja de arena"
        };


        s1 = (Spinner) findViewById(R.id.spinner_tipoNecesidad);
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

    public int catalogoMeses(String mes){

        switch(mes){

            case "Jan":
                return 1;

            case "Feb":
                return 2;

            case "Mar":
                return 3;

            case "Apr":
                return 4;

            case "May":
                return 5;

            case "Jun":
                return 6;

            case "Jul":
                return 7;

            case "Aug":
                return 8;

            case "Sep":
                return 9;

            case "Oct":
                return 10;

            case "Nov":
                return 11;

            case "Dec":
                return 12;

            default:
                return 0;

        }
    }

    public int catalogoDias (String d){

        switch(d){

            case "Mon":
                return 1;
            case "Tue":
                return 2;
            case "Wed":
            return 3;
            case "Thu":
                return 4;
            case "Fri":
                return 5;
            case "Sat":
                return 6;
            case "Sun":
                return 7;
            default:
                return 0;
        }
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    /*private void scheduleNotificationDelay(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, 1000 * 60 * 10, pendingIntent);
    }*/

    private void scheduleNotificationNoDelay(Notification notification, long time, int id) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        //notificationIntent.putExtra("aa","duwang".toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = time;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //ALARMA NORMAL
        //alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

        //ALARMA QUE SE REPITE
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, 1000 * 60 * 10, pendingIntent);

        //IGNORAR ESTA PARTE
        /*VectorAlarmas v = new VectorAlarmas(alarmManager,this.getApplicationContext());
        //Toast.makeText(getApplicationContext(), "Vector creado", Toast.LENGTH_SHORT).show();
        v.almacenarAlarma(new Alarma(dueño,mascota,id,pendingIntent,posicionElemento));*/

    }

    private Notification getNotification(String title, String text, int id) {
        android.support.v7.app.NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this);

        //Actividad a invocar
        Intent intent = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("aa","duwang");
        //Toast.makeText(getApplicationContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.a);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setAutoCancel(true);
        return mBuilder.build();
    }
}

