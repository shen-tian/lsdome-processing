/*
 * Fractal noise animation. Modified version of Micah Scott's code at 
 * https://github.com/scanlime/fadecandy/tree/master/examples/processing/grid24x8z_clouds
 */
import me.lsdo.processing.*;

CloudsSketch cloud;
Dome dome;
OPC opc;
void setup() {
    size(300, 300);
    dome = new Dome();
    opc = new OPC("127.0.0.1", 7890);
    cloud = new CloudsSketch(this, dome, opc);
    
}

void draw() {
  for (DomeCoord c : dome.coords){
      
      dome.setColor(c, cloud.samplePoint(dome.getLocation(c), millis()/1000d, 0));
  }
  
      background(0);
    noStroke();
    for (DomeCoord c : dome.coords){
        PVector p = LayoutUtil.xyToScreen(dome.getLocation(c), width, height, 2*dome.getRadius(), true);
        fill(dome.getColor(c));
           ellipse(p.x, p.y, 3, 3);
        }
     opc.draw();
        
    fill(128);
    text("opc @" + opc.getHost(), 100, height - 10);
    text(String.format("%.1ffps", frameRate), 10, height - 10);
  
}

void keyPressed(){
   //driver.processKeyInput();
}


