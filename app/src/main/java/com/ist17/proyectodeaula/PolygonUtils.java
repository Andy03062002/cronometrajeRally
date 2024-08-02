package com.ist17.proyectodeaula;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PolygonUtils {
    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygon) {
        int intersectCount = 0;
        for (int j = 0; j < polygon.size() - 1; j++) {
            if (rayCastIntersect(point, polygon.get(j), polygon.get(j + 1))) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1; // odd = inside, even = outside
    }

    private static boolean rayCastIntersect(LatLng point, LatLng vertA, LatLng vertB) {
        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = point.latitude;
        double pX = point.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or b must be to the right of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Slope of the line
        double bee = (-aX) * m + aY; // Y-intercept of the line

        double x = (pY - bee) / m; // The x-intercept of the line given y

        return x > pX;
    }
}