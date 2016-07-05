# lsdome library for Processing

[![Build Status](https://travis-ci.org/shen-tian/lsdome-processing.svg?branch=master)](https://travis-ci.org/shen-tian/lsdome-processing)

Processing Library for [Limitless Slip Dome](https://github.com/shen-tian/l-s-dome).
This uses the correct format for Processing [Contributed Libraries]
(https://github.com/processing/processing/wiki/How-to-Install-a-Contributed-Library). It still
targets Processing 2.2.1, thus Java 1.6. Not sure if it will change.

## Goals

Make it easy to target the Limitless Slip Dome, with its particular geometry,
over [Open Pixel Control](http://openpixelcontrol.org/). Note that this is
not actually dependent on use of Fadecady controller boards.

This helps with two related aspects that are non-trivial: laying out of the pixels
in the triangular pattern, mirroring the physical wiring of the dome. This is most
useful for sketches which are indifferent to pixel layout; it also
allows easier use of animations that specifically takes advantage of this geometry
 (see `kaleidoscope`).

Potential future goal: define a container that allows sketches to be put into
an animation playlist of sorts.

## Build/Install

Need to flash out, but in short:

1. `git clone` somewhere.
2. `./gradlew makeArtifact` to build. This creates `lsdome-processing.zip` in `\build\distributions\`.
3. Unzip the contents into your Processing `libraries` folder.

## Use

### Simple

Simplest is `SimpleSketch`. This gives you the shortest path to porting an existing
sketch to the dome.

Simple add these lines:

    import me.lsdo.processing.*;
    SimplestSketch sketch;

to near the top of the file. Then, to the `setup` method, add

    sketch = new SimplestSketch(this, new Dome(), new OPC("127.0.0.1", 7890));

and to the end of the `draw` method:

    sketch.draw();

have a look at the `dot` example to see this in action. What this does is to sample
directly from the sketch, after is has been rendered. It applies a bit of
anti-aliasing and motion blur to smooth things out.

### XY Animation

This mode is for when you render a scene via its XY coorodinate. In essence. You'll
be implementing the function (pseudocode):

    color drawScene(int xCoord, int yCoord, int time)

Have a look at the `Cloud` example for how this works. It doesn't actually use
X and Y coordinates, but a PVector. The color is calculated, and then drawn onto
the Sketch.

### Dome Animation

This mode is when you are working with the Dome directly. That way, you can play
with the funky UVW co-ordinate system. See `Kaleidoscope` for a good example.

## Design Notes (Legacy)

The general idea, I think, is that:

* `FadecandySketch` does the basic stuff, init, and scaffolding for general
sketch stuff.  
* `PixelGridSketch` exposes the geometry of sketches, so they can be individually
controlled.
* `PointSampleSketch` samples from the Processing window.

Not all the dome code followed this intended structure, as a lot of it just
went straight to `FadecandySketch`. :(

`TriCoord`, `DomeCoord`, `LayoutUtil`, `MathUtil` handles the 2D geometry.
Config is more of a placeholder right now.

`OPC` is based on Micah Scott's code from the Fadecandy examples.

of the initial set of sketches that were used at AfrikaBurn 2016:

* `Cloud` and `tube` extends `PointSampleSketch` extends `PixelGridSketch` extends `FadecandySketch`.

* `KaleidoscopeSketch`, `twinkle`, `grid_test` and `pixel_test` extends `PixelGridSketch` extends `FadecandySketch`.

* `CloudFLock`, `PixelFlock`, `giftest`, `Ripple`, the two FFT sketches and `VideoPlayer`
extends `FadecandySketch` directly, but I'm not sure if that's the intended design.
