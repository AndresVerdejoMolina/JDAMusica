package com.example.jdamusica;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;



public class MainActivity extends AppCompatActivity implements CancionesFragment.ReproducirListener, ReproducirFragment.OnFragmentInteractionListener, AgregarCancion.NuevaCancion {
    private DatabaseReference mDatabase;
    public ChildEventListener childEvent;
    private StorageReference mStorage;
    private FirebaseStorage mFirebase = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String urlDescargaFoto= "";
    public static String urlDescargaCancion= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canciones);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mStorage = FirebaseStorage.getInstance().getReference("canciones");
        childEvent = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Cancion cancion = dataSnapshot.getValue(Cancion.class);

                    guardarCancion(cancion);

                    Log.i("cancion", cancion.getNombreCancion() + "-" + cancion.getNombreArtista() + "-" + cancion.getFoto());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        obtenerCancionesBBDD();
        cargarLista();
    }

    private void cargarLista() {
        CancionesFragment fragment = CancionesFragment.newInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("canciones").removeEventListener(childEvent);

        mDatabase.child("canciones").addChildEventListener(childEvent);
        cargar_fragment(fragment, "CancionesFragment");

    }

    private void obtenerCancionesBBDD() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("canciones").addChildEventListener(childEvent);
    }

    @Override
    public void pasarCancion(final Cancion cancion) {
        ReproducirFragment fragment = ReproducirFragment.newInstance(cancion);
        cargar_fragment(fragment, "ReproducirFragment");

    }

    private void cargar_fragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.canciones_container, fragment, tag).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void guardarCancion(Cancion cancion){
        CancionesFragment fragment = (CancionesFragment) getSupportFragmentManager().findFragmentByTag("CancionesFragment");
        if(fragment != null) {
            fragment.addCancion(cancion);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canciones_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionMenuItemView refresh = findViewById(R.id.action_refesh);
        AgregarCancion fragment = AgregarCancion.newInstance();
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refesh:
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                Toast.makeText(this,
                        "Refrescando lista",
                        Toast.LENGTH_SHORT).show();
                refresh.startAnimation(animation);
                cargarLista();
                break;
            case R.id.action_addSong:
                if(!getDialogStatus()){
                    new GuiaAñadirCancionDialog().show(getSupportFragmentManager(), "GuiaAñadirCancionDialog");
                }
                cargar_fragment(fragment, "AgregarCancion");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cargarLista();
    }

    protected boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        boolean status = mSharedPreferences.getBoolean("item", false);
        Log.i("status2", String.valueOf(status));
        return status;
    }

    @Override
    public void agregarNuevaCancion(final String nombreCancion, final String nombreArtista, final Uri uriFoto, Uri uriAudio) {
        MiHilo thread = new MiHilo();
        Log.i("agregarNuevaCancion", String.valueOf(uriFoto) + "-" +  String.valueOf(uriAudio));
        thread.execute(new Cancion(nombreCancion, nombreArtista, uriFoto.toString(), uriAudio.toString()));

        Toast.makeText(this,
                "Se ha subido la cancion",
                Toast.LENGTH_SHORT).show();

    }

    class MiHilo extends AsyncTask<Cancion, Void, Void> {

        @Override
        protected Void doInBackground(final Cancion... cancions) {
            final UUID uuid = UUID.randomUUID();

            Uri uriFoto = Uri.parse(cancions[0].getFoto());

            Log.i("DoinBackgroung", cancions[0].getFoto() + "-" + cancions[0].getUrlCancion());

            Uri uriAudio = Uri.parse(cancions[0].getUrlCancion());
            StorageReference ref = mFirebase.getReference().child("audios/" + cancions[0].getNombreArtista() + "-" + cancions[0].getNombreCancion() + "(" + uuid + ")");


            UploadTask uploadTask;
            uploadTask = ref.putFile(uriAudio);

            final StorageReference finalRef = ref;
            final StorageReference finalRef1 = ref;
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return finalRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        finalRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlDescargaCancion = String.valueOf(uri);
                            }
                        });

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

            ref = mFirebase.getReference().child("imagenes/" + cancions[0].getNombreArtista() + "-" + cancions[0].getNombreCancion() + "(" + uuid + ")");

            uploadTask = ref.putFile(uriFoto);

            final StorageReference finalRef2 = ref;
            final StorageReference finalRef3 = ref;
            Task<Uri> urlTask1 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return finalRef2.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        finalRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlDescargaFoto = String.valueOf(uri);
                            }
                        });

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

            Log.i("URLS", "audio:" + urlDescargaCancion + " foto:" + urlDescargaFoto);

            cancions[0].setUrlCancion(urlDescargaCancion);
            cancions[0].setFoto(urlDescargaFoto);

            mDatabase.child("canciones").push().setValue(cancions[0]);

            return null;
        }

    }
}


