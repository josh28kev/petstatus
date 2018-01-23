package com.bssdpg.petstatus;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class Actividad_necesidades extends AppCompatActivity {

   FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ArrayList<Necesidad> necesidadesArray = new ArrayList<Necesidad>();
    ArrayAdapter<Necesidad> adapter;
    int porcentajeActual;
    String dueño;
    String mascota;
    String mascotaN;
    Necesidad necesidad = new  Necesidad();
    int opcionAux = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_necesidades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent callingIntent = getIntent();
        mascota = callingIntent.getStringExtra("a");
        mascotaN = callingIntent.getStringExtra("b");

        getSupportActionBar().setTitle("");

        ListView Mi_listview = (ListView) findViewById(R.id.listView_necesidades);

        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        String id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");
        dueño =id;


       adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView_necesidades);
        list.setAdapter(adapter);
        registerForContextMenu(list);
      myRef = database.getReference("Dueño").child(id).child("Mascotas").child(mascota).child("Necesidad");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Necesidad necesidad = dataSnapshot.getValue(Necesidad.class);

                necesidadesArray.add(necesidad);

                adapter.notifyDataSetChanged();


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

        final int l = list.getChildCount();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intento = new Intent(getApplicationContext(), Actividad_registroNecesidad.class);
                intento.putExtra("dueño", dueño);
                intento.putExtra("mascota", mascota);
                intento.putExtra("mascotaN", mascotaN);
                intento.putExtra("cantidadElementos", necesidadesArray.size());
                startActivity(intento);
            }
        });

        DandoLongClickALosItems();

        }


    public void DandoLongClickALosItems() {
        ListView list = (ListView) findViewById(R.id.listView_necesidades);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                necesidad = necesidadesArray.get(position);
                //Toast.makeText(getApplicationContext(), "Fecha programada: "+necesidad.getDetalle(), Toast.LENGTH_SHORT).show();
                opcionAux = position;
                //Toast.makeText(getApplicationContext(), "ID leído: "+necesidad.getIdAlarma(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
public void rellenarBarra(Necesidad necesidad, String nuevoId){

    if(necesidad.getFrecuencia().equals("Mensual")) {//Si la frecuencia es mensual

        Calendar c = Calendar.getInstance();//Obteniendo fecha y hora
        String aux = c.getTime() + "";
        String partes[] = aux.split(" ");
        String det;
        if((this.catalogoMeses(partes[1]) + 1) == 13)
             det = partes[2] + " " + (1);//Sigue enero
        else
             det = partes[2] + " " + (this.catalogoMeses(partes[1]) + 1);//Obteniendo el número de mes siguiente al actual

        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("detalle");
        myRef.setValue(det);

        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("frecuencia");
        myRef.setValue(det);

        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("idAlarma");
        myRef.setValue(nuevoId);

        /*if(c.get(Calendar.MONTH) == 11) {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR)+1);
        }*/
        /*for(int i = 0; i < partes.length; i++) {
            Toast.makeText(getApplicationContext(), partes[i], Toast.LENGTH_SHORT).show();
        }*/
    }
    else if(necesidad.getFrecuencia().equals("Semanal")){

        Calendar c =Calendar.getInstance();
        String aux=c.getTime()+"";

        String partes[]= aux.split(" ");
        int fechaActual = Integer.parseInt(partes[2]);
        int numMes = this.catalogoMeses(partes[1]);
        int diaSiguiente;
        if(numMes ==1 ||numMes ==3 || numMes==5||numMes==7||numMes==8||numMes==10||numMes==12){
            if(fechaActual+7>31){
                diaSiguiente = (fechaActual+7)-31;
                numMes++;
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
        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("detalle");
        myRef.setValue(detalleAux);

        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("idAlarma");
        myRef.setValue(nuevoId);

        /*for(int i = 0; i < partes.length; i++) {
            Toast.makeText(getApplicationContext(), partes[i], Toast.LENGTH_SHORT).show();
        }*/
    }
    else if(necesidad.getFrecuencia().equals("Diaria")){

        Calendar c =Calendar.getInstance();
        String aux=c.getTime()+"";

        String partes[]= aux.split(" ");

        int diaActual =Integer.parseInt(partes[2]);
        int numMes = this.catalogoMeses(partes[1]);
        int diaSiguiente;

        if(numMes ==1 ||numMes ==3 || numMes==5||numMes==7||numMes==8||numMes==10||numMes==12){
            if(diaActual+1>31){
                diaSiguiente = (diaActual+1)-31;
                numMes++;
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
        String [] auxN = necesidad.getDetalle().split(" ");
        String detalle = auxN[0];
        detalle=detalle+" "+diaSiguiente+" "+numMes;
        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("detalle");
        myRef.setValue(detalle);

        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId()).child("idAlarma");
        myRef.setValue(nuevoId);

        /*for(int i = 0; i < partes.length; i++) {
            Toast.makeText(getApplicationContext(), partes[i], Toast.LENGTH_SHORT).show();
        }*/

        c.set(Calendar.DAY_OF_MONTH, diaSiguiente);
        c.set(Calendar.MONTH, numMes);
        if(diaSiguiente == 1 && numMes == 1) {
            c.set(Calendar.YEAR, c.get(Calendar.YEAR)+1);
        }

    }
    }

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

    public void Mensaje(String msg){Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    @Override
public void onBackPressed(){
        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        startActivity(intento);
}

    private class MyListAdapter extends ArrayAdapter<Necesidad>{

        public MyListAdapter (){
            super(Actividad_necesidades.this, R.layout.layout_necesidades, necesidadesArray);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){

                itemView = LayoutInflater.from(Actividad_necesidades.this).inflate(R.layout.layout_necesidades,parent,false);
            }
            final Necesidad necesidad = necesidadesArray.get(position);


            TextView tipo = (TextView) itemView.findViewById(R.id.txt_tipoNecesidadLVE);
            ProgressBar satisfaccion = (ProgressBar) itemView.findViewById(R.id.progressBar);

            tipo.setText(necesidad.getTipo());

            Calendar c =Calendar.getInstance();//Obtiene la fecha actual del calendario
            String aux=c.getTime()+"";
            String partes[]= aux.split(" ");

            if(necesidad.getFrecuencia().equals("Mensual")){//Si la necesidad es mensual
               int fechaActual = Integer.parseInt(partes[2]);
                String [] detalleAux = necesidad.getDetalle().split(" ");
                int fecha = Integer.parseInt(detalleAux[0]);
                int porcentaje = 0;
                int numeroDias=0;
                int numMes = catalogoMeses(partes[1]);

                //Obteniendo el número de mes actual
                if(numMes ==1 ||numMes ==3 || numMes==5||numMes==7||numMes==8||numMes==10||numMes==12){
                numeroDias=31;
                }else if(numMes==2){
                numeroDias=28;
                }else{
                    numeroDias=30;
                }

                //Si la fecha de hoy es menor a la fecha de la necesidad y el mes actual es menor igual al de la necesidad
                if(fechaActual<fecha && Integer.parseInt(detalleAux[1])<=catalogoMeses(partes[1])){
                    float auxMeses=  ((float)((float)fecha-(float)fechaActual)/(float)numeroDias)*100;
                    porcentaje=(int) Math.round(auxMeses);
                }

                //Si la fecha actual es mayor a la fecha futura
                if(fechaActual>fecha) {

                    //Si el mes de la necesidad es menor al actual
                    if(Integer.parseInt(detalleAux[1])<catalogoMeses(partes[1])){
                        porcentaje =(int) Math.round(((float)((float)fecha-(float)fechaActual)/(float)numeroDias)*100);
                    }
                    else{
                        porcentaje =100;
                    }
                }

                else if(fecha ==fechaActual){//Si la fecha actual es igual a la fecha de la necesidad
                    //Si el mes actual es igualal mes de la necesidad
                     if(Integer.parseInt(detalleAux[1])>catalogoMeses(partes[1])){
                         porcentaje = 100;
                     }else{
                         porcentaje =0;
                     }
                 }

                satisfaccion.setMax(100);
                if(porcentaje>75){

                    satisfaccion.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje>25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje<25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                satisfaccion.setProgress(porcentaje);
            }

            else if(necesidad.getFrecuencia().equals("Diaria")) {//Si la necesidad es diaria
                int diaActual =Integer.parseInt(partes[2]);
                int mesActual = catalogoMeses(partes[1]);
                String [] partes2 =partes[3].split(":");
                String auxPartes = partes2[0]+partes2[1];
                int horaActual = Integer.parseInt(auxPartes);
                String [] partesDetalle = necesidad.getDetalle().split(" ");
                int horaNecesidad = Integer.parseInt(partesDetalle[0]);
                int diaNecesidad = Integer.parseInt(partesDetalle[1]);
                int mesNecesidad = Integer.parseInt(partesDetalle[2]);
                int porcentaje = 0;
                //Si la hora de la necesidad es mayor a la hora actual y el día (lunes, martes...) es menor igual al de la necesidad
                if(horaNecesidad>horaActual && diaActual<=diaNecesidad){
                porcentaje= (int)  100-( Math.round((float)((float)((float)horaNecesidad-(float)horaActual)/2400)*100));
                }

                else if(horaNecesidad<horaActual){
                    //Si el dia actuales menor al de la necesidad
                    if(diaActual<diaNecesidad){
                        porcentaje= (int)  100-  (Math.round((float)((float)((float)horaActual-(float)horaNecesidad)/2400)*100));
                    }
                    //Si el mes actuales menor al de la necesidad
                    else if(mesActual<mesNecesidad){
                        porcentaje= (int) 100-  ( Math.round((float)((float)((float)horaActual-(float)horaNecesidad)/2400)*100));
                    }
                    else{
                        porcentaje=0;
                    }
                }
                //SI la hora de la necesidad es igual a la hora actual
               else if (horaNecesidad == horaActual) {

                    if (diaActual == diaNecesidad &&mesActual==mesNecesidad) {
                        porcentaje = 0;
                    } else {
                        porcentaje = 100;
                    }
                }

                satisfaccion.setMax(100);
                if(porcentaje>75){

                    satisfaccion.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje>25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje<25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                satisfaccion.setProgress(porcentaje);
            }
            else if(necesidad.getFrecuencia().equals("Semanal")){//Si la necesidad es semanal
                int porcentaje =0;
                int diaActual = catalogoDias(partes[0]);
                String [] auxDia=  necesidad.getDetalle().split(" ");

                int diaFuturo = Integer.parseInt(auxDia[0]);
                int fechaActual = Integer.parseInt(partes[2]);
                int fechaFutura = Integer.parseInt(auxDia[1]);
                int mesActual = catalogoMeses(partes[1]);
                int mesFuturo= Integer.parseInt(auxDia[2]);

                //Si el día actual es menor al de la necesidad
                if(diaActual<diaFuturo && fechaActual<fechaFutura){
                    porcentaje =  ((diaFuturo-diaActual)/7)*100;
                }
                else if(diaActual>diaFuturo){

                    if(mesActual<mesFuturo){
                        porcentaje = 100-(((diaActual-diaFuturo)/7)*100);
                    }
                    else if(fechaActual<fechaFutura){

                        double aux2 =100-(((float)diaActual-(float)diaFuturo)/7.0)*100;

                        porcentaje = (int)Math.round(aux2);
                    }
                    else{

                        porcentaje =0;
                    }

                }else if(diaActual==diaFuturo){
                    if(fechaActual==fechaFutura) {
                        porcentaje = 0;
                    }else{
                        porcentaje=100;
                    }


                }
                if(porcentaje>75){

                    satisfaccion.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje>25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                }else if(porcentaje<25) {
                    satisfaccion.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                satisfaccion.setProgress(porcentaje);
            }

            satisfaccion.setMax(100);
            return itemView;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId()==R.id.listView_necesidades) {
            menu.setHeaderTitle("Seleccione una opción: ");
            menu.add(0, 1, 0, "Reiniciar necesidad");
            menu.add(0, 2, 0, "Eliminar");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int opcionseleccionada = item.getItemId();
        switch (item.getItemId()) {
            case 1:
                DialogoSiNo(findViewById(R.id.listView_necesidades));
               break;
            case 2:
                DialogoSiNo2(findViewById(R.id.listView_necesidades)); break;

            default:  Mensaje("No clasificado"); break;
        }
        return true;
    }

    public void DialogoSiNo(View view){
        final int nuevoId = ContadorAlarma.getAlarma(this.getApplicationContext());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("¿Seguro que desea reiniciar la necesidad?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id2) {

                        if(crearAlarmaNecesidad(necesidad,nuevoId)){
                            //En este punto no se ha sobreescritvoId));
                            Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
                            intento.putExtra("a", mascota);
                            intento.putExtra("b", mascotaN);
                            startActivity(intento);
                            Mensaje("¡Necesidad reiniciada correctamente!");
                        }
                        else {
                            Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
                            intento.putExtra("a", mascota);
                            intento.putExtra("b", mascotaN);
                            startActivity(intento);
                            Mensaje("Aún no ha ocurrido esta fecha");
                        }
                    }
                });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
o la necesidad, solamente se creó otra alarma
                            eliminarAlarma(necesidad.getIdAlarma(),necesidad);
                            //Ahora sí se va a sobreescribir
                            rellenarBarra(necesidad,Integer.toString(nue
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

    public void DialogoSiNo2(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("¿Seguro que desea eliminar la necesidad?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id2) {
                        myRef = database.getReference("Dueño").child(dueño).child("Mascotas").child(mascota).child("Necesidad").child(necesidad.getId());

                        String idEliminar = necesidad.getIdAlarma();
                        eliminarAlarma(idEliminar,necesidad);

                        myRef.removeValue();
                        Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
                        intento.putExtra("a", mascota);



                        startActivity(intento);
                        Mensaje("¡Necesidad eliminada correctamente!");
                    }
                });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };


    public boolean crearAlarmaNecesidad(Necesidad necesidad, int id) {
        

        if(necesidad.getFrecuencia().equals("Semanal")) {
            Calendar c = Calendar.getInstance();
            if(fechaMenor(c,necesidad.getDetalle(),2)) {
                //Si no ha pasado aún la fecha
                if(c.get(Calendar.DAY_OF_MONTH) > 24 && c.get(Calendar.MONTH)+1 > 11) {
                    //Si el cálculo anterior ocurrió porque la fecha siguiente es año nuevo
                    c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+7-31);
                    c.set(Calendar.MONTH,0);//Enero
                    c.set(Calendar.YEAR,c.get(Calendar.YEAR)+1);
                    c.set(Calendar.HOUR_OF_DAY,12);
                    c.set(Calendar.MINUTE,0);
                    c.set(Calendar.SECOND,0);

                    long ms = c.getTimeInMillis();
                   
                    scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                            "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);

                    return true;
                }
                else {
					return false;
                }
            }
            else {
                //Si es el día o ya pasó la fecha
                int m = c.get(Calendar.MONTH);
                if(m == 0 || m == 2 || m == 4 || m == 6 || m == 7 ||m == 9 || m == 11) {//31 días
                    if(c.get(Calendar.DAY_OF_MONTH) > 24) {
                        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7 - 31);
                        c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                    }
                    else c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+7);
                }
                else {
                    if(m == 1) {//Febrero
                        if (c.get(Calendar.DAY_OF_MONTH) > 22) {
                            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7 - 28);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
                        } else
                            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7);
                    }
                    else {
                        if(c.get(Calendar.DAY_OF_MONTH) > 23) {
                            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7 - 30);
                            c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                        }
                        else c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+7);
                    }
                }
                c.set(Calendar.HOUR_OF_DAY,12);
                c.set(Calendar.MINUTE,0);
                c.set(Calendar.SECOND,0);

                long ms = c.getTimeInMillis();
                

                scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                        "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);

                return true;
            }
        }
        /////////////--------------------------------------------------------------------/////////////
        if(necesidad.getFrecuencia().equals("Mensual")) {
            Calendar c = Calendar.getInstance();
            if(fechaMenor(c,necesidad.getDetalle(),1)) {
                //Si no ha pasado aún la fecha
                if(Integer.parseInt(necesidad.getDetalle().split(" ")[0]) > 11) {
                    //Si el cálculo anterior ocurrió porque la fecha siguiente es año nuevo
                    c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(necesidad.getDetalle().split(" ")[0]));//El día queda igual a hoy
                    c.set(Calendar.MONTH,0);//Enero
                    c.set(Calendar.YEAR,c.get(Calendar.YEAR)+1);
                    c.set(Calendar.HOUR_OF_DAY,12);
                    c.set(Calendar.MINUTE,0);
                    c.set(Calendar.SECOND,0);

                    long ms = c.getTimeInMillis();


                    scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                            "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);

                    return true;
                }
                else {
					return false;
                }
            }
            else {
                //Si es el día o ya pasó la fecha
                int m = c.get(Calendar.MONTH);
                if(m == 0 || m == 2 || m == 4 || m == 6 || m == 7 ||m == 9 || m == 11) {//31 días
                    if(c.get(Calendar.DAY_OF_MONTH) == 31) {
                        if(m != 0) {
                            c.set(Calendar.DAY_OF_MONTH, 1);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH)+2);
                        }
                        else {
                            c.set(Calendar.DAY_OF_MONTH, 3);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH)+2);
                        }
                    }
                    else c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                }
                else {
                    if(m == 1) {//Febrero
                        c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);
                    }
                    else {
                        c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                    }
                }
                c.set(Calendar.HOUR_OF_DAY,12);
                c.set(Calendar.MINUTE,0);
                c.set(Calendar.SECOND,0);

                long ms = c.getTimeInMillis();
                
                scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                        "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);

                return true;
            }
        }
        /////////////--------------------------------------------------------------------/////////////
        if(necesidad.getFrecuencia().equals("Diaria")) {
            Calendar c = Calendar.getInstance();
            if(fechaMenor(c,necesidad.getDetalle(),3)) {
                //Si no ha pasado aún la fecha
                if(c.get(Calendar.DAY_OF_MONTH) > 30 && c.get(Calendar.MONTH)+1 > 11) {
                    //Si el cálculo anterior ocurrió porque la fecha siguiente es año nuevo
                    c.set(Calendar.DAY_OF_MONTH,1);
                    c.set(Calendar.MONTH,0);//Enero
                    c.set(Calendar.YEAR,c.get(Calendar.YEAR)+1);
                    c.set(Calendar.HOUR_OF_DAY,12);
                    c.set(Calendar.MINUTE,0);
                    c.set(Calendar.SECOND,0);

                    long ms = c.getTimeInMillis();
                   
                    scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                            "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);
                    //Toast.makeText(getApplicationContext(), "For año nuevo", Toast.LENGTH_LONG).show();
                    return true;
                }
                else { //Si no ha pasado la fecha (no hay que reprogramar la alarma)
                    /*String a = "Fecha actual: " + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/"
                            + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                            + ":" + c.get(Calendar.SECOND);
                    String b = "1 : " + necesidad.getDetalle().split(" ")[1] + " 2 " +necesidad.getDetalle().split(" ")[2];
                    Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();*/
                    //Toast.makeText(getApplicationContext(), "Fecha no transcurrida", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else {
                //Si es el día o ya pasó la fecha
                int m = c.get(Calendar.MONTH);
                if(m == 0 || m == 2 || m == 4 || m == 6 || m == 7 ||m == 9 || m == 11) {//31 días
                    if(c.get(Calendar.DAY_OF_MONTH) > 30) {
                        c.set(Calendar.DAY_OF_MONTH, 1);
                        c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                    }
                    else c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);
                }
                else {
                    if(m == 1) {//Febrero
                        if (c.get(Calendar.DAY_OF_MONTH) > 27) {
                            c.set(Calendar.DAY_OF_MONTH, 1);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
                        } else
                            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);
                    }
                    else {
                        if(c.get(Calendar.DAY_OF_MONTH) > 29) {
                            c.set(Calendar.DAY_OF_MONTH, 1);
                            c.set(Calendar.MONTH,c.get(Calendar.MONTH)+1);
                        }
                        else c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);
                    }
                }
                c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(necesidad.getDetalle().split(" ")[0].substring(0,2)));
                c.set(Calendar.MINUTE,Integer.parseInt(necesidad.getDetalle().split(" ")[0].substring(2,4)));
                c.set(Calendar.SECOND,0);

                long ms = c.getTimeInMillis();
                //int id = ContadorAlarma.getAlarma(getApplicationContext()); No reactivar
                //Toast.makeText(getApplicationContext(), "IDD2: "+Integer.toString(id), Toast.LENGTH_SHORT).show();

                /*Toast.makeText(getApplicationContext(),
                        Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "/" +
                                Integer.toString(c.get(Calendar.MONTH) + 1) + "/" +
                                Integer.toString(c.get(Calendar.YEAR)) + " " +
                                Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" +
                                Integer.toString(c.get(Calendar.MINUTE)) + ":" +
                                Integer.toString(c.get(Calendar.SECOND))
                        , Toast.LENGTH_LONG).show();*/

                scheduleNotificationNoDelay(getNotification(mascotaN + " necesita su atención",
                        "Es hora de '" + necesidad.getTipo() + "'", id), ms, id);
                //Toast.makeText(getApplicationContext(), "For final", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }

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

    public boolean fechaMenor(Calendar c, String detalle, int tipo) {
        Calendar caux = Calendar.getInstance();
        if(tipo == 1) {
            if(c.get(Calendar.MONTH) == 11) {//Es diciembre
                if(Integer.parseInt(detalle.split(" ")[1]) == 0) {//La alarma se activa en enero
                    //Mensaje("a");
                    return false;//No es menor
                }
            }
            if(c.get(Calendar.MONTH)+1 < Integer.parseInt(detalle.split(" ")[1])) {//Menor al mes buscado
                //Mensaje("b");
                return true; //Es menor
            }
            if(c.get(Calendar.MONTH)+1 == Integer.parseInt(detalle.split(" ")[1])) {//Si es el mismo mes de la alarma
                if(c.get(Calendar.DAY_OF_MONTH) < Integer.parseInt(detalle.split(" ")[0])) {//Si el día es menor
                    //Mensaje("c");
                    return true; //Es menor
                }
            }
            //Mensaje("d");
            return false;//Si llegó hasta aquí no es menor
        }
        if(tipo == 2) {
            if(c.get(Calendar.MONTH) == 11) {//Es diciembre
                if(Integer.parseInt(detalle.split(" ")[2]) == 0) {//La alarma se activa en enero
                    //Mensaje("a");
                    return false;//No es menor
                }
            }
            if(c.get(Calendar.MONTH)+1 < Integer.parseInt(detalle.split(" ")[2])) {//Menor al mes buscado
                //Mensaje("b");
                return true; //Es menor
            }
            if(c.get(Calendar.MONTH)+1 == Integer.parseInt(detalle.split(" ")[2])) {//Si es el mismo mes de la alarma
                if(c.get(Calendar.DAY_OF_MONTH) < Integer.parseInt(detalle.split(" ")[1])) {//Si el día es menor
                    //Mensaje("c");
                    return true; //Es menor
                }
            }
            //Mensaje("d");
            return false;//Si llegó hasta aquí no es menor
        }
        if(tipo == 3) {
            if(c.get(Calendar.MONTH) == 0) {//Es enero
                if(Integer.parseInt(detalle.split(" ")[2]) == 12) {//La alarma se activa en diciembre
                    //Mensaje("a");
                    return false;//No es menor
                }
            }
            if(c.get(Calendar.MONTH)+1 < Integer.parseInt(detalle.split(" ")[2])) {//Menor al mes buscado
                //Mensaje("b");
                return true; //Es menor
            }
            if(c.get(Calendar.MONTH)+1 == Integer.parseInt(detalle.split(" ")[2])) {//Si es el mismo mes de la alarma
                if(c.get(Calendar.DAY_OF_MONTH) < Integer.parseInt(detalle.split(" ")[1])) {//Si el día es menor
                    //Mensaje("c");
                    return true; //Es menor
                }
            }
            //Mensaje("d");
            return false;//Si llegó hasta aquí no es menor
        }
        return false;
    }

    public void eliminarAlarma(String id, Necesidad necesidad) {
        int idInt = Integer.parseInt(id);

        android.support.v7.app.NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this);
        Intent intent = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent0 = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent0);
        mBuilder.setSmallIcon(R.drawable.a);
        mBuilder.setContentTitle(mascotaN+" necesita su atención");
        mBuilder.setContentText("Es hora de '"+necesidad.getTipo()+"'");
        mBuilder.setAutoCancel(true);
        Notification n = mBuilder.build();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, idInt);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, n);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, idInt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}