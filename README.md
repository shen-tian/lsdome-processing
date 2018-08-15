# lsdome library for Processing

[![Build Status](https://travis-ci.org/shen-tian/lsdome-processing.svg?branch=master)](https://travis-ci.org/shen-tian/lsdome-processing)
[![Download](https://api.bintray.com/packages/shen-tian/maven/lsdome-processing/images/download.svg)](https://bintray.com/shen-tian/maven/lsdome-processing/_latestVersion)

This is a Processing Library for [Limitless Slip Dome](https://github.com/shen-tian/lsdome).
This uses the correct format for Processing [Contributed Libraries](https://github.com/processing/processing/wiki/How-to-Install-a-Contributed-Library).
Note that this targets Processing 2. No idea if/when we'll support Processing 3.

It can also be used as a straightforward Java library, if you'd prefer to not
use the standard Processing tools, or as someone put it, "avoid the arty bullshit
language that doesn't make sense".

## Goals

Make it easy to target the Limitless Slip Dome, with its particular geometry,
over [Open Pixel Control](http://openpixelcontrol.org/) protocol. Note that this
is not dependent on use of Fadecady controller boards.

This helps with two related aspects that are non-trivial: laying out of the
pixels in the triangular pattern, mirroring the physical wiring of the dome.
This is most useful for sketches which are indifferent to pixel layout; it also
allows easier use of animations that specifically takes advantage of this
geometry (see `kaleidoscope`).

## Build/Install

There's two main ways of using the library: as a Processing Contributed library,
or as a Java library.

### Processing

The process is slightly manual. But we don't want to deal with Processing's
library publishing stuff. Instructions work on Linux and macOS. Something
similar should work in Windows. Assumes you've got Java installed.

1. `git clone` this repo somewhere.
2. `./gradlew makeArtifact` to build. This creates `lsdome.zip` in `\build\distributions\`.
3. Unzip the contents into your Processing `libraries` folder. This seems to be `~\Documents\Processing\libraries`
on macOS and `~/sketchbook/libraries` on linux (which only appears after running processing for the first time). Restart Processing IDE and you are good.

### Plain Java

The library is published at the Maven repo on _jcenter_, so you just need to add it as a dependency into your
 build-runner/project tool of choice. If you are using Gradle:

    repositories {
        jcenter()
    }

    dependencies {
        compile 'me.lsdo.processing:lsdome-processing:0.9.4'
        }

in the right place in your `gradle.build`. If you are using Leiningen:

    :repositories [["jcenter" {:url "http://jcenter.bintray.com"}]]
    :dependencies [
        [me.lsdo.processing/lsdome-processing "0.9.4"]]

in your `project.clj` should do the trick.

### Building the JAR

If you need to do this, for whatever reason, simple `git clone` the repo,
ensure you've got Java installed, and go

    ./gradlew build

builds the JAR and places it in `/build/ibs/`.

### Publishing the JAR

To do this, you need the _jcenter_ API keys. Gradle will look for
`gradle.properties` in the project root folder. It expect something like

    bintrayUser=user-name
    bintrayApiKey=apikeyxxxxxxxxxxxxxxx

No quotes. Then, update version number in `build.gradle`, and `gradle bintrayupload`.


## Use

There's a few ways to create animations with the library.

* _Canvas_ - easiest way possible. Start with almost any Processing sketch you
created or borrowed off of the internet, and add a few lines of code to make it light up
some LEDs. You'd be essentially using the LEDs as a bright but low res display.
* _XY Animation_ - you are rendering a scene using some Maths and shit. Thus,
have some way of turning XY-coordinates (a 2D vector, actually) into a colour.
A typical usecase would be rainbow fractals.
* _Dome Animation_ - this is the most complicated one, and involves understanding the addressing system of the LEDs in
the triangular panels a bit. Best to read some code comments.

### Canvas

Simplest is `CanvasSketch`. This gives you the shortest path to porting an existing
sketch to the dome.

Simple add these lines

    import me.lsdo.processing.*;

    CanvasSketch sketch;

to near the top of the file. Then, to the `setup` method, add

    sketch = new CanvasSketch(this, new Dome(), new OPC());

and to the end of the `draw` method:

    sketch.draw();

have a look at the `stripes` example to see this in action. What this does is
to sample directly from the sketch, after is has been rendered. It applies a
bit of anti-aliasing to smooth things out.

### XY Animation

This mode is for when you render a scene via its XY coorodinate. In essence. You'll
be implementing the function (pseudocode):

    color drawScene(int xCoord, int yCoord, int time)

Have a look at the `Cloud` example for how this works. It doesn't actually use
X and Y coordinates, but a vector. The color is calculated, and then drawn onto
the Sketch.

Note that DomeAnimation doesn't depend on an PApplet, so theoretically, can be
ran in a headless scenario.

### Dome Animation

This mode is when you are working with the Dome directly. That way, you can play
with the funky UVW co-ordinate system. See `Kaleidoscope` for a good example.

### Configuration

There are a few things to specify: OPC Host and port, and dome configuration.

OPC Host and port defaults to `127.0.0.1:7890`. You can change this either by

**Hardcoding**: `opc = new OPC("192.168.1.2", 7890)`

Using the `config.properties` file. e.g.

    opchostname=192.168.1.2
    opcport=7890

place this file in working directory.

The dome layout supports 2 (default), 6 and 13 panels. This is specified
via the `Dome` constructor.

## Design Notes

The general idea:

* `Dome` class contains most of the geometry.
* `OPC` class contains the OPC client. Based on Micah Scott's code from the Fadecandy examples.
* `DomeAnimation` is an abstract class. Extend it to animate the Dome directly, UVW geometry and all.
* `XYAnimation` is an abstract class too. It extends `DomeAnimation`. It abstracts over that geometry.
Converts it to XY coords.
* `PixelGridSketch` wraps both the above animation, together with a `PApplet`. It uses information from the
`Dome` object to draw onto the sketch, so it's really just as feedback. Stuff that gets drawn onto the sketch
via the usual Processing means won't show up on the dome. For that, you'll need...
* `CanvasSketch`, which takes a `PApplet`, and samples its canvas, using that to colour the `Dome`
structure. It also does the AA to compensate for the extreme low resolution of the
dome.

`TriCoord`, `DomeCoord`, `LayoutUtil`, `MathUtil` handles the 2D geometry. It's kind of complicated, read
the source for hints on how they work.
`Config` is more of a placeholder right now.
`OpcColor` has some color related functions.
