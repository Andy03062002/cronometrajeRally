package com.ist17.proyectodeaula;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * La clase PolygonUtils proporciona métodos para determinar si un punto específico
 * está dentro de un polígono definido por una lista de coordenadas de vértices.
 */
public class PolygonUtils {

    /**
     * Determina si un punto está dentro de un polígono.
     *
     * @param point   El punto que se desea verificar, representado como un objeto LatLng.
     * @param polygon La lista de vértices que define el polígono, representada como una lista de objetos LatLng.
     * @return Verdadero si el punto está dentro del polígono, falso en caso contrario.
     */
    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygon) {
        int intersectCount = 0;
        for (int j = 0; j < polygon.size() - 1; j++) {
            if (rayCastIntersect(point, polygon.get(j), polygon.get(j + 1))) {
                intersectCount++;
            }
        }
        // Un número impar de intersecciones significa que el punto está dentro del polígono
        return (intersectCount % 2) == 1;
    }

    /**
     * Determina si una línea horizontal desde un punto intersecta con una línea definida por dos vértices.
     *
     * @param point El punto desde el cual se lanza la línea horizontal.
     * @param vertA El primer vértice de la línea.
     * @param vertB El segundo vértice de la línea.
     * @return Verdadero si la línea horizontal desde el punto intersecta la línea entre vertA y vertB, falso en caso contrario.
     */
    private static boolean rayCastIntersect(LatLng point, LatLng vertA, LatLng vertB) {
        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = point.latitude;
        double pX = point.longitude;

        // Verifica si ambos vértices están por encima o por debajo del punto, o si ambos están a la izquierda del punto
        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // No hay intersección posible
        }

        // Calcula la pendiente de la línea
        double m = (aY - bY) / (aX - bX);
        // Calcula la intersección con el eje Y de la línea
        double bee = (-aX) * m + aY;

        // Calcula el punto de intersección en el eje X dado el valor Y del punto
        double x = (pY - bee) / m;

        // Devuelve verdadero si la intersección está a la derecha del punto
        return x > pX;
    }
}
