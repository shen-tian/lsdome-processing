import me.lsdo.processing.*;

PixelGridSketch sketch;

void setup() {
    size(450, 450);
    Dome dome = new Dome();
    OPC opc = new OPC();
    DomeAnimation animation = new KaleidoscopeSketch(dome, opc);
    sketch = new PixelGridSketch(this, animation);
}

void draw() {
    sketch.draw();
}
