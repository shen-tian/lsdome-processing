package me.lsdo.processing;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by shen on 2016/06/26.
 */
public class CanvasSketch extends XYAnimation {

    protected PApplet app;

    public CanvasSketch(PApplet app, Dome dome, OPC opc){
        super(dome, opc);
        this.app = app;
    }

    protected void preFrame(double t){
        app.loadPixels();
    }

    protected  void postFrame(double t){

        // draw pixel locations
        for (DomeCoord c : dome.coords){
            PVector screenP = LayoutUtil.xyToScreen(dome.getLocation(c), app.width, app.height, 2 * dome.getRadius(), true);
            int pixelLocation = (int) Math.floor(screenP.x) + app.width * ((int) Math.floor(screenP.y));

            app.pixels[pixelLocation] = 0xFFFFFF ^ app.pixels[pixelLocation];
        }

        app.updatePixels();

        // on screen text
        app.fill(127f, 256f);
        app.text("opc @" + opc.getHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);

    }

    protected int samplePoint(PVector ir, double t, double t_jitter)
    {
        PVector screenP = LayoutUtil.xyToScreen(ir, app.width, app.height, 2 * dome.getRadius(), true);

        // some super simple super-sample anti aliasing here.
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

        // Motion blur?
        //int output = OpcColor.blurr(dome.getColor(ir), OpcColor.blend(samples));

        return OpcColor.blend(samples);

    }

    public void draw()
    {
        draw(0); // because we don't use time here.
    }

    public void writeLayoutJson(){

        ArrayList<PVector> xy = new ArrayList<PVector>();
        for (DomeCoord c : dome.coords) {
            xy.add(dome.getLocation(c));
        }

        LayoutUtil.generateOPCSimLayout(xy, app, "layout.json");
    }

}
