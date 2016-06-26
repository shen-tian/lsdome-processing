/*
 * Fractal noise animation. Modified version of Micah Scott's code at 
 * https://github.com/scanlime/fadecandy/tree/master/examples/processing/grid24x8z_clouds
 */
import me.lsdo.processing.*;

CloudsSketch cloud;

void setup() {
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);
    cloud = new CloudsSketch(this, dome, opc);
}

void draw() {
      cloud.draw(); 
}


