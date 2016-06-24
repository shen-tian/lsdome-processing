import java.util.*;
import processing.core.*;
import me.lsdo.processing.*;

public class KaleidoscopeSketch {

    TriCoord basePanel;
    Dome dome;
    PApplet app;
    public KaleidoscopeSketch(PApplet app, int size_px, Dome dome) {
        this.dome = dome;
        this.app = app;
        basePanel = new TriCoord(TriCoord.CoordType.PANEL, 0, 0, -1);
    }

    // start by filling in the base panel.
    public void beforeFrame(double t) {
        for (DomeCoord c : dome.coords) {
            if (c.panel.equals(basePanel)) {
                dome.setColor(c, getBasePixel(c, t));
            }
        }
    }

    // colors for the base panel. What algorithm is here?
    int getBasePixel(DomeCoord c, double t) {
        PVector p = dome.getLocation(c);
        p = LayoutUtil.Vrot(p, t * (.5 + 3*.5*(Math.cos(.1213*t)+1)));
        p = LayoutUtil.Vmult(p, 1/(1 + 5*.5*(Math.cos(.3025*t)+1)));
        p = LayoutUtil.Vadd(p, LayoutUtil.V(2*Math.cos(.2*t), 0));
        return app.color((int)(MathUtil.fmod(p.x + .4081*t, 1.) * 255), (int)(.6 * 255), (int)(.5*(Math.cos(40*p.x)+1) * 255));
    }

    // This is the kaleidoscope effect. Depending on which panel, flip/rotate.
    // and copy the base panel's colors.
    public int drawPixel(DomeCoord c, double t) {
        int pos = MathUtil.mod(c.panel.u - c.panel.v, 3);
        int rot = MathUtil.mod(c.panel.getOrientation() == TriCoord.PanelOrientation.A ? 2*pos : 1-2*pos, 6);
        boolean flip = (MathUtil.mod(rot, 2) == 1);
        TriCoord basePx = c.pixel.rotate(rot);
        if (flip) {
            basePx = basePx.flip(TriCoord.Axis.U);
        }
        return dome.getColor(new DomeCoord(basePanel, basePx));
    }
    
    public void draw(double t)
    {
        beforeFrame(t);
        for (DomeCoord c : dome.coords){
            dome.setColor(c, drawPixel(c, t));
        }
    }
}
