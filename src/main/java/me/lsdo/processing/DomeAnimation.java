package me.lsdo.processing;

/**
 * Created by shen on 2016/06/28.
 *
 * THis forms the basis for a dome animation.
 */
public abstract class DomeAnimation {

    public static final double FRAMERATE_SMOOTHING_FACTOR = .9;  // [0, 1) -- higher == smoother
    
    protected Dome dome;
    protected OPC opc;

    private boolean initialized = false;
    private double lastT = 0;
    public double frameRate = 0.;  // fps

    public DomeAnimation(Dome dome, OPC opc) {
        this.dome = dome;
        this.opc = opc;

        opc.setDome(dome);
    }

    public void draw(double t) {
	if (!initialized) {
	    init();
	    initialized = true;
	}

	double deltaT = t - lastT;
	lastT = t;
	updateFramerate(deltaT);
	
        preFrame(t, deltaT);
        for (DomeCoord c : dome.coords){
            dome.setColor(c, drawPixel(c, t));
        }
        postFrame(t);
        opc.draw();
    }

    public Dome getDome(){
        return dome;
    }

    public String getOpcHost(){
        return opc.getHost();
    }

    // Main method that need to be implemented.
    protected abstract int drawPixel(DomeCoord c, double t);

    /** Override this for pre-draw stuff.
     *  e.g. loadPixel, or advance animation state.
     * @param t time in seconds since start.
     * @param deltaT time in seconds since last frame.
     */
    protected void preFrame(double t, double deltaT){
    }

    /** Override this for post-draw stuff
     * e.g. save pixels.
     * @param t time in seconds since start
     */
    protected void postFrame(double t){
    }
    
    // Override: optional
    // Perform one-time initialization that for whatever reason can't be performed in the constructor
    protected void init() {}

    private void updateFramerate(double frameT) {
	double avgFrameLen = frameRate <= 0. ? frameT : 1. / frameRate;
        avgFrameLen = FRAMERATE_SMOOTHING_FACTOR * avgFrameLen + (1 - FRAMERATE_SMOOTHING_FACTOR) * frameT;
	frameRate = 1. / avgFrameLen;
    }
}
