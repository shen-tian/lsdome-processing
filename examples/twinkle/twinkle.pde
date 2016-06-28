// Twinkling stars.

import me.lsdo.processing.*;

TwinkleSketch twinkle;

void setup() {
    size(300, 300);
    twinkle = new TwinkleSketch(this, new Dome(), new OPC("127.0.0.1", 7890));
}

void draw() {
    twinkle.draw();
}
