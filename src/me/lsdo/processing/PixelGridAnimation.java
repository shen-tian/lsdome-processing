package me.lsdo.processing;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by shen on 2016/06/28.
 */
public abstract class PixelGridAnimation {
    protected Dome dome;
    protected OPC opc;

    public PixelGridAnimation(Dome dome, OPC opc) {


        this.dome = dome;
        this.opc = opc;

        opc.setDome(dome);
    }

    public void draw(double t) {

        preFrame(t);

        for (DomeCoord c : dome.coords){
            dome.setColor(c, drawPixel(c, t));
        }

        opc.draw();

    }

    protected abstract int drawPixel(DomeCoord c, double t);

        protected void preFrame(double t){

    }
}
