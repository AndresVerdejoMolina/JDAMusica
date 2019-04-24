package com.example.jdamusica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositorioCanciones {
    protected static RepositorioCanciones repository = new RepositorioCanciones();
    protected HashMap<String, Cancion> canciones = new HashMap<>();

    public static RepositorioCanciones getInstance(){ return repository;}

    protected  RepositorioCanciones(){
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));
        guardarCancion(new Cancion("Verte ir", "Anuel AA", R.drawable.cancion));
        guardarCancion(new Cancion("Hola", "Zion Lenox", R.drawable.cancion));
        guardarCancion(new Cancion("Hello", "Adelle", R.drawable.cancion));


    }

    protected void guardarCancion(Cancion canicon){ canciones.put(canicon.getId(), canicon);}

    public List<Cancion> getCanciones() {return new ArrayList<>(canciones.values()); }
}
