package me.lsdo.processing;

// Template for sketches that compute pixel values directly based on their (x,y) position within a
// a scene. This implies you have a sampling function to render a scene pixel-by-pixel (such as
// ray-tracing and fractals). It is not for scenes that must be rendered the whole screen at once
// (i.e., anything using GPU acceleration), nor for 'pixel art'-type scenes where you want to treat
// the pixels as a discrete grid.
//
// Both spatial and temporal anti-aliasing is supported.

import java.util.*;
import processing.core.PVector;

// IR is the type of the intermediate representation of the individual points to be sampled/rendered.
public abstract class XYAnimation extends DomeAnimation {

    static final int DEFAULT_BASE_SUBSAMPLING = 1;
    static final int MAX_SUBSAMPLING = 64;


    // Mapping of display pixels to 1 or more actual samples that will be combined to yield that
    // display pixel's color.
    private HashMap<DomeCoord, ArrayList<PVector>> points_ir;


    // Amount of subsampling for each display pixel.
    int base_subsampling;

    // If true, perform anti-aliasing in the time domain. Combined with subsampling, this will yield
    // motion blur.
    boolean temporal_jitter;

    public XYAnimation(Dome dome, OPC opc) {
        this(dome, opc, DEFAULT_BASE_SUBSAMPLING, false);
    }

    // Assign each display pixel to N random samples based on the required amount of subsampling.
    // Furthermore, each subsample is converted to its intermediate representation to avoid
    // re-computing it every frame.
    public XYAnimation(Dome dome, OPC opc, int base_subsampling, boolean temporal_jitter) {
        super(dome, opc);


        this.base_subsampling = base_subsampling;
        this.temporal_jitter = temporal_jitter;

        points_ir = new HashMap<DomeCoord, ArrayList<PVector>>();
        int total_subsamples = 0;
        for (DomeCoord c : dome.coords) {
            PVector p = dome.getLocation(c);
            ArrayList<PVector> samples = new ArrayList<PVector>();
            points_ir.put(c, samples);

            p = normalizePoint(p);
            int num_subsamples = Math.min((int)Math.ceil(base_subsampling * subsamplingBoost(p)), MAX_SUBSAMPLING);
            boolean jitter = (num_subsamples > 1);
            for (int i = 0; i < num_subsamples; i++) {
                PVector offset = (jitter ?
                        normalizePoint(LayoutUtil.polarToXy(LayoutUtil.V(
                                Math.random() * .5*LayoutUtil.pixelSpacing(dome.getPanelSize()),
                                Math.random() * 2*Math.PI
                        ))) :
                        LayoutUtil.V(0, 0));
                PVector sample = LayoutUtil.Vadd(p, offset);
                samples.add(sample);
            }

            total_subsamples += num_subsamples;
        }

        System.out.println(String.format("%d subsamples for %d pixels (%.1f samples/pixel)",
                total_subsamples, dome.getNumPoints(), (double)total_subsamples / dome.getNumPoints()));

    }


    // Convert an xy coordinate in 'panel length' units such that the perimeter of the display area
    // is the unit circle.
    protected PVector normalizePoint(PVector p) {
        return LayoutUtil.Vmult(p, 1. / dome.getRadius());
    }

    // **OVERRIDE** (optional)
    // We may want to perform more subsampling in certain areas. Return the factor (e.g., 2x, 3x) to
    // increase subsampling by at the given point.
    double subsamplingBoost(PVector p) {
        return 1.;
    }


    protected int drawPixel(DomeCoord c, double t) {
        return sampleAntialiased(points_ir.get(c), t);
    }

    // Perform the anti-aliasing for a single display pixel.
    int sampleAntialiased(ArrayList<PVector> sub, double t) {
        int[] samples = new int[sub.size()];
        for (int i = 0; i < sub.size(); i++) {
            // Probably not right. The 60 used to read app.framerate
            double t_jitter = (temporal_jitter ? (Math.random() - .5) / 60 : 0.);
            samples[i] = samplePoint(sub.get(i), t + t_jitter, t_jitter);
        }
        return OpcColor.blend(samples);
    }

    // Render an individual sample. 't' is clock time, including temporal jitter. 't_jitter' is the
    // amount of jitter added. Return a color.
    protected abstract int samplePoint(PVector ir, double t, double t_jitter);


}
