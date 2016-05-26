// The abstract geometry of the dome

package me.lsdo.processing;

import java.util.*;
import processing.core.*;

public class Dome {

    // width and height of processing canvas, in pixels
    int width, height;

    // Positions of all the pixels in triangular grid coordinates (and in the order seen by
    // the fadecandy).
    public ArrayList<DomeCoord> coords;

    // Mapping of pixel grid coordinates to xy locations (world coordinates, not screen
    // coordinates!)
    public  HashMap<DomeCoord, PVector> points;

    public HashMap<DomeCoord, Integer> colors;

    // Size of a single panel's pixel grid
    protected int panel_size;

    // Layout configuration of panels
    protected PanelLayout panel_config_mode;

    // Distance from center to farthest pixel, in panel lengths
    double radius;

    public Dome(int size_px) {
        this(size_px, size_px);
    }

    public Dome(int width_px, int height_px) {
        this.width = width_px;
        this.height = height_px;
    }

    public Integer getColor(DomeCoord dCoord){
      return colors.get(dCoord);
    }

    public void setColor(DomeCoord dCoord, Integer color){
      colors.put(dCoord, color);
    }

    // Determine optimum screen width for panel display, such that pixels are packed in relatively
    // close together without actually overlapping. This should decrease aliasing (if the sketch
    // renders more smoothly at lower resolution), but will also increase quantization error (how
    // far a pixel's snapped-to-screen position is off from its true dome position) and blurriness
    // in a way that is undesirable for some sketches. Increase the spacing with
    // 'spacingMultiplier' (default: 1).
    // This function is completely useless for sketches that already do per-pixel rendering
    // directly (e.g., PointSampleSketch, PixelGridSketch)
    static int widthForPixelDensity(double spacingMultiplier) {
        double radius = getRadius();
        double pixelWidth = 2. * radius / LayoutUtil.pixelSpacing(Config.PANEL_SIZE);
        double baseSpacing = 2.;
        int dim = (int)Math.round(baseSpacing * pixelWidth * spacingMultiplier);
        System.out.println("dome pixel width: " + pixelWidth);
        System.out.println("screen width: " + dim);
        return dim;
    }

    // Get the radius of the panel display, in panel lengths
    static double getRadius() {
        return LayoutUtil.getPanelConfig(Config.PARTIAL_LAYOUT ?
                                         Config.FULL_PANEL_LAYOUT :
                                         Config.PANEL_LAYOUT).radius;
    }

    // Override this if you have more specific initialization to perform. Be sure to call
    // super.init()!
    public void init() {

        panel_size = Config.PANEL_SIZE;
        panel_config_mode = Config.PANEL_LAYOUT;
        LayoutUtil.PanelConfig config = LayoutUtil.getPanelConfig(panel_config_mode);
        coords = config.fill(panel_size);
        points = config.coordsToXy(coords);
        colors = new HashMap<DomeCoord, Integer>();

        radius = getRadius();
    }

    ArrayList<PVector> pixelLocationsInOrder() {
        ArrayList<PVector> xy = new ArrayList<PVector>();
        for (DomeCoord c : coords) {
            xy.add(points.get(c));
        }
        return xy;
    }

    // Convert a screen pixel position to world coordinates.
    public PVector screenToXy(PVector p) {
        return LayoutUtil.screenToXy(p, width, height, 2*radius, true);
    }

    // Inverse of screenToXy()
    public PVector xyToScreen(PVector p) {
        return LayoutUtil.xyToScreen(p, width, height, 2*radius, true);
    }

}
