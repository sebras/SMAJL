package com.brunschen.christian.graphic;

import java.util.LinkedList;
import java.util.List;

public class Path {
  
  public List<Step> steps = new LinkedList<Path.Step>();
  public Object cached = null;
  public Rectangle boundingBox = null;
  
  private double x = Double.NaN, y = Double.NaN;
  
  private static final double MAX_ARC_HEIGHT = 1.0 - Math.cos(Math.PI / 8.0);
  
  public Path() {
  }
  
  public Path moveTo(double x, double y) {
    steps.add(Step.moveTo(x, y));
    this.x = x;
    this.y = y;
    boundingBox = null;
    return this;
  }
  
  public Path rMoveTo(double x, double y) {
    return moveTo(this.x + x, this.y + y);
  }

  public Path lineTo(double x, double y) {
    steps.add(Step.lineTo(x, y));
    this.x = x;
    this.y = y;
    boundingBox = null;
    return this;
  }
  
  public Path rLineTo(double x, double y) {
    return lineTo(this.x + x, this.y + y);
  }

  public Path quadraticTo(double x0, double y0, double x1, double y1) {
    steps.add(Step.quadraticTo(x0, y0, x1, y1));
    this.x = x1;
    this.y = y1;
    boundingBox = null;
    return this;
  }

  public Path rQuadraticTo(double x0, double y0, double x1, double y1) {
    return quadraticTo(x+x0, y+y0, x+x1, y+y1);
  }
  
  public Path cubicTo(double x0, double y0, double x1, double y1, double x2, double y2) {
    steps.add(Step.cubicTo(x0, y0, x1, y1, x2, y2));
    this.x = x2;
    this.y = y2;
    boundingBox = null;
    return this;
  }
  
  public Path rCubicTo(double x0, double y0, double x1, double y1, double x2, double y2) {
    return cubicTo(x+x0, y+y0, x+x1, y+y1, x+x2, y+y2);
  }

