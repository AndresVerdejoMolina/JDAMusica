package com.example.jdamusica;

import java.util.UUID;

public class Cancion {
    private String nombreCancion;
    private String nombreArtista;
    private String foto;
    private String urlCancion;

    public Cancion(){}

    public Cancion(String nombreCancion, String nombreArtista, String foto, String urlCancion) {
        this.nombreCancion = nombreCancion;
        this.nombreArtista = nombreArtista;
        this.foto = foto;
        this.urlCancion=urlCancion;
    }

    public void setNombreCancion(String nombreCancion) {
        this.nombreCancion = nombreCancion;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUrlCancion() {
        return urlCancion;
    }

    public void setUrlCancion(String urlCancion) {
        this.urlCancion = urlCancion;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

}
