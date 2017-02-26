import me.lsdo.processing.*;

CanvasSketch sketch;

int pitch = 50;

void setup()
{
    size(300, 300);
    Dome dome = new Dome();
    OPC opc = new OPC("127.0.0.1", 7890);

    sketch = new CanvasSketch(this, dome, opc, 16);

    colorMode(HSB, 256);
}

void draw()
{
    background(0);

    float t = millis();
    float xpos = t/20;

    for (int i = 0; i < 3; i++) {

        fill(i * 80, 192, 255);
        
        float thisPos = (xpos + i * (2 * pitch)) % width;
        rect (thisPos, 0, pitch, height);

        if (thisPos > width - pitch)
            rect (thisPos - width, 0, pitch, height);
    }
    sketch.draw();
}