  public Path addArc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
    steps.add(Step.arc(x, y, width, height, startAngle, sweepAngle));
    boundingBox = null;
    this.x = x + Math.cos(Math.PI * (startAngle + sweepAngle) / 180.0);
    this.y = y + Math.sin(Math.PI * (startAngle + sweepAngle) / 180.0);
    return this;
  }

  public Path rAddArc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
    return addArc(this.x + x, this.y + y, width, height, startAngle, sweepAngle);
  }

  public Path closePath() {
    steps.add(Step.closePath());
    this. x = this.y = Double.NaN;
    boundingBox = null;
    return this;
  }
  
  public Rectangle boundingBox() {
    if (boundingBox == null) {
      boundingBox = Rectangle.empty();
      for (Step step : steps) {
        double [] c = step.coords;
        switch(step.kind) {
        case Step.CLOSE_PATH:
          break;
        case Step.ARC:
          boundingBox.include(c[0], c[1]);
          boundingBox.include(c[0] + c[2], c[1] + c[3]);
          break;
        default:
          for (int i = 0; i < c.length; i += 2) {
            boundingBox.include(c[i],  c[i+1]);
          }
        }
      }
    }
    return boundingBox;
  }
  
  public Path transformed(AffineTransform at) {
    Point p = new Point(), q = new Point(), r = new Point();
    Path path = new Path();

    for (Step step : steps) {
      switch(step.kind) {
      case Step.MOVE_TO:
        at.transform(step.coords[0], step.coords[1], p);
        path.moveTo(p.x, p.y);
        break;
      case Step.LINE_TO:
        at.transform(step.coords[0], step.coords[1], p);
        path.lineTo(p.x, p.y);
        break;
      case Step.ARC:
        at.transform(step.coords[0], step.coords[1], p);
        at.transform(step.coords[0] + step.coords[2], step.coords[1] + step.coords[3], q);
        path.addArc(p.x, p.y, q.x - p.x, q.y - p.y, step.coords[4], step.coords[5]);
        break;
      case Step.QUADRATIC:
        at.transform(step.coords[0], step.coords[1], p);
        at.transform(step.coords[2], step.coords[3], q);
        path.quadraticTo(p.x, p.y, q.x, q.y);
        break;
      case Step.CUBIC:
        at.transform(step.coords[0], step.coords[1], p);
        at.transform(step.coords[2], step.coords[3], q);
        at.transform(step.coords[4], step.coords[5], r);
        path.cubicTo(p.x, p.y, q.x, q.y, r.x, r.y);
        break;
      case Step.CLOSE_PATH:
        path.closePath();
        break;
      }
    }
    return path;
  }
  
  private static void flattenArc(Path path, double flatness,
      double x0, double y0, double w, double h, double startDegrees, double sweepDegrees) {
    double rx = w / 2.0;
    double ry = h / 2.0;
    double cx = x0 + rx;
    double cy = y0 + ry;
    double start = Math.PI * startDegrees / 180.0;
    double sweep = Math.PI * sweepDegrees / 180.0;
    double rMax = Math.max(Math.abs(rx), Math.abs(ry));
    double arcHeight = Math.min(flatness / rMax, MAX_ARC_HEIGHT);
    double cosHalfArc = 1.0 - arcHeight;
    double halfArc = Math.acos(cosHalfArc);
    double maxArc = 2.0 * halfArc;
    int n = (int) Math.ceil(sweep / maxArc);
    double delta = sweep / n;
    for (int i = 0; i <= n; i++) {
      path.lineTo(
          cx + rx * Math.cos(start + i * delta),
          cy - ry * Math.sin(start + i * delta));
    }
  }
  
  private static void adjust(double[] p, double theta, double cx, double cy, double rx, double ry) {
    double cs = Math.cos(theta);
    double sn = Math.sin(theta);
        
    double x = p[0] * cs - p[1] * sn;
    double y = p[0] * sn + p[1] * cs;
    
    p[0] = cx + rx * x;
    p[1] = cy + ry * y;
  }

  private static void addCubicsForArc(Path path,
      double x0, double y0, double w, double h, double startDegrees, double sweepDegrees) {
    double rx = w / 2.0;
    double ry = h / 2.0;
    double cx = x0 + rx;
    double cy = y0 + ry;
    
    int curves = (int) Math.ceil(sweepDegrees / 90.0);
    sweepDegrees = sweepDegrees / curves;
    double start = Math.PI * startDegrees / 180.0;
    double sweep = Math.PI * sweepDegrees / 180.0;
    
    for (int i = 0; i < curves; i++) {
      double p0[] = { Math.cos(sweep / 2.0), Math.sin(sweep / 2.0) };
      double p1[] = { (4.0 - p0[0]) / 3.0, (1.0 - p0[0]) * (3.0 - p0[0]) / (3.0 * p0[1]) };
      double p2[] = { p1[0], -p1[1] };
      double p3[] = { p0[0], -p0[1] };
      
      double rotate = -(start + sweep / 2.0);
      
      adjust(p0, rotate, cx, cy, rx, ry);
      adjust(p1, rotate, cx, cy, rx, ry);
      adjust(p2, rotate, cx, cy, rx, ry);
      adjust(p3, rotate, cx, cy, rx, ry);

      if (i == 0) {
        path.lineTo(p0[0], p0[1]);
      }
      path.cubicTo(p1[0], p1[1], p2[0], p2[1], p3[0], p3[1]);

      start += sweep;
    }
  }

  private static double distance(double x, double y, double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    return Math.abs(dx*y - dy*x + x0*y1 - x1*y0) / Math.sqrt(dx*dx + dy*dy);
  }
  
  private static void flattenQuadratic(Path path, double flatness, 
      double p0x, double p0y, double p1x, double p1y, double p2x, double p2y) {
    if (Math.abs(distance(p1x, p1y, p0x, p0y, p2x, p2y)) <= flatness) {
      path.lineTo(p2x,  p2y);
    } else {
      double q0x = (p0x + p1x) / 2.0;
      double q0y = (p0y + p1y) / 2.0;
      double q1x = (p1x + p2x) / 2.0;
      double q1y = (p1y + p2y) / 2.0;
      
      double r0x = (q0x + q1x) / 2.0;
      double r0y = (q0y + q1y) / 2.0;
      
      flattenQuadratic(path, flatness, p0x, p0y, q0x, q0y, r0x, r0y);
      flattenQuadratic(path, flatness, r0x, r0y, q1x, q1y, p2x, p2y);
    }
  }
    
  private static void flattenCubic(Path path, double flatness, 
      double p0x, double p0y, double p1x, double p1y, double p2x, double p2y, double p3x, double p3y) {
    if (Math.abs(distance(p1x, p1y, p0x, p0y, p3x, p3y)) <= flatness
        && Math.abs(distance(p2x, p2y, p0x, p0y, p3x, p3y)) <= flatness) {
      path.lineTo(p3x,  p3y);
    } else {
      double q0x = (p0x + p1x) / 2.0;
      double q0y = (p0y + p1y) / 2.0;
      double q1x = (p1x + p2x) / 2.0;
      double q1y = (p1y + p2y) / 2.0;
      double q2x = (p2x + p3x) / 2.0;
      double q2y = (p2y + p3y) / 2.0;
      
      double r0x = (q0x + q1x) / 2.0;
      double r0y = (q0y + q1y) / 2.0;
      double r1x = (q1x + q2x) / 2.0;
      double r1y = (q1y + q2y) / 2.0;

      double s0x = (r0x + r1x) / 2.0;
      double s0y = (r0y + r1y) / 2.0;
      
      flattenCubic(path, flatness, p0x, p0y, q0x, q0y, r0x, r0y, s0x, s0y);
      flattenCubic(path, flatness, s0x, s0y, r1x, r1y, q2x, q2y, p3x, p3y);
    }
  }
  
  public Path flattened(double flatness) {
    Path path = new Path();

    for (Step step : steps) {
      switch(step.kind) {
      case Step.MOVE_TO:
        path.moveTo(step.coords[0], step.coords[1]);
        break;
      case Step.LINE_TO:
        path.lineTo(step.coords[0], step.coords[1]);
        break;
      case Step.ARC:
        flattenArc(path, flatness,
            step.coords[0], step.coords[1], 
            step.coords[2], step.coords[3],
            step.coords[4], step.coords[5]);
        break;
      case Step.QUADRATIC:
        flattenQuadratic(path, flatness,
            path.x, path.y,
            step.coords[0], step.coords[1], 
            step.coords[2], step.coords[3]);
        break;
      case Step.CUBIC:
        flattenCubic(path, flatness,
            path.x, path.y,
            step.coords[0], step.coords[1], 
            step.coords[2], step.coords[3],
            step.coords[4], step.coords[5]);
        break;
      case Step.CLOSE_PATH:
        path.closePath();
        break;
      }
    }
    return path;
  }
  
  public Path withCubics() {
    Path path = new Path();

    for (Step step : steps) {
      switch(step.kind) {
      case Step.MOVE_TO:
        path.moveTo(step.coords[0], step.coords[1]);
        break;
      case Step.LINE_TO:
        path.lineTo(step.coords[0], step.coords[1]);
        break;
      case Step.ARC:
        addCubicsForArc(path,
            step.coords[0], step.coords[1], 
            step.coords[2], step.coords[3],
            step.coords[4], step.coords[5]);
        break;
      case Step.QUADRATIC:
        double c1x = path.x + (2.0 / 3.0) * (step.coords[0] - path.x);
        double c1y = path.y + (2.0 / 3.0) * (step.coords[1] - path.y);
        double c3x = step.coords[2];
        double c3y = step.coords[3];
        double c2x = c3x + (2.0 / 3.0) * (step.coords[0] - c3x);
        double c2y = c3y + (2.0 / 3.0) * (step.coords[1] - c3y);
        path.cubicTo(c1x, c1y, c2x, c2y, c3x, c3y);
        break;
      case Step.CUBIC:
        path.cubicTo(step.coords[0], step.coords[1], 
            step.coords[2], step.coords[3],
            step.coords[4], step.coords[5]);
        break;
      case Step.CLOSE_PATH:
        path.closePath();
        break;
      }
    }
    return path;
  }


  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Step step : steps) {
      if (first) {
        first = false;
      } else {
        sb.append("; ");
      }
      sb.append(step.toString());
    }
    return sb.toString();
  }
  
  public static class Step {
    public static final int MOVE_TO = 1;
    public static final int LINE_TO = 2;
    public static final int ARC = 3;
    public static final int QUADRATIC = 4;
    public static final int CUBIC = 5;
    public static final int CLOSE_PATH = 6;

    public int kind;
    public double[] coords;
    public Step(int kind, double... coords) {
      super();
      this.kind = kind;
      this.coords = coords;
    }
    
    public static Step moveTo(double x, double y) {
      return new Step(MOVE_TO, new double[] { x, y });
    }

    public static Step lineTo(double x, double y) {
      return new Step(LINE_TO, new double[] { x, y });
    }

    public static Step arc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
      return new Step(ARC, new double[] { x, y, width, height, startAngle, sweepAngle });
    }
    
    public static Step quadraticTo(double x0, double y0, double x1, double y1) {
      return new Step(QUADRATIC, new double[] { x0, y0, x1, y1 });
    }

    public static Step cubicTo(double x0, double y0, double x1, double y1, double x2, double y2) {
      return new Step(CUBIC, new double[] { x0, y0, x1, y1, x2, y2 });
    }

    public static Step closePath() {
      return new Step(CLOSE_PATH, null);
    }
    
    public String toString() {
      switch(kind) {
      case MOVE_TO:
        return String.format("MoveTo(%f, %f)", coords[0], coords[1]);
      case LINE_TO:
        return String.format("LineTo(%f, %f)", coords[0], coords[1]);
      case ARC:
        return String.format("Arc(%f, %f, %f, %f, %f, %f)",
            coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
      case QUADRATIC:
        return String.format("Quadratic(%f, %f, %f, %f)", coords[0], coords[1], coords[2], coords[3]);
      case CUBIC:
        return String.format("Cubic(%f, %f, %f, %f, %f, %f)",
            coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
      case CLOSE_PATH:
        return String.format("Close", coords[0], coords[1]);
      }
      return null;
    }
  }
}
