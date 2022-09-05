package ru.neirodev.mehanik.util;


public class GPSUtils {
    // Экваториальный радиус (км)
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * Преобразовано в радианы (рад)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double a = radLat1 - radLat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s *= EARTH_RADIUS;
        return s;
    }
}

