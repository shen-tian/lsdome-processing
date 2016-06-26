package me.lsdo.processing;

import processing.core.PApplet;
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
            int pixelLocation = (int) Math.floor(screenP.x) + app.width * ((int) Math.floor(screenP.y));
            int pixel = app.pixels[pixelLocation];

            dome.setColor(c, pixel);

            app.pixels[pixelLocation] = 0xFFFFFF ^ pixel;
        }

        app.updatePixels();

        opc.draw();

        app.fill(127);

        app.text("opc @" + opc.getHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);
    }

    public abstract void paint();

}
