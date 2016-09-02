package me.lsdo.processing;

/**
 * Created by shen on 2016/06/28.
 *
 * THis forms the basis for a dome animation.
 */
public abstract class DomeAnimation {
    protected Dome dome;
    protected OPC opc;


    public DomeAnimation(Dome dome, OPC opc) {
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
