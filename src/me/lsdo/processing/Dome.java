/**
 * The abstract structure of the dome. No aware of any sketches.
 *
 * Config is pulled in from elsewhere, and the layout is mostly done by layout util, so class is a little bit
 * thin right now.
 */

package me.lsdo.processing;

import java.util.*;
import processing.core.*;

public class Dome {

    // Positions of all the pixels in triangular grid coordinates (and in the order seen by
    // the fadecandy).
    public ArrayList<DomeCoord> coords;

    // Mapping of pixel grid coordinates to xy locations (world coordinates, not screen
    // coordinates!)
    private HashMap<DomeCoord, PVector> points;

    // Color
    private HashMap<DomeCoord, Integer> colors;

    // Size of a single panel's pixel grid
    private int panel_size;

    // Distance from center to farthest pixel, in panel lengths
    private double radius;

    public Dome() {

        // e.g. 15
        panel_size = Config.PANEL_SIZE;

        LayoutUtil.PanelConfig config = LayoutUtil.getPanelConfig(Config.PANEL_LAYOUT);
        coords = config.fill(panel_size);
        points = config.coordsToXy(coords);
        colors = new HashMap<DomeCoord, Integer>();

        radius =LayoutUtil.getPanelConfig(Config.PARTIAL_LAYOUT ?
                Config.FULL_PANEL_LAYOUT :
                Config.PANEL_LAYOUT).radius;

        for (DomeCoord c : coords)
            setColor(c, 0);

    }

    public Integer getColor(DomeCoord dCoord){
      return colors.get(dCoord);
    }

    public void setColor(DomeCoord dCoord, Integer color){
      colors.put(dCoord, color);
    }

    public PVector getLocation(DomeCoord dCoord){
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

}
