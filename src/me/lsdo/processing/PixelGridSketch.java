

// Template for sketches where you want to directly assign a color to each pixel in the dome grid.
// The color assigned may depend on its triangular grid position or its xy position-- all that matters
// is that there is no underlying 'screen' being rendered that you want to pluck colors from.

package me.lsdo.processing;

import java.util.*;
import processing.core.*;

public abstract class PixelGridSketch {

    protected Dome dome;
    protected PApplet app;
    protected OPC opc;


    public PixelGridSketch(PApplet app, Dome dome, OPC opc) {
        this.dome = dome;
        this.app = app;
        this.opc = opc;

        opc.setDome(dome);

    }

    public void draw() {


        app.background(0);
        app.noStroke();
        for (DomeCoord c : dome.coords){
            dome.setColor(c, drawPixel(c, app.millis()/1000d));

            PVector p = LayoutUtil.xyToScreen(dome.getLocation(c), app.width, app.height, 2 * dome.getRadius(), true);
            app.fill(dome.getColor(c));
            app.ellipse(p.x, p.y, 3, 3);
        }

        opc.draw();

        app.text("opc @" + opc.getHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);

    }

    protected abstract int drawPixel(DomeCoord c, double t);

}
