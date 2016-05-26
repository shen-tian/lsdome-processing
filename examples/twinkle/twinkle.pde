// Twinkling stars.

import me.lsdo.processing.*;

//FadecandySketch driver = new TwinkleSketch(this, 300);
TwinkleSketch twinkle;

Dome dome;
OPC opc;

void setup() {
    size(300, 300);
  dome = new Dome(300);
  dome.init();
  opc = new OPC(this, "127.0.0.1", 7890);
  opc.setDome(dome);
  twinkle = new TwinkleSketch(this,dome);
  twinkle.init();
  colorMode(HSB,255);
}

void draw() {
  for (DomeCoord c : dome.coords){
      
      dome.setColor(c, twinkle.drawPixel(c, millis()/1000d));
  }
  
      background(0);
    noStroke();
    for (DomeCoord c : dome.coords){
        PVector p = dome.xyToScreen(dome.points.get(c));
        fill(dome.getColor(c));
           ellipse(p.x, p.y, 3, 3);
        }
        
    fill(128);
    text("opc @" + opc.host, 100, height - 10);
    text(String.format("%.1ffps", frameRate), 10, height - 10);
  
}
