package me.lsdo.processing;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by shen on 2016/06/26.
 */
public abstract class CanvasSketch {

    protected Dome dome;
    protected PApplet app;
    protected OPC opc;

    public CanvasSketch(PApplet app, Dome dome, OPC opc) {
        this.dome = dome;
        this.app = app;
        this.opc = opc;

        opc.setDome(dome);
    }

    public void draw() {
        paint();

        app.loadPixels();

        for (DomeCoord c : dome.coords){
            PVector screenP = LayoutUtil.xyToScreen(dome.getLocation(c), app.width, app.height, 2 * dome.getRadius(), true);

            // some super simple super sample anti aliasing here.
            int gridSize = 3;
            int density = 2;
            int[] samples = new int[gridSize * gridSize];

            for (int i = 0; i < samples.length; i++){
                int mid = (gridSize - 1)/2;
                int ox = density * (i % gridSize - mid);
                int oy = density * (i / gridSize - mid);
                int sampleLocation = (int) Math.floor(screenP.x + ox) + app.width * ((int) Math.floor(screenP.y + oy));
                samples[i] = app.pixels[sampleLocation];
            }


            int output = blurr(dome.getColor(c), blendSamples(samples));

            dome.setColor(c, output);
        }

        for (DomeCoord c : dome.coords){
            PVector screenP = LayoutUtil.xyToScreen(dome.getLocation(c), app.width, app.height, 2 * dome.getRadius(), true);
            int pixelLocation = (int) Math.floor(screenP.x) + app.width * ((int) Math.floor(screenP.y));

            app.pixels[pixelLocation] = 0xFFFFFF ^ app.pixels[pixelLocation];
        }

        app.updatePixels();

        opc.draw();
        app.fill(127f, 256f);
        app.text("opc @" + opc.getHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);


    }

    private int blurr(int c1, int c2){
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

        byte r = (byte)Math.max((int)(factor * r1), r2);
        byte g = (byte)Math.max((int)(factor * g1), g2);
        byte b = (byte)Math.max((int)(factor * b1), b2);

        return getColor(r, g, b, (byte)255);

    }

    private int getColor(byte r, byte g, byte b, byte alpha){
        return b + (g << 8) + (r << 16) + (alpha << 24);
    }

    private int blendSamples(int[] samples) {
        int blended = samples[0];
        for (int i = 1; i < samples.length; i++) {
            blended = app.lerpColor(blended, samples[i], 1f / (1f + i));
        }
        return blended;
    }

    public abstract void paint();

}
