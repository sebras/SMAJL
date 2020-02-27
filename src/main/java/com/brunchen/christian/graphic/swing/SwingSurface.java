package com.brunschen.christian.graphic.swing;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import com.brunschen.christian.graphic.Color;
import com.brunschen.christian.graphic.Ellipse;
import com.brunschen.christian.graphic.Font;
import com.brunschen.christian.graphic.Image;
import com.brunschen.christian.graphic.Path;
import com.brunschen.christian.graphic.Path.Step;
import com.brunschen.christian.graphic.RadialGradient;
import com.brunschen.christian.graphic.Rectangle;
import com.brunschen.christian.graphic.StrokeStyle;
import com.brunschen.christian.graphic.Surface;

public class SwingSurface extends Surface {

  private static RenderingHints RENDERING_HINTS = new RenderingHints(null);
  static {
    RENDERING_HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    RENDERING_HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    RENDERING_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
  }

  // Before Java 7, radial gradients were offset by 0.5 pixel. This tries to adapt to that.
  private static double GRADIENT_OFFSET = 0.0;
//  static {
//    String version = System.getProperty("java.version");
//    String vmVendor = System.getProperty("java.vm.vendor");
//    String vendor = System.getProperty("java.vendor");
//    System.err.format("version '%s', vendor '%s', vm vendor '%s'%n", version, vendor, vmVendor);
//    String[] parts = version.split("[._-]");
//    if (Integer.parseInt(parts[0]) <= 1 && Integer.parseInt(parts[1]) <= 6) {
//      GRADIENT_OFFSET = -0.5;
//    }
//    // System.out.format("Java version = '%s' => parts = %s => GRADIENT_OFFSET = %f%n", 
//    //    version, Arrays.toString(parts), GRADIENT_OFFSET);
//  }
  private static final double GRADIENT_SCALE = 16.0; 
  private static final AffineTransform GRADIENT_TRANSFORM = AffineTransform.getScaleInstance(GRADIENT_SCALE, GRADIENT_SCALE);
  private static final AffineTransform GRADIENT_INVERSE_TRANSFORM = AffineTransform.getScaleInstance(1.0/GRADIENT_SCALE, 1.0/GRADIENT_SCALE);

  private Graphics2D graphics;
  
  private Stack<AffineTransform> transformStack = new Stack<AffineTransform>();
  private Stack<Shape> clipStack = new Stack<Shape>();
    
  public SwingSurface(Graphics2D graphics) {
    this.graphics = graphics;
    graphics.addRenderingHints(RENDERING_HINTS);
  }

  public void transform(com.brunschen.christian.graphic.AffineTransform t) {
    graphics.transform(new AffineTransform(t.xx, t.xy, t.yx, t.yy, t.tx, t.ty));
  }
  
  public void save() {
    transformStack.push(graphics.getTransform());
    clipStack.push(graphics.getClip());
  }

  public void restore() {
    graphics.setTransform(transformStack.pop());
    graphics.setClip(clipStack.pop());
  }

  public double getScale() {
    return graphics.getTransform().getScaleX();
  }

  public void scale(double sx, double sy) {
    graphics.scale(sx, sy);
  }
  
  public void translate(double tx, double ty) {
    graphics.translate(tx, ty);
  }
  
  public void rotate(double theta) {
    graphics.rotate(theta);
  }

  @Override
  public void fill(Rectangle r) {
    graphics.fill(toRectangle2D(r));
  }

  @Override
  public void fill(Ellipse c) {
    graphics.fill(toEllipse2D(c));
  }

  @Override
  public void drawImage(Image image, double scale) {
    if (image instanceof SwingImage) {
      SwingImage swingImage = (SwingImage) image;
      BufferedImage bufferedImage = swingImage.getBufferedImage();
      graphics.drawImage(bufferedImage, AffineTransform.getScaleInstance(scale, scale), null);
    } else {
      // can't draw a non-Swing image
    }
  }

