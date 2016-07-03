import me.lsdo.processing.*;

PixelGridSketch kaleido;
KaleidoscopeSketch sketch;

void setup() {
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);
    sketch = new KaleidoscopeSketch(dome, opc);
    kaleido = new PixelGridSketch(this, sketch);
}

void draw() {
    kaleido.draw();
    //double t = millis()/1000f;
    //sketch.draw(t);
    
}
