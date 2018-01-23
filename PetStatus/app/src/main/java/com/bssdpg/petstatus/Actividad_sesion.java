package com.bssdpg.petstatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
//import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;

public class Actividad_sesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_sesion);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton logingoogle = (SignInButton) findViewById(R.id.logingoogle);
        logingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

       /* Button MiButton = (Button) findViewById(R.id.b_cerrar_sesion);
        MiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                signOut();
            }
        });*/
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                     /*   ImageView imageView = (ImageView) findViewById(R.id.i_imagen_usuario);
                        imageView.setImageResource(0);
                        TextView texto = (TextView) findViewById(R.id.t_nombre);
                        texto.setText("Nombre");
                        texto = (TextView) findViewById(R.id.t_correo);
                        texto.setText("Correo");
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.clear().commit();
                        Mensaje("Sesión cerrada");*/

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                GoogleSignInAccount profile = result.getSignInAccount();

                // get profile information
                String name = "";
                String email = "";
                String uriPicture = "";
                String id="";
                if (profile.getDisplayName() != null) {
                    name = profile.getDisplayName();
                }
                if (profile.getEmail() != null) {
                    email = profile.getEmail();
                }
                if (profile.getPhotoUrl() != null) {
                    uriPicture = profile.getPhotoUrl().toString();
                }
                if (profile.getId() != null) {
                    id = profile.getId();
                }

                Mensaje("Sesión abierta");
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE).edit();
                editor.putString("nombre", name);
                editor.putString("id", id);
                editor.putString("email", email);
                editor.commit();
                Intent intento = new Intent(getApplicationContext(), Actividad_menuPrincipal.class);
                startActivity(intento);
                // ---------------------------------------------------------------------
            } else { // Otros result de actividades de inicio de session como facebook o twitter
            }
        }
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
}
