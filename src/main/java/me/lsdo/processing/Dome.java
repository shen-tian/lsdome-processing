/**
 * The abstract structure of the dome. No aware of any sketches.
 *
 * Config is pulled in from elsewhere, and the layout is mostly done by layout util, so class is a little bit
 * thin right now.
 */

package me.lsdo.processing;

import java.util.*;
//import processing.core.PVector;

public class Dome {

    // Positions of all the pixels in triangular grid coordinates (and in the order seen by
    // the fadecandy).
    public ArrayList<DomeCoord> coords;

    // Mapping of pixel grid coordinates to xy locations (world coordinates, not screen
    // coordinates!)
    // Question: is this Intermediate representation?
    private HashMap<DomeCoord, PVector2> points;

    // Color
    private HashMap<DomeCoord, Integer> colors;

    // Size of a single panel's pixel grid
    private int panel_size;

    // Distance from center to farthest pixel, in panel lengths
    private double radius;

    public Dome() {
        this(Config.getConfig().numPanels);
    }

    public Dome(int numPanels) {
	this(LayoutUtil.getPanelLayoutForNumPanels(numPanels));
	System.out.println(String.format("Using %d-panel layout", numPanels));
    }

    protected Dome(PanelLayout layout) {
        // e.g. 15
        panel_size = Config.PANEL_SIZE;

	LayoutUtil.PanelConfig config = LayoutUtil.getPanelConfig(layout);
	
        coords = config.fill(panel_size);
        points = config.coordsToXy(coords);
        colors = new HashMap<DomeCoord, Integer>();

	radius = config.radius;

        for (DomeCoord c : coords) {
            setColor(c, 0);
	}

    }

    public Integer getColor(DomeCoord dCoord){
      return colors.get(dCoord);
    }

    public void setColor(DomeCoord dCoord, Integer color){
      colors.put(dCoord, color);
    }

    public PVector2 getLocation(DomeCoord dCoord){
        return points.get(dCoord);
    }

    public int getNumPoints(){
        return points.size();
    }

    public int getPanelSize(){
        return panel_size;
    }

    public double getRadius(){
        return radius;
    }

    public PVector2 domeCoordToScreen(DomeCoord c, int width, int height) {
	return LayoutUtil.xyToScreen(getLocation(c),
				     width, height, 2 * getRadius(), true);
    }

    public PVector2[] getViewport() {
	return getViewport(0.);
    }
    
    // Returns the bounding rectangular viewport for the dome pixel area (rotated by angle 'rot').
    // 1st vector in the array is the lower left corner; 2nd vector is the width/height.
    public PVector2[] getViewport(double rot) {
        double xmin = getRadius();
        double xmax = -getRadius();
        double ymin = getRadius();
        double ymax = -getRadius();
        for (DomeCoord c : coords) {
	    PVector2 p = LayoutUtil.Vrot(getLocation(c), rot);
            xmin = Math.min(xmin, p.x);
            xmax = Math.max(xmax, p.x);
            ymin = Math.min(ymin, p.y);
            ymax = Math.max(ymax, p.y);
        }
        double margin = .5*LayoutUtil.pixelSpacing(getPanelSize());
        xmin -= margin;
        xmax += margin;
        ymin -= margin;
        ymax += margin;

	PVector2 p0 = LayoutUtil.V(xmin, ymin);
        PVector2 pdiag = LayoutUtil.Vsub(LayoutUtil.V(xmax, ymax), p0);
	return new PVector2[] {p0, pdiag};
    }
}
