import java.util.*;
import processing.core.*;
import me.lsdo.processing.*;

public class DotSketch extends CanvasSketch {

    private PImage dot; 
    
    public DotSketch(PApplet app, Dome dome, OPC opc) {
        super(app, dome, opc);
        
        dot = app.loadImage("dot.png");
        app.colorMode(app.HSB, 256);
    }

   public void paint(){
      app.background(0, 127);

      // Draw the image, centered at the mouse location
      float dotSize = (float)(app.height * 0.33);
      app.image(dot, app.mouseX - dotSize/2, app.mouseY - dotSize/2, dotSize, dotSize);
      
      app.ellipseMode(app.CENTER);
      app.fill(40, 180, 256);
      app.noStroke();
      app.ellipse(app.width - app.mouseX, app.height - app.mouseY, dotSize / 4, dotSize / 4);
   }
}
