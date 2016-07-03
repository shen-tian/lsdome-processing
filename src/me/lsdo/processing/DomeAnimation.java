package me.lsdo.processing;

import processing.core.PGraphics;

/**
 * Created by shen on 2016/06/28.
 *
 * THis forms the basis for a dome animation.
 *
 * TODO: using PGraphics for color right now. Not sure that's neccesarry.
 */
public abstract class DomeAnimation {
    protected Dome dome;
    protected OPC opc;

    protected PGraphics graphics;

    public DomeAnimation(Dome dome, OPC opc) {


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
