package com.ist17.proyectodeaula;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Poligono {
    private String nombre;
    private List<LatLng> puntos;

    public Poligono(String nombre, List<LatLng> puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public List<LatLng> getPuntos() {
        return puntos;
    }

    @NonNull
    @Override
    public String toString() {
        return "Nombre"+getNombre()
                ;
    }
}