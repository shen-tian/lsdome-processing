import me.lsdo.processing.*;

PixelGridSketch kaleido;

void setup() {
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);
    kaleido = new PixelGridSketch(this, new KaleidoscopeSketch(dome, opc));
}

void draw() {
    kaleido.draw();
}
