import me.lsdo.processing.*;
import codeanticode.syphon.*;

CanvasSketch sketch;
PGraphics canvas;
SyphonClient client;

int pitch = 50;

void setup()
{
    size(300, 300, P3D);
    
    frameRate(24);
    
    Dome dome = new Dome(8);
    OPC opc = new OPC("192.168.1.11",7890);

    sketch = new CanvasSketch(this, dome, opc, 16);
    println("Available Syphon servers:");
    println(SyphonClient.listServers());

    // Create syhpon client to receive frames 
    // from the first available running server: 
    client = new SyphonClient(this);

    background(0);
}

void draw()
{
    background(0);

    if (client.newFrame()) {
        background(0);
        canvas = client.getGraphics(canvas);
        image(canvas, 0, 0, width, height);
    }
    
    sketch.draw();
}
