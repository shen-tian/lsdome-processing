package me.lsdo.processing;

public class MathUtil {

    public static double LN2 = Math.log(2.);

    // Fix java's stupid AF mod operator to always return a positive result
    public static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }

    public static double fmod(double a, double b) {
        double mod = a % b;
        if (mod < 0) {
            mod += b;
        }
        return mod;
    }

    public static double log2(double x) {
        return Math.log(x) / LN2;
    }

}
