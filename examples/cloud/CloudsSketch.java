/*
 * Fractal noise animation. Modified version of Micah Scott's code at 
 * https://github.com/scanlime/fadecandy/tree/master/examples/processing/grid24x8z_clouds
 */

import processing.core.*;
import me.lsdo.processing.*;

public class CloudsSketch extends PointSampleSketch<PVector, CloudsState> {

    CloudsState state;
    double last_t;
    int mode;

    double noiseScale=0.02;

    CloudsSketch(PApplet app, Dome dome, int size_px) {
        super(app, dome, size_px);
        mode = 1;
        
        state = initialState();
    }

    protected CloudsState initialState() {
        return new CloudsState();
    }

    protected CloudsState updateState(CloudsState state, double delta_t) {
        double t = last_t + delta_t;
        double speed = 0.06;
        double zspeed = 3.;
        double angle = Math.sin(t);

        state.dx += Math.cos(angle) * speed * delta_t;
        state.dy += Math.sin(angle) * speed * delta_t;
        state.dz += (noise(t * 0.014) - 0.5) * zspeed * delta_t;
        return state;
    }

    protected int samplePoint(PVector p, double t, double t_jitter) {
        switch (mode) {
        case 0:
            return drawCloud(p, t);
        case 1:
            return drawRing(p, t);          
        default:
            throw new RuntimeException();
        }
    }
    
    void processKeyInput(){
        if (app.keyPressed) {
            if (app.key == 'c') {
                mode = 0;
            }
            if (app.key == 'r') {
                mode = 1;
            }
        }
    }

    protected void beforeFrame(double t) {
    }

    void afterFrame(double t) {
    }

    int drawCloud(PVector p, double t) {
        // Noise patterns are symmetrical around the origin and it looks weird. Move origin to corner.
        p = LayoutUtil.Vadd(p, LayoutUtil.V(1, 1));

        double z = .08 * t;
        double hue = .1 * t;
        double scale = .75;
        
        double n = fractalNoise(state.dx + p.x*scale, state.dy + p.y*scale, z) - 0.75;
        double m = fractalNoise(state.dx + p.x*scale, state.dy + p.y*scale, z + 10.0) - 0.75;
                    
        return app.color(
                     (int)(255 * MathUtil.fmod(hue + .8 * m, 1.)), 
                     (int)(255 * (1. - constrain(Math.pow(3.0 * n, 3.5), 0, 0.9))), 
                     (int)(255 * constrain(Math.pow(3.0 * n, 1.5), 0, 0.9))
                     );
    }

    int drawRing(PVector p, double t) {
        // Noise patterns are symmetrical around the origin and it looks weird. Move origin to corner.
        p = LayoutUtil.Vadd(p, LayoutUtil.V(1, 1));

        double z = .08 * t;
        double hue = .1 * t;
        double scale = .75;
        
        double saturation = constrain(Math.pow(1.15 * noise(t * 0.122), 2.5), 0, 1);
        double spacing = noise(t * 0.124) * 15.;
        
        double centerx = noise(t *  0.125) * 2.5;
        double centery = noise(t * -0.125) * 2.5;
        
        double dist = Math.sqrt(Math.pow(p.x - centerx, 2) + Math.pow(p.y - centery, 2));
        double pulse = (Math.sin(state.dz + dist * spacing) - 0.3) * 0.3;
                    
        double n = fractalNoise(state.dx + p.x*scale + pulse, state.dy + p.y*scale, z) - 0.75;
        double m = fractalNoise(state.dx + p.x*scale, state.dy + p.y*scale, z + 10.0) - 0.75;
                    
        return app.color(
                     (int)(255 * MathUtil.fmod(hue + .4 * m, 1.)), 
                     (int)(255 * saturation),
                     (int)(255 * constrain(Math.pow(3.0 * n, 1.5), 0, 0.9))
                     );
    }

    double fractalNoise(double x, double y, double z) {
        double r = 0;
        double amp = 1.0;
        for (int octave = 0; octave < 4; octave++) {
            r += app.noise((float)x, (float)y, (float)z) * amp;
            amp /= 2;
            x *= 2;
            y *= 2;
            z *= 2;
        }
        return r;
    }

    double noise(double x) {
        return app.noise((float)x);
    }

    double constrain(double x, double min, double max) {
        return app.constrain((float)x, (float)min, (float)max);
    }

}

