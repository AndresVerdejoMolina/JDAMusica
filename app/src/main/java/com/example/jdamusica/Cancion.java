package com.example.jdamusica;

import java.util.UUID;

public class Cancion {
    private String mId;
    private String nombreCancion;
    private String nombreArtista;
    private int mImage;

    public Cancion(String nombreCancion, String nombreArtista, int mImage) {
        mId = UUID.randomUUID().toString();
        this.nombreCancion = nombreCancion;
        this.nombreArtista = nombreArtista;
        this.mImage = mImage;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public int getmImage() {
        return mImage;
    }

    public String getId() {
        return mId;
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "ID='" + mId + '\'' +
                ", Nombre de cancion='" + nombreCancion + '\'' +
                ", Nombre del artista='" + nombreArtista + '\'' +
                '}';
    }

}
