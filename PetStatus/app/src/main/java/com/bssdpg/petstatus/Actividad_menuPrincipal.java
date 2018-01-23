package com.bssdpg.petstatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Actividad_menuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ArrayList<Mascota> mascotas = new ArrayList<Mascota>();
    ArrayAdapter<Mascota> adapter;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    String id_mascota="";
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PetStatus");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ListView Mi_listview = (ListView) findViewById(R.id.listView_mascotasregistradas);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview = navigationView.getHeaderView(0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Obtiene los datos del usuario almacenados
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        id = prefs.getString("id", "invalido");
        String nombre = prefs.getString("nombre", "invalido");
        String correo = prefs.getString("email","invalido");

        TextView txtNombre = (TextView)hview.findViewById(R.id.correoprincipal_txtv);
        txtNombre.setText(nombre);
        TextView txtCorreo = (TextView)hview.findViewById(R.id.textView_ND);
        txtNombre.setText(nombre);
        txtCorreo.setText(correo);
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView_mascotasregistradas);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        myRef = database.getReference("Dueño").child(id).child("Mascotas");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Mascota mascota = dataSnapshot.getValue(Mascota.class);
                mascotas.add(mascota);

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

        DandoClickALosItems();
        DandoLongClickALosItems();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //DialogoSiNoCierre(this.getCurrentFocus());
        }
    }

    public void DandoClickALosItems() {
        ListView list = (ListView) findViewById(R.id.listView_mascotasregistradas);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked,
                                    int position, long id) {
                String message = mascotas.get(position).getId();
                String nombre = mascotas.get(position).getNombre();

                Intent intento = new Intent(getApplicationContext(), Actividad_necesidades.class);
                intento.putExtra("a", message);
                intento.putExtra("b", nombre);
                startActivity(intento);
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

    public void DandoLongClickALosItems() {
        ListView list = (ListView) findViewById(R.id.listView_mascotasregistradas);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = mascotas.get(position).getId();
                id_mascota =message;
                return false;
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.registronav) {
            Intent intento = new Intent(getApplicationContext(), Actividad_registrarMascotas.class);
            startActivity(intento);
        } else if (id == R.id.nav_share) {

            TextView Mi_textview = (TextView) findViewById(R.id.textView4);
            DialogoSiNo(Mi_textview);

        }
        else if (id == R.id.mapeoonav) {

            Intent intento = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intento);

        }
        else if (id == R.id.primerosAuxnav) {

            Intent intento = new Intent(getApplicationContext(), Activida_primerosAuxilios.class);
            startActivity(intento);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    public void DialogoSiNo(View view){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("¿Seguro que desea cerrar sesión?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.clear().commit();
                        Mensaje("Sesión cerrada");
                        signOut();


                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Mensaje("Sesión cerrada");
                        Intent intento = new Intent(getApplicationContext(), Actividad_sesion.class);

                        startActivity(intento);

                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {




                Mensaje("Sesión abierta");
                signOut();


                // ---------------------------------------------------------------------
            } else { // Otros result de actividades de inicio de session como facebook o twitter
            }
        }
    }
    private class MyListAdapter extends ArrayAdapter<Mascota>{

        public MyListAdapter (){
            super(Actividad_menuPrincipal.this, R.layout.mascotas_layout, mascotas);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){

                itemView = LayoutInflater.from(Actividad_menuPrincipal.this).inflate(R.layout.mascotas_layout,parent,false);
            }

            final Mascota mascota = mascotas.get(position);


            TextView nombre = (TextView) itemView.findViewById(R.id.txt_nombreMascotaLV);
            ImageView imagen = (ImageView) itemView.findViewById(R.id.imageView2);
            //TextView tipo = (TextView) itemView.findViewById(R.id.txt_tipoLVE);


            if(mascota.getTipo().equals("Gato")){
                imagen.setImageResource(R.drawable.gato);
                nombre.setText("      " + mascotas.get(position).getNombre());
            }
            else{
                imagen.setImageResource(R.drawable.perro);
                nombre.setText("    " + mascotas.get(position).getNombre());
            }
            //edad.setText(mascotas.get(position).getAños()+"");
            //tipo.setText(mascotas.get(position).getTipo());
            return itemView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId()==R.id.listView_mascotasregistradas) {
            menu.setHeaderTitle("Seleccione una opción: ");
            menu.add(0, 1, 0, "Ver detalles");
            menu.add(0, 2, 0, "Eliminar");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int opcionseleccionada = item.getItemId();
        switch (item.getItemId()) {
            case 1:
                Intent intento = new Intent(getApplicationContext(), Actividad_detallesMascota.class);
                intento.putExtra("mascota",id_mascota);
                startActivity(intento);break;
            case 2:
                DialogoSiNo2(findViewById(R.id.listView_mascotasregistradas)); break;

            default:  Mensaje("No clasificado"); break;
        }
        return true;
    }

    public void DialogoSiNo2(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("¿Seguro que desea eliminar la mascota?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id2) {
                        myRef = database.getReference("Dueño").child(id).child("Mascotas").child(id_mascota);
                        myRef.removeValue();
                        Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
                        startActivity(intento);
                        Mensaje("¡Mascota eliminada correctamente!");
                    } });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {} });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: Intent intento = new Intent(getApplicationContext(), Actividad_detalleDueno.class);
                startActivity(intento); break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void DialogoSiNoCierre(View view){
    // Uso:   DialogoSiNo(findViewById(R.id.btnNombreBoton))

        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("¿Está seguro de cerrar la aplicación?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Destrucción de aplicación
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        //Cierre de aplicación ("minimiza")
                        //finish();
                    } });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };


}
