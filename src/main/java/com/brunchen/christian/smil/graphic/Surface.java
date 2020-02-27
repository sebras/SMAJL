package com.brunschen.christian.graphic;

public abstract class Surface {  
  public static final int TEXT_ALIGNMENT_LEFT = 0;
  public static final int TEXT_ALIGNMENT_CENTER = 1;
  public static final int TEXT_ALIGNMENT_RIGHT = 2;

  protected AffineTransform transform = new AffineTransform();
  protected Color color = Color.BLACK;
  protected Font font = null;
  protected int textAligment = TEXT_ALIGNMENT_LEFT;
  
  public Surface() {
  }
  
  public abstract void save();
  
  public abstract void restore();
  
  public abstract void transform(AffineTransform transform);
  
  public abstract double getScale();
  
  public abstract void scale(double sx, double sy);
  
  public abstract void translate(double tx, double ty);
  
  public abstract void rotate(double theta);

  public abstract void fill(Rectangle r);

  public abstract void fill(Ellipse c);
  
  public abstract void fill(Path path);

  public abstract void stroke(Rectangle r);

  public abstract void stroke(Ellipse c);
  
  public abstract void stroke(Path path);

  public abstract void drawImage(Image image, double scale);

  public void drawImage(Image image) {
    drawImage(image, 1.0);
  }
  
  public abstract void setColor(Color c);
  
  public abstract void setGradient(RadialGradient gradient);
  
  public abstract void setStrokeStyle(StrokeStyle strokeStyle);
  
  public abstract Image makeImage(int width, int height);
  
  public abstract void clip(Path clip);
  
  public abstract void clip(Rectangle r);

  public abstract void clip(Ellipse e);

  public void setFont(Font font) {
    this.font = font;
  }
  
  public Font font() {
    return this.font;
  }
  
  public void setTextAlignment(int alignment) {
    textAligment = alignment;
  }
  
  public int getTextAlignment() {
    return textAligment;
  }

  public abstract void drawString(String s, double f, double g);
  
  public abstract Rectangle measureString(String s);

  public abstract Rectangle getBoundingBox(String s);

  public abstract boolean skip(double x, double y, double width, double height);
  
  public boolean skip(Rectangle r) {
    return skip(r.x, r.y, r.width, r.height);
  }
}
