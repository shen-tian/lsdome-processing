import me.lsdo.processing.*;

KaleidoscopeSketch kaleido;

void setup() {
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);
    kaleido = new KaleidoscopeSketch(this, dome, opc);
}

void draw() {
    kaleido.draw();
}
