package me.lsdo.processing;

// Template for sketches that compute pixel values directly based on their (x,y) position within a
// a scene. This implies you have a sampling function to render a scene pixel-by-pixel (such as
// ray-tracing and fractals). This sketch is also used to sample rendered pixels from a processing
// canvas via CanvasSketch. Spatial anti-aliasing is supported. Variable-density sub-sampling is
// supported.

import java.util.*;

public abstract class XYAnimation extends DomeAnimation {

    static final int DEFAULT_BASE_SUBSAMPLING = 1;
    static final int MAX_SUBSAMPLING = 64;

    private int baseSubsampling;
    
    // Mapping of display pixels to 1 or more actual samples that will be combined to yield that
    // display pixel's color. Most simply the samples will be xy-coordinates near the dome pixels,
    // though they may also be transformed into some intermediate vector space (screen pixels, a
    // UV-mapped texture, etc.) for efficiency.
    private HashMap<DomeCoord, ArrayList<PVector2>> points_ir;

    public XYAnimation(Dome dome, OPC opc) {
        this(dome, opc, Config.getSketchProperty("subsampling", DEFAULT_BASE_SUBSAMPLING));
    }

    // Assign each display pixel to N random samples based on the required amount of subsampling.
    // Furthermore, each subsample is converted to its intermediate representation to avoid
    // re-computing it every frame.
    public XYAnimation(Dome dome, OPC opc, int baseSubsampling) {
        super(dome, opc);
	this.baseSubsampling = baseSubsampling;
    }
    
    // Can't do this in the constructor because generating the sub-sample coordinates usually
    // depends on member variables in the sketch, which can only be set after the constructor
    // has finished.
    @Override
    protected void init() {
        points_ir = new HashMap<DomeCoord, ArrayList<PVector2>>();
        int total_subsamples = 0;
        for (DomeCoord c : dome.coords) {
            PVector2 p = dome.getLocation(c);
            ArrayList<PVector2> samples = new ArrayList<PVector2>();
            points_ir.put(c, samples);

            p = normalizePoint(p);
            int num_subsamples = Math.min((int)Math.ceil(baseSubsampling * subsamplingBoost(p)), MAX_SUBSAMPLING);
            boolean jitter = (num_subsamples > 1);
            for (int i = 0; i < num_subsamples; i++) {
                PVector2 offset = (jitter ?
				   normalizePoint(LayoutUtil.polarToXy(LayoutUtil.V(
				    Math.random() * .5*LayoutUtil.pixelSpacing(dome.getPanelSize()),
				    Math.random() * 2*Math.PI
                                  ))) :
                                  LayoutUtil.V(0, 0));
                PVector2 sample = LayoutUtil.Vadd(p, offset);
                samples.add(toIntermediateRepresentation(sample));
            }

            total_subsamples += num_subsamples;
        }

        System.out.println(String.format("%d subsamples for %d pixels (%.1f samples/pixel)",
					 total_subsamples, dome.getNumPoints(), (double)total_subsamples / dome.getNumPoints()));
    }

    // Convert an xy coordinate in 'panel length' units such that the perimeter of the display area
    // is the unit circle.
    protected PVector2 normalizePoint(PVector2 p) {
        return LayoutUtil.Vmult(p, 1. / dome.getRadius());
    }

    @Override
    protected int drawPixel(DomeCoord c, double t) {
        ArrayList<PVector2> sub = points_ir.get(c);

        int[] samples = new int[sub.size()];
        for (int i = 0; i < sub.size(); i++) {
            samples[i] = samplePoint(sub.get(i), t);
        }
        return OpcColor.blend(samples);
    }

    // Render an individual sample. 't' is clock time. Default implementation redirects to the
    // motion blur version, but override this function rather than that one if you don't care about
    // that.
    protected int samplePoint(PVector2 ir, double t) {
	double temporal_jitter = (Math.random() - .5) / frameRate;
	return samplePointWithMotionBlur(ir, t + temporal_jitter, temporal_jitter);
    }

    // Render an individual sample, with motion blur. 't' is clock time plus a random jitter of up to
    // one frame. jitterT is the amount of jitter added. Requires decent subsampling to get a good
    // effect.
    protected int samplePointWithMotionBlur(PVector2 ir, double t, double jitterT) {
	throw new RuntimeException("not implemented");
    }

    // **OVERRIDE** (optional)
    // We may want to perform more subsampling in certain areas. Return the factor (e.g., 2x, 3x) to
    // increase subsampling by at the given point.
    protected double subsamplingBoost(PVector2 p) {
        return 1.;
    }

    // **OVERRIDE** (optional)
    // Convert an xy point to be sampled into an intermediate representation, if it would save work
    // that would otherwise be re-computed each frame.
    protected PVector2 toIntermediateRepresentation(PVector2 p) {
        return p;
    }
    
}