  @Override
  public void setColor(Color c) {
    graphics.setPaint(null);
    graphics.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
  }

  @Override
  public void setGradient(RadialGradient gradient) {
    if (gradient.cached == null || !(gradient.cached instanceof RadialGradientPaint)) {
      float[] fractions = new float[gradient.distances.length];
      for (int i = 0; i < gradient.distances.length; i++) {
        fractions[i] = (float) gradient.distances[i];
      }
      
      java.awt.Color[] colors = new java.awt.Color[gradient.colors.length];
      for (int i = 0; i < gradient.colors.length; i++) {
        Color c = gradient.colors[i];
        colors[i] = new java.awt.Color(c.r, c.g, c.b, c.a);
      }
      
      Point2D center = new Point2D.Double(gradient.x + GRADIENT_OFFSET, gradient.y + GRADIENT_OFFSET);
      center = GRADIENT_TRANSFORM.transform(center, center);
      gradient.cached = new RadialGradientPaint(center, (float) (gradient.r * GRADIENT_SCALE), center, fractions, colors,
          CycleMethod.NO_CYCLE, ColorSpaceType.LINEAR_RGB, GRADIENT_INVERSE_TRANSFORM);
    }
    RadialGradientPaint paint = (RadialGradientPaint) gradient.cached;
    graphics.setColor(null);
    graphics.setPaint(paint);
  }

  @Override
  public Image makeImage(int width, int height) {
    return new SwingImage(width, height);
  }

  @Override
  public void setFont(Font font) {
    super.setFont(font);
    if (font instanceof SwingFont) {
      graphics.setFont(((SwingFont) font).awtFont);
    }
  }

  @Override
  public void fill(Path path) {
    graphics.fill(toPath2D(path));
  }

  @Override
  public void stroke(Rectangle r) {
    graphics.draw(toRectangle2D(r));
  }

  @Override
  public void stroke(Ellipse c) {
    graphics.draw(toEllipse2D(c));
  }

  @Override
  public void stroke(Path path) {
    graphics.draw(toPath2D(path));
  }

