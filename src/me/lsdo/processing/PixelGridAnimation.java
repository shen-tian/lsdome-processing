package me.lsdo.processing;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by shen on 2016/06/28.
 */
public abstract class PixelGridAnimation {
    protected Dome dome;
    protected OPC opc;

    protected PGraphics graphics;

    public PixelGridAnimation(Dome dome, OPC opc) {


        this.dome = dome;
        this.opc = opc;

        this.graphics = new PGraphics();
        opc.setDome(dome);
    }

    public void draw(double t) {

        preFrame(t);

        for (DomeCoord c : dome.coords){
            dome.setColor(c, drawPixel(c, t));
        }

        opc.draw();

    }

    public Dome getDome(){
        return dome;
    }

    public String getOpcHost(){
        return opc.getHost();
    }

    protected abstract int drawPixel(DomeCoord c, double t);

    protected void preFrame(double t){

    }
}
