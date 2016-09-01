package me.lsdo.processing;

/**
 * A few color related functions. Separating these out from how Processing
 * does color, some duplication, but cleaner dependency.
 *
 * Created by shen on 2016/09/01.
 */
public class OpcColor {

    // HSB color, given a value in [0,1] as input for each thing.
    public static int getHsbColor(float hue, float saturation, float brightness) {
        float x = hue;
        float y = saturation;
        float z = brightness;

        float calcR = 0;
        float calcG = 0;
        float calcB = 0;

        if (y == 0) {  // saturation == 0
            calcR = calcG = calcB = z;

        } else {
            float which = (x - (int) x) * 6.0f;
            float f = which - (int) which;
            float p = z * (1.0f - y);
            float q = z * (1.0f - y * f);
            float t = z * (1.0f - (y * (1.0f - f)));

            switch ((int) which) {
                case 0:
                    calcR = z;
                    calcG = t;
                    calcB = p;
                    break;
                case 1:
                    calcR = q;
                    calcG = z;
                    calcB = p;
                    break;
                case 2:
                    calcR = p;
                    calcG = z;
                    calcB = t;
                    break;
                case 3:
                    calcR = p;
                    calcG = q;
                    calcB = z;
                    break;
                case 4:
                    calcR = t;
                    calcG = p;
                    calcB = z;
                    break;
                case 5:
                    calcR = z;
                    calcG = p;
                    calcB = q;
                    break;
            }
        }

        byte calcRi = (byte) (255 * calcR);
        byte calcGi = (byte) (255 * calcG);
        byte calcBi = (byte) (255 * calcB);

        return getRgbColor(calcRi, calcBi, calcGi);
    }

    // This uses 0-255 inputs
    public static int getRgbColor(int red, int green, int blue) {
        // Why do these two lines do diff things?
        //return (255 << 24) | (red << 16) | (green << 8) | blue;
        return blue + (green << 8) + (red << 16) + (255 << 24);
    }

    // Does a simple average (no gamma?) on the RGB of the samples.
    public static int blend(int[] samples)
    {
        int r = 0;
        int g = 0;
        int b = 0;

        int B_MASK = 255;
        int G_MASK = 255<<8; //65280
        int R_MASK = 255<<16; //16711680

        for (int i = 0; i < samples.length; i++) {
            r += ((samples[i] & R_MASK)>>16);
            g += ((samples[i] & G_MASK)>>8);
            b += (samples[i] & B_MASK);
        }

        r /= samples.length;
        g /= samples.length;
        b /= samples.length;

        return getRgbColor(r, g, b);
        //return samples[4];
    }

    // TODO: this is messy as hell, and has a constant fade factor.
    // Think this does an exponetial decay kind of thing. WHo knows.
    public static int blurr(int c1, int c2){
        float factor = .9f;

        int B_MASK = 255;
        int G_MASK = 255<<8; //65280
        int R_MASK = 255<<16; //16711680

        int r1 = (c1 & R_MASK)>>16;
        int g1 = (c1 & G_MASK)>>8;
        int b1 = c1 & B_MASK;

        int r2 = (c2 & R_MASK)>>16;
        int g2 = (c2 & G_MASK)>>8;
        int b2 = c2 & B_MASK;

        byte r = (byte)Math.max(factor * r1, r2);
        byte g = (byte)Math.max(factor * g1, g2);
        byte b = (byte)Math.max(factor * b1, b2);

        return getRgbColor(r, g, b);
        //return c2;
    }
}
