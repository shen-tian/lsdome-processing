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

    protected int getHsbColor(int hue, int saturation, int brightness) {
        float x = hue / 255f;        // h
        float y = saturation / 255f; // s
        float z = brightness / 255f; // b

        float calcR = 0;
        float calcG = 0;
        float calcB = 0;

        if (y == 0) {  // saturation == 0
            calcR = calcG = calcB = z;

        } else {
            float which = (x - (int)x) * 6.0f;
            float f = which - (int)which;
            float p = z * (1.0f - y);
            float q = z * (1.0f - y * f);
            float t = z * (1.0f - (y * (1.0f - f)));

            switch ((int)which) {
                case 0: calcR = z; calcG = t; calcB = p; break;
                case 1: calcR = q; calcG = z; calcB = p; break;
                case 2: calcR = p; calcG = z; calcB = t; break;
                case 3: calcR = p; calcG = q; calcB = z; break;
                case 4: calcR = t; calcG = p; calcB = z; break;
                case 5: calcR = z; calcG = p; calcB = q; break;
            }
        }

        int calcRi = (int)(255 * calcR);
        int calcGi = (int)(255 * calcG);
        int calcBi = (int)(255 * calcB);

        return (255 << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
    }
}
