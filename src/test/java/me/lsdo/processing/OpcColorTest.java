package me.lsdo.processing;

/**
 * Created by shen on 2016/09/17.
 */

import junit.framework.TestCase;

public class OpcColorTest extends TestCase{
    public void testRGB() {
        int color = OpcColor.getRgbColor(255,192,127);

        int redChannel = OpcColor.getRed(color);
        int blueChannel = OpcColor.getBlue(color);
        int greenChannel = OpcColor.getGreen(color);

        assertEquals(redChannel, 255);
        assertEquals(greenChannel, 192);
        assertEquals(blueChannel, 127);
    }

    public void testHSBRed() {
        int color = OpcColor.getHsbColor(0,1,1);
        int rgbEquivalent = OpcColor.getRgbColor(255,0,0);
        assertEquals(color, rgbEquivalent);
    }

    public void testHSBBlue() {
        int color = OpcColor.getHsbColor(.666667,1,1);
        int rgbEquivalent = OpcColor.getRgbColor(0,0,255);
        assertTrue(similar(color, rgbEquivalent));
    }

    private static boolean similar(int color1, int color2){
        int r1 = OpcColor.getRed(color1);
        int r2 = OpcColor.getRed(color2);
        int g1 = OpcColor.getGreen(color1);
        int g2 = OpcColor.getGreen(color2);
        int b1 = OpcColor.getBlue(color1);
        int b2 = OpcColor.getBlue(color2);

        return Math.max(
                Math.max(Math.abs(r1 - r2),
                          Math.abs(g1 - g2)),
                Math.abs(b1 - b2)) < 3;
    }

}
