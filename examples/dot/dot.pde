import me.lsdo.processing.*;

DotSketch dot;

void setup()
{
    size(300, 300);
    Dome dome = new Dome(6);
    OPC opc = new OPC("127.0.0.1", 7890);
    dot = new DotSketch(this, dome, opc);
}

void draw()
{
    dot.draw();
}

