package com.example.jdamusica;
import android.app.ProgressDialog;
import android.content.Intent;
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

        mStorage = FirebaseStorage.getInstance().getReference();
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
        fragment.addCancion(cancion);
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
                new GuiaAñadirCancionDialog().show(getSupportFragmentManager(), "GuiaAñadirCancionDialog");
                cargar_fragment(fragment, "AgregarCancion");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cargarLista();
    }


    @Override
    public void agregarNuevaCancion(final String nombreCancion, final String nombreArtista, final Uri uriFoto, Uri uriAudio) {
        MiHilo thread = new MiHilo();
        thread.execute(new Cancion(nombreCancion, nombreArtista, uriFoto.toString(), uriAudio.toString()));
    }

        class MiHilo extends AsyncTask<Cancion, Void, Void>{

            @Override
            protected Void doInBackground(Cancion... cancions) {
                final UUID uuid = UUID.randomUUID();
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                MainActivity.this.getContentResolver().getType(Uri.parse(cancions[0].getFoto())).startsWith("images/");

                    progressDialog.setTitle("Subiendo audio...");
                    progressDialog.show();

                    final StorageReference ref = mFirebase.getReference().child("audios/" + nombreArtista + "-" + nombreCancion + "(" + uuid + ")");

                    UploadTask uploadTask;
                    uploadTask = ref.putFile(uri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this,
                                        "Se ha subido el audio en el FireBaseStorage, con el nombre: " + nombreArtista + "-" + nombreCancion + "(" + uuid + ")",
                                        Toast.LENGTH_LONG).show();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                    Log.i("NuevaCancion", nombreArtista + "-" + nombreCancion + "cancion: " + urlDescargaCancion + "foto: " + urlDescargaFoto);
                    if(!urlDescargaCancion.isEmpty() && !urlDescargaFoto.isEmpty()) {
                        database.getReference().push().setValue(new Cancion(nombreCancion, nombreArtista, urlDescargaFoto, urlDescargaCancion));
                        Toast.makeText(MainActivity.this,
                                "Nueva canción, " + nombreCancion + " de " + nombreArtista,
                                Toast.LENGTH_LONG).show();


                        urlDescargaFoto = "";
                        urlDescargaCancion="";
                    }


                progressDialog.setTitle("Subiendo foto...");
                progressDialog.show();

                final StorageReference ref = mFirebase.getReference().child("imagenes/" + nombreArtista + "-" + nombreCancion + "(" + uuid + ")");

                UploadTask uploadTask;
                uploadTask = ref.putFile(uriFoto);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,
                                    "Se ha subido la foto en el FireBaseStorage, con el nombre: " + nombreArtista + "-" + nombreCancion + "(" + uuid + ")",
                                    Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

            }

            }


    }
}
