package com.brunschen.christian.graphic;

public class AffineTransform {
  
  public double xx, xy, yx, yy, tx, ty;
  
  public AffineTransform(double xx, double xy, double yx, double yy, double tx, double ty) {
    this.xx = xx;
    this.xy = xy;
    this.yx = yx;
    this.yy = yy;
    this.tx = tx;
    this.ty = ty;
  }
  
  public AffineTransform(AffineTransform at) {
    this.xx = at.xx;
    this.xy = at.xy;
    this.yx = at.yx;
    this.yy = at.yy;
    this.yx = at.tx;
    this.ty = at.ty;
  }

  public AffineTransform() {
    this(1, 0, 0, 1, 0, 0);
  }
  
  static public AffineTransform makeIdentity() {
    return new AffineTransform(1, 0, 0, 1, 0, 0);
  }
  
  static public AffineTransform makeTranslate(double tx, double ty) {
    return new AffineTransform(1, 0, 0, 1, tx, ty);
  }
  
  static public AffineTransform makeScale(double sx, double sy) {
    return new AffineTransform(sx, 0, 0, sy, 0, 0);
  }
  
  static public AffineTransform makeRotate(double theta) {
    return new AffineTransform(Math.cos(theta), -Math.sin(theta), Math.sin(theta), Math.cos(theta), 0, 0);
  }
  
  public AffineTransform translate(double tx, double ty) {
    return concat(1, 0, 0, 1, tx, ty);
  }

  public AffineTransform scale(double sx, double sy) {
    return concat(sx, 0, 0, sy, 0, 0);
  }

  public AffineTransform rotate(double theta) {
    return concat(Math.cos(theta), -Math.sin(theta), Math.sin(theta), Math.cos(theta), 0, 0);
  }

  public Point transform(double x, double y) {
    Point result = new Point();
    transform(x, y, result);
    return result;
  }

  public Point transform(Point p) {
    return transform(p.x, p.y);
  }
  
  public void transform(Point in, Point out) {
    transform(in.x, in.y, out);
  }

  public void transform(double x, double y, Point out) {
    out.x = xx*x + xy*y + tx;
    out.y = yx*x + yy*y + ty;
  }
  
  public Path transform(Path p) {
    return p.transformed(this);
  }

  public AffineTransform concat(AffineTransform m) {
    return concat(m.xx, m.xy, m.yx, m.yy, m.tx, m.ty);
  }

  public AffineTransform concat(double oxx, double oxy, double oyx, double oyy, double otx, double oty) {
    double nxx = xx * oxx + xy * oyx;
    double nxy = xx * oxy + xy * oyy;
    double nyx = yx * oxx + yy * oyx;
    double nyy = yx * oxy + yy * oyy;
    double ntx = xx * otx + xy * oty + tx;
    double nty = yx * otx + yy * oty + ty;
    
    xx = nxx;
    xy = nxy;
    yx = nyx;
    yy = nyy;
    tx = ntx;
    ty = nty;
    
    return this;
  }

  public String toString() {
    return String.format("AffineTransform(xx=%f, xy=%f, yx=%f, yy=%f, tx=%f, ty=%f)", xx, xy, yx, yy, tx, ty);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(tx);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(ty);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(xx);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(xy);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(yx);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(yy);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AffineTransform other = (AffineTransform) obj;
    if (Double.doubleToLongBits(tx) != Double.doubleToLongBits(other.tx))
      return false;
    if (Double.doubleToLongBits(ty) != Double.doubleToLongBits(other.ty))
      return false;
    if (Double.doubleToLongBits(xx) != Double.doubleToLongBits(other.xx))
      return false;
    if (Double.doubleToLongBits(xy) != Double.doubleToLongBits(other.xy))
      return false;
    if (Double.doubleToLongBits(yx) != Double.doubleToLongBits(other.yx))
      return false;
    if (Double.doubleToLongBits(yy) != Double.doubleToLongBits(other.yy))
      return false;
    return true;
  }  
}
