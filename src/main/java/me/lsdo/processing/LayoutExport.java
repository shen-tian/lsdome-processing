package me.lsdo.processing;

import java.util.*;
import java.io.*;
import processing.data.*;

// Utility class to dump the various panel layouts to json format readable by the OPC simulator.

// To run:
// java -cp library/lsdome.jar:$HOME/processing-2.2.1/core/library/core.jar me.lsdo.processing.LayoutExport

public class LayoutExport {

    public static void main(String[] args) {
	exportLayoutsForSimulator();
    }

    public static void exportLayoutsForSimulator() {
	for (PanelLayout layout : PanelLayout.values()) {
	    String layoutName = layout.name().substring(1);
	    Dome dome = new Dome(layout);
	    
	    JSONArray values = new JSONArray();
	    for (int i = 0; i < dome.coords.size(); i++) {
		PVector2 xy = dome.getLocation(dome.coords.get(i));
		float[] coordinates = {xy.x, xy.y, 0f};
		JSONObject point = new JSONObject();
		point.setJSONArray("point", new JSONArray(new FloatList(coordinates)));
		values.setJSONObject(i, point);
	    }
	    values.save(new File(String.format("opcsimulator_layout_%s.json", layoutName)), null);
	}
    }
    
}
