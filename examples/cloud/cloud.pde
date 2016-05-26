/*
 * Fractal noise animation. Modified version of Micah Scott's code at 
 * https://github.com/scanlime/fadecandy/tree/master/examples/processing/grid24x8z_clouds
 */
import me.lsdo.*;

CloudsSketch driver = new CloudsSketch(this, 300);

void setup() {
  driver.Run();
}

void draw() {
  driver.draw();
}

void keyPressed(){
   driver.processKeyInput();
}