  @Override
  public void setStrokeStyle(StrokeStyle strokeStyle) {
    graphics.setStroke(new BasicStroke((float) strokeStyle.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
  }
  
  protected static Ellipse2D toEllipse2D(Ellipse e) {
    if (e.cached == null || !(e.cached instanceof Ellipse2D)) {
      e.cached = new Ellipse2D.Double(e.x, e.y, e.width, e.height);
    }
    return (Ellipse2D) e.cached;
  }

  protected static Rectangle2D toRectangle2D(Rectangle e) {
    if (e.cached == null || !(e.cached instanceof Rectangle2D)) {
      e.cached = new Rectangle2D.Double(e.x, e.y, e.width, e.height);
    }
    return (Rectangle2D) e.cached;
  }

  protected static Path2D toPath2D(Path path) {
    Path2D.Double p;
    
    if (path.cached != null && path.cached instanceof Path2D) {
      return (Path2D) path.cached;
    } else {
      p = new Path2D.Double();
      
      for (Path.Step step : path.steps) {
        double[] c = step.coords;
        switch(step.kind) {
        case Step.MOVE_TO:
          p.moveTo(c[0], c[1]);
          break;
        case Step.LINE_TO:
          p.lineTo(c[0], c[1]);
          break;
        case Step.ARC:
          p.append(new Arc2D.Double(c[0], c[1], c[2], c[3], c[4], c[5], Arc2D.OPEN), true);
          break;
        case Step.QUADRATIC:
          p.quadTo(c[0], c[1], c[2], c[3]);
          break;
        case Step.CUBIC:
          p.curveTo(c[0], c[1], c[2], c[3], c[4], c[5]);
          break;
        case Step.CLOSE_PATH:
          p.closePath();
          break;
        }
      }
      
      path.cached = p;
    }
    
    return p;
  }
  
  protected static void appendToPath(Path path, PathIterator pi) {
    double c[] = new double[6];
    while (!pi.isDone()) {
      int seg = pi.currentSegment(c);
      pi.next();
      
      switch(seg) {
      case PathIterator.SEG_MOVETO:
        path.moveTo(c[0], c[1]);
        break;
      case PathIterator.SEG_LINETO:
        path.moveTo(c[0], c[1]);
        break;
      case PathIterator.SEG_QUADTO:
        path.quadraticTo(c[0], c[1], c[2], c[3]);
        break;
      case PathIterator.SEG_CUBICTO:
        path.cubicTo(c[0], c[1], c[2], c[3], c[4], c[5]);
        break;
      case PathIterator.SEG_CLOSE:
        path.closePath();
        break;
      }
    }
  }

  protected static Path toPath(Path2D p) {
    Path path = new Path();
    path.cached = p;
    appendToPath(path, p.getPathIterator(null));
    return path;
  }

  @Override
  public void clip(Path clip) {
    Path2D path2D = toPath2D(clip); 
    graphics.clip(clip == null ? null : path2D);
  }

  @Override
  public void clip(Rectangle r) {
    int xx = (int) Math.floor(r.getMinX());
    int yy = (int) Math.floor(r.getMinY());
    int ww = (int) Math.ceil(r.getMaxX()) - xx;
    int hh = (int) Math.ceil(r.getMaxY()) - yy;
    graphics.clipRect(xx, yy, ww, hh);
  }

  @Override
  public void clip(Ellipse e) {
    graphics.clip(new Ellipse2D.Double(e.x, e.y, e.width, e.height));
  }

  @Override
  public void drawString(String s, double x, double y) {
    FontRenderContext frc = graphics.getFontRenderContext();

    Rectangle2D rect = graphics.getFont().getStringBounds(s, frc);
    double dx = textAligment == TEXT_ALIGNMENT_LEFT ? 0
        : textAligment == TEXT_ALIGNMENT_CENTER ? rect.getWidth() / 2.0
        : textAligment == TEXT_ALIGNMENT_RIGHT ? rect.getWidth()
        : 0;
        
    graphics.drawString(s, (float) (x - dx), (float) y);
  }
  
  @Override
  public Rectangle measureString(String s) {
    FontRenderContext frc = graphics.getFontRenderContext();
    Rectangle2D rect = graphics.getFont().getStringBounds(s, frc);
    double dx = textAligment == TEXT_ALIGNMENT_LEFT ? 0
        : textAligment == TEXT_ALIGNMENT_CENTER ? rect.getWidth() / 2.0
        : textAligment == TEXT_ALIGNMENT_RIGHT ? rect.getWidth()
        : 0;
    return new Rectangle(rect.getMinX() - dx, rect.getY(), rect.getWidth(), rect.getHeight());
  }
  
  @Override
  public Rectangle getBoundingBox(String s) {
    FontRenderContext frc = graphics.getFontRenderContext();
    GlyphVector glyphVector = graphics.getFont().createGlyphVector(frc, s);
    Shape outline = glyphVector.getOutline();
    Rectangle2D rect = outline.getBounds2D();
    double dx = textAligment == TEXT_ALIGNMENT_LEFT ? 0
        : textAligment == TEXT_ALIGNMENT_CENTER ? rect.getWidth() / 2.0
        : textAligment == TEXT_ALIGNMENT_RIGHT ? rect.getWidth()
        : 0;
    return new Rectangle(rect.getMinX() - dx, rect.getMinY(), rect.getWidth(), rect.getHeight());
  }

  @Override
  public boolean skip(double x, double y, double width, double height) {
    int xx = (int) Math.floor(x);
    int yy = (int) Math.floor(y);
    int ww = (int) Math.ceil(x + width) - xx;
    int hh = (int) Math.ceil(y + height) - yy;
    return !graphics.hitClip(xx, yy, ww, hh);
  }
}
