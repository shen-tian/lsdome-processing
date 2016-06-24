// Twinkling stars.

import me.lsdo.processing.*;

TwinkleSketch twinkle;

void setup() {
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);
    twinkle = new TwinkleSketch(this, dome, opc);
}

void draw() {
    twinkle.draw();
}
