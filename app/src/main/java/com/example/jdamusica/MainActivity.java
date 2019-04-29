package com.example.jdamusica;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CancionesFragment.ReproducirListener, ReproducirFragment.OnFragmentInteractionListener {
    private DatabaseReference mDatabase;
    public ChildEventListener childEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canciones);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
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
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        int id = item.getItemId();
        if (id == R.id.action_refesh) {
            Toast.makeText(this,
                    "Refrescando lista",
                    Toast.LENGTH_LONG).show();
            refresh.startAnimation(animation);
            cargarLista();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cargarLista();
    }
}
