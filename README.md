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

## Build/Testing

Gradle is working. Go

    gradle build

and find the jar file in `build\libs`. Will figure out a better
solution later. This all runs on travis-ci too. Click the badge at top of this
file to go see what's happening there.

### Manual build

To build the code, have a look at `src\build.sh`. It's dependent on both the
location of processing 2.2.1 jar files, but also on where the processing Libraries
are stored.

The rough steps are:

1. Build the library code;
2. JAR the whole lot into `lsdome.jar`;
3. Deploy the whole library directory into the processing folder.

## Using this

For here, the library is available to all sketches, written and ran through the
Processing IDE. Just include:

    import me.lsdo.processing.* ;

on the top of the `.pde` files. Yup. That's the package name. Following naming
standard like a pro.

While doing dev on the library, it might be a good idea to either:

* `git clone` into the processing libraries folder, and work from there.
* work in whatever folder, but have an easy way to copy output to the processing
folder to test the build.

Later, we might want some CI/testing, and create a zip of the deployable library.
one day.

## Design Notes

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
