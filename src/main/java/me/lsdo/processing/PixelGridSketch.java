

// Template for sketches where you want to directly assign a color to each pixel in the dome grid.
// The color assigned may depend on its triangular grid position or its xy position-- all that matters
// is that there is no underlying 'screen' being rendered that you want to pluck colors from.

package me.lsdo.processing;

import processing.core.PApplet;

public class PixelGridSketch {

    protected PApplet app;
    protected DomeAnimation animation;

    public PixelGridSketch(PApplet app, DomeAnimation animation) {

        this.app = app;
        this.animation = animation;
    }

    public void draw() {
        double t = app.millis()/1000d;

        animation.draw(t);

        app.background(0);
        app.noStroke();
        for (DomeCoord c : animation.dome.coords){
            PVector2 p = LayoutUtil.xyToScreen(animation.dome.getLocation(c),
                    app.width, app.height, 2 * animation.dome.getRadius(), true);
            app.fill(animation.dome.getColor(c));
            app.ellipse(p.x, p.y, 3, 3);
        }


        app.fill(127);

        app.text("opc @" + animation.getOpcHost(), 100, app.height - 10);
        app.text(String.format("%.1ffps", app.frameRate), 10, app.height - 10);

    }

}
