package me.lsdo.processing;

import processing.core.PApplet;

import processing.data.FloatList;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

/**
 * Created by shen on 2016/06/26.
 */
public class CanvasSketch extends XYAnimation {

    protected PApplet app;
    private static int DEFAULT_AA = 8;

    public CanvasSketch(PApplet app, Dome dome, OPC opc){
        this(app, dome, opc, DEFAULT_AA);
    }

    public CanvasSketch(PApplet app, Dome dome, OPC opc, int antiAliasingSamples){
        super(dome, opc, antiAliasingSamples);
        this.app = app;
    }

    protected void preFrame(double t){
        app.loadPixels();
    }

    protected  void postFrame(double t){

        // draw pixel locations
        for (DomeCoord c : dome.coords){
            PVector2 screenP = LayoutUtil.xyToScreen(dome.getLocation(c), app.width, app.height, 2 * dome.getRadius(), true);
            int pixelLocation = (int) Math.floor(screenP.x) + app.width * ((int) Math.floor(screenP.y));

            app.pixels[pixelLocation] = 0xFFFFFF ^ app.pixels[pixelLocation];
        }

        app.updatePixels();

        // on screen text
        app.fill(127f, 256f);
        app.text("opc @" + opc.getHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);

    }

    protected int samplePoint(PVector2 ir, double t)
    {
        PVector2 screenP = LayoutUtil.xyToScreen(ir, app.width, app.height, 2 * dome.getRadius(), true);
        int sampleLocation = (int)(Math.floor(screenP.x)) + app.width * ((int) Math.floor(screenP.y));
        return app.pixels[sampleLocation];
    }

    public void draw()
    {
        draw(0); // because we don't use time here.
    }

    public void writeLayoutJson(){

        ArrayList<PVector2> xy = new ArrayList<PVector2>();
        for (DomeCoord c : dome.coords) {
            xy.add(dome.getLocation(c));
        }

        generateOPCSimLayout(xy, app, "layout.json");
    }

    // Generates the JSON config file for OPC simulator
    public static void generateOPCSimLayout(ArrayList<PVector2> points, PApplet app, String fileName)
    {
        JSONArray values = new JSONArray();

        for (int i = 0; i < points.size(); i++) {

            JSONObject point = new JSONObject();

            float[] coordinates = new float[3];
            coordinates[0] = 2 * points.get(i).x;
            coordinates[1] = 2 * points.get(i).y;
            coordinates[2] = 0;

            point.setJSONArray("point", new JSONArray(new FloatList(coordinates)));

            values.setJSONObject(i, point);
        }

        app.saveJSONArray(values, fileName);
    }

}
