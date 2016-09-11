package me.lsdo.processing;

// Template for sketches that compute pixel values directly based on their (x,y) position within a
// a scene. This implies you have a sampling function to render a scene pixel-by-pixel (such as
// ray-tracing and fractals). It is not for scenes that must be rendered the whole screen at once
// (i.e., anything using GPU acceleration), nor for 'pixel art'-type scenes where you want to treat
// the pixels as a discrete grid.
//
// Both spatial and temporal anti-aliasing is supported.

import java.util.*;
//import processing.core.PVector;

// IR is the type of the intermediate representation of the individual points to be sampled/rendered.
public abstract class XYAnimation extends DomeAnimation {

    static final int DEFAULT_BASE_SUBSAMPLING = 1;
    static final int MAX_SUBSAMPLING = 64;


    // Mapping of display pixels to 1 or more actual samples that will be combined to yield that
    // display pixel's color.
    private HashMap<DomeCoord, ArrayList<PVector2>> points_ir;

    public XYAnimation(Dome dome, OPC opc) {
        this(dome, opc, DEFAULT_BASE_SUBSAMPLING);
    }

    // Assign each display pixel to N random samples based on the required amount of subsampling.
    // Furthermore, each subsample is converted to its intermediate representation to avoid
    // re-computing it every frame.
    public XYAnimation(Dome dome, OPC opc, int baseSubsampling) {
        super(dome, opc);

        points_ir = new HashMap<DomeCoord, ArrayList<PVector2>>();
        int total_subsamples = baseSubsampling * dome.getNumPoints();
        int num_subsamples = Math.min(baseSubsampling, MAX_SUBSAMPLING);

        for (DomeCoord c : dome.coords) {
            PVector2 p = dome.getLocation(c);
            ArrayList<PVector2> samples = new ArrayList<PVector2>();
            points_ir.put(c, samples);

            p = normalizePoint(p);

            boolean jitter = (num_subsamples > 1);
            for (int i = 0; i < num_subsamples; i++) {
                PVector2 offset = (jitter ?
                        normalizePoint(LayoutUtil.polarToXy(LayoutUtil.V(
                                Math.random() * .5 * LayoutUtil.pixelSpacing(dome.getPanelSize()),
                                Math.random() * 2 * Math.PI
                        ))) :
                        LayoutUtil.V(0, 0));
                PVector2 sample = LayoutUtil.Vadd(p, offset);
                samples.add(sample);
            }
        }

        System.out.println(String.format("%d subsamples for %d pixels (%d samples/pixel)",
                total_subsamples, dome.getNumPoints(), baseSubsampling));

    }


    // Convert an xy coordinate in 'panel length' units such that the perimeter of the display area
    // is the unit circle.
    protected PVector2 normalizePoint(PVector2 p) {
        return LayoutUtil.Vmult(p, 1. / dome.getRadius());
    }

    protected int drawPixel(DomeCoord c, double t) {
        ArrayList<PVector2> sub = points_ir.get(c);

        int[] samples = new int[sub.size()];
        for (int i = 0; i < sub.size(); i++) {
            samples[i] = samplePoint(sub.get(i), t);
        }
        return OpcColor.blend(samples);
    }

    // Render an individual sample. 't' is clock time, including temporal jitter. 't_jitter' is the
    // amount of jitter added. Return a color.
    protected abstract int samplePoint(PVector2 ir, double t);


}
