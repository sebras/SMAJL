package com.brunschen.christian.graphic;

public class Ellipse {

  public double x;
  public double y;
  public double width;
  public double height;
  public Object cached = null;

  public Ellipse(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Ellipse(Rectangle r) {
    this.x = r.x;
    this.y = r.y;
    this.width = r.width;
    this.height = r.height;
  }

  public double getMinX() {
    return x;
  }

  public double getMinY() {
    return y;
  }

  public double getCenterX() {
    return x + width / 2.0;
  }

  public double getCenterY() {
    return y + height / 2.0;
  }

  public double getMaxX() {
    return x + width;
  }

  public double getMaxY() {
    return y + height;
  }
  
  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }
  
  public Path toPath() {
    return new Path().addArc(x, y, width, height, 0, 360).closePath();
  }
  
  public Rectangle boundingBox() {
    return new Rectangle(x, y, width, height);
  }
}
