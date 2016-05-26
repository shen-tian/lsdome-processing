# lsdome-processing
Processing Library for LSDome

## Note for implementation

FadecandySketch is the base class.

Cloud extends PointSampleSketch extends PixelGridSketch extends FadecandySketch
CloudFlock and PixelFlock extends FadecandySketch directly, but not sure if it's used as
(mrgriscom) intended.
gittest extends FadecandySketch
KaleidoscopeSketch extends PixelGridSketch extends FadecandySketch
pixel_test extends PixelGridSketch extends FadecandySketch
Ripple extends FadecandySketch
The two FFT Sketches extend FadecandySketch directly. Once again, not sure if
that's right
Tube extends PointSampleSketck
Twinkle extends PixelGridSketch
VideoPlayer extends FadecandySketch

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
