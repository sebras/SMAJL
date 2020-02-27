package com.brunschen.christian.graphic;

public class Rectangle {
  
  public double x, y, width, height;
  public Object cached = null;

  public Rectangle(Point origin, Size size) {
    this.x = origin.x;
    this.y = origin.y;
    this.width = size.width;
    this.height = size.height;
  }
  
  public Rectangle(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public Rectangle(Rectangle r) {
    this.x = r.x;
    this.y = r.y;
    this.width = r.width;
    this.height = r.height;
  }
  
  public void set(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.cached = null;
  }

  public Rectangle intersection(Rectangle r) {
    if (x + width < r.x || y + height < r.y || r.x + r.width < x || r.y + r.height < y) {
      return empty();
    }
    double x0 = Math.max(x, r.x);
    double y0 = Math.max(y, r.y);
    double x1 = Math.min(x + width, r.x + r.width);
    double y1 = Math.min(y + height, r.y + r.height);
    return new Rectangle(x0,  y0, x1 - x0, y1 - y0);
  }
  
  public Rectangle union(Rectangle r) {
    double x0 = Math.min(x, r.x);
    double y0 = Math.min(y, r.y);
    double x1 = Math.max(x + width, r.x + r.width);
    double y1 = Math.max(y + height, r.y + r.height);
    return new Rectangle(x0,  y0, x1 - x0, y1 - y0);
  }
  
  public boolean intersects(Rectangle r) {
    if (x + width < r.x || y + height < r.y || r.x + r.width < x || r.y + r.height < y) {
      return false;
    }
    return true;
  }

  public double getX() {
    return x;
  }
  
  public double getY() {
    return y;
  }
  
  public double getWidth() {
    return width;
  }
  
  public double getHeight() {
    return height;
  }
  
  public double getMinX() {
    return x;
  }
  
  public double getMinY() {
    return y;
  }
 
  public double getMaxX() {
    return x + width;
  }
  
  public double getMaxY() {
    return y + height;
  }
  
  public double getCenterX() {
    return x + width / 2.0;
  }
  
  public double getCenterY() {
    return y + height / 2.0;
  }
  
  public boolean contains(Point p) {
    double dx = p.x - x;
    if (dx < 0 || width < dx) {
      return false;
    }
    double dy = p.y - y;
    if (dy < 0 || height < dy) {
      return false;
    }
    return true;
  }
  
  public void include(double x, double y) {
    if (Double.isNaN(this.x)) {
      this.x = x;
      this.y = y;
      this.width = 0;
      this.height = 0;
    } else {
      if (x < this.x) {
        this.width += this.x - x;
        this.x = x;
      } else if (this.x + this.width < x) {
        this.width = x - this.x;
      }
      if (y < this.y) {
        this.height += this.y - y;
        this.y = y;
      } else if (this.y + this.height < y) {
        this.height = y - this.y;
      }
    }
  }
  
  public void include(Rectangle r) {
    double x0 = Math.min(x, r.x);
    double y0 = Math.min(y, r.y);
    double x1 = Math.max(x + width, r.x + r.width);
    double y1 = Math.max(y + height, r.y + r.height);
    set(x1, y0, x1 - x0, y1 - y0);
  }

  public String toString() {
    return String.format("Rectangle(x=%f, y=%f, width=%f, height=%f)", x, y, width, height);
  }

  public Path toPath() {
    return new Path().moveTo(x, y)
        .lineTo(x + width, y)
        .lineTo(x + width, y + height)
        .lineTo(x,  y + height)
        .closePath();
  }
  
  public Rectangle boundingBox() {
    return new Rectangle(x, y, width, height);
  }

  public Size size() {
    return new Size(width, height);
  }
  
  public static Rectangle empty() {
    return new Rectangle(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
  }
}
