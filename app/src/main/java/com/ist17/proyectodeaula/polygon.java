package com.ist17.proyectodeaula;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class polygon {
    private String nombre;
    private List<LatLng> puntos;

    public polygon(String nombre, List<LatLng> puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public List<LatLng> getPuntos() {
        return puntos;
    }

    // Método para comprobar si un punto está dentro del polígono
    public boolean isPointInPolygon(double lat, double lng) {
        int intersectCount = 0;
        for (int j = 0; j < puntos.size() - 1; j++) {
            if (rayCastIntersect(lat, lng, puntos.get(j), puntos.get(j + 1))) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1; // impar significa dentro
    }

    // Método auxiliar para el algoritmo ray-casting
    private boolean rayCastIntersect(double lat, double lng, LatLng vertA, LatLng vertB) {
        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = lat;
        double pX = lng;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false;
        }

        double m = (aY - bY) / (aX - bX);
        double bee = (-aX) * m + aY;
        double x = (pY - bee) / m;

        return x > pX;
    }
}
