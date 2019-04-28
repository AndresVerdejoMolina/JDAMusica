package com.example.jdamusica;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositorioCanciones {
    protected static RepositorioCanciones repository = new RepositorioCanciones();
    protected HashMap<String, Cancion> canciones = new HashMap<>();
    private DatabaseReference mDatabase;

    public static RepositorioCanciones getInstance(){ return repository;}

    protected  RepositorioCanciones(){

            guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
    }

    protected void guardarCancion(Cancion cancion){ canciones.put(cancion.getId(), cancion);}

    public List<Cancion> getCanciones() {return new ArrayList<>(canciones.values()); }
}
