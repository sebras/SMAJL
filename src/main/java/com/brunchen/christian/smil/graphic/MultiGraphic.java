/*
 * MultiGraphic
 * 
 * Copyright (C) 2005  Christian Brunschen
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package com.brunschen.christian.graphic;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MultiGraphic extends AbstractGraphic {

  public static enum Gravity {
    MIN, MID, MAX;
  }

  public static enum Direction {
    X {
      @Override
      public Direction otherDirection() {
        return Y;
      }
    },
    Y {
      @Override
      public Direction otherDirection() {
        return X;
      }
    };
    public abstract Direction otherDirection();
  }

  private double offset = 0.0;
  private Gravity gravity = null;
  private Direction direction = null;
  private double regionWidth = 32.0, regionHeight = 32.0;
  private Graphic mouseDownOn = null;
  private Graphic underMouse = null;

  private Map<Integer, Map<Integer, List<Graphic>>> graphics = new TreeMap<Integer, Map<Integer, List<Graphic>>>();
  private List<Graphic> allGraphics = new LinkedList<Graphic>();
  
  public Color backgroundColor;

  public MultiGraphic(Size size, Direction direction, Gravity gravity) {
    super(size);
    this.direction = direction;
    this.gravity = gravity;
    if (direction != null && gravity != null) {
      double x = bounds.getX();
      double y = bounds.getY();
      double w = bounds.getWidth();
      double h = bounds.getHeight();
      double d = 0.0;
      switch (gravity) {
        case MIN:
          d = 0.0;
          break;
        case MID:
          d = (direction == Direction.X) ? -h / 2 : w / 2;
          break;
        case MAX:
          d = (direction == Direction.X) ? -h : -w;
          break;
      }
      switch (direction) {
        case X:
          y += d;
          break;
        case Y:
          x += d;
          break;
      }
      Rectangle rect = new Rectangle(x, y, w, h);
      setFrame(rect);
      setBounds(rect);
    }
  }

  public MultiGraphic(Size size, Direction direction) {
    this(size, direction, Gravity.MID);
  }

  public MultiGraphic(Size size) {
    this(size, null, null);
  }

  public MultiGraphic() {
    this(new Size(0.0, 0.0), null, null);
  }

  public MultiGraphic(Direction direction, Gravity gravity) {
    this(new Size(0.0, 0.0), direction, gravity);
  }

  public MultiGraphic(Direction direction) {
    this(direction, Gravity.MID);
  }

  public static MultiGraphic newRowGraphic(Size size, Gravity gravity) {
    return new MultiGraphic(size, Direction.X, gravity);
  }

  public static MultiGraphic newRowGraphic(Size size) {
    return new MultiGraphic(size, Direction.X);
  }

  public static MultiGraphic newRowGraphic(Gravity gravity) {
    return new MultiGraphic(Direction.X, gravity);
  }

  public static MultiGraphic newRowGraphic() {
    return new MultiGraphic(Direction.X);
  }

  public static MultiGraphic newColumnGraphic(Size size, Gravity gravity) {
    return new MultiGraphic(size, Direction.Y, gravity);
  }

  public static MultiGraphic newColumnGraphic(Size size) {
    return new MultiGraphic(size, Direction.Y);
  }

  public static MultiGraphic newColumnGraphic(Gravity gravity) {
    return new MultiGraphic(Direction.Y, gravity);
  }

  public static MultiGraphic newColumnGraphic() {
    return new MultiGraphic(Direction.Y);
  }

  public static int indexForCoordinate(double regionSize, double coord) {
    return (int) Math.floor(coord / regionSize);
  }

  public int rowForY(double y) {
    return indexForCoordinate(regionHeight, y);
  }

  public int columnForX(double x) {
    return indexForCoordinate(regionWidth, x);
  }

  public void draw(Surface g) {
    if (backgroundColor != null) {
      g.setColor(backgroundColor);
      g.fill(bounds);
    }

    Collection<Graphic> repaintThese = allGraphics;

    // System.err.format("Repainting with frame (%.2f + %.2f, %.2f + %.2f), %d
    // matching children\n", frame.getX(), frame.getWidth(), frame.getY(),
    // frame.getHeight(), repaintThese.size());
    // Graphics2D g2 = (Graphics2D)g.create();
    // g2.setColor(Color.LIGHT_GRAY);
    for (Graphic graphic : repaintThese) {
      if (!g.skip(graphic.frame())) {
        g.save();

        g.clip(graphic.frame());
        AffineTransform gt = graphic.getTransform();
        g.transform(gt);
        graphic.draw(g);

        g.restore();
      }
    }
    // g2.dispose();
  }

  protected void addGraphicToGraphics(Graphic graphic) {
    int c0 = columnForX(graphic.frame().getMinX());
    int c1 = columnForX(graphic.frame().getMaxX());
    int r0 = rowForY(graphic.frame().getMinY());
    int r1 = rowForY(graphic.frame().getMaxY());

    for (int r = r0; r <= r1; r++) {
      Map<Integer, List<Graphic>> row = graphics.get(r);
      if (row == null) {
        graphics.put(r, row = new TreeMap<Integer, List<Graphic>>());
      }
      for (int c = c0; c <= c1; c++) {
        List<Graphic> list = row.get(c);
        if (list == null) {
          row.put(c, list = new LinkedList<Graphic>());
        }
        list.add(graphic);
      }
    }
    
    allGraphics.add(graphic);

    setBounds(bounds().union(graphic.frame()));
    // System.err.format("new bounds = %s\n", bounds);
  }

  public void add(Graphic graphic, Point origin) {
    /*
     * System.err.format("%sAdding graphic '%s' (%.2f + %.2f, %.2f + %.2f) at (%.2f, %.2f)\n",
     * prefix, graphic, graphic.frame().getX(), graphic.frame().getWidth(), graphic.frame().getY(),
     * graphic.frame().getHeight(), origin.getX(), origin.getY());
     */
    graphic.setFrame(new Rectangle(origin.getX(), origin.getY(), graphic.bounds().getWidth(), graphic.bounds()
        .getHeight()));
    graphic.setParent(this);
    addGraphicToGraphics(graphic);
  }

  private static double extentInDirection(Rectangle rect, Direction direction) {
    switch (direction) {
      case X:
        return rect.getWidth();
      case Y:
        return rect.getHeight();
      default:
        return Double.NaN;
    }
  }

  private static double maxCoordInDirection(Rectangle rect, Direction direction) {
    switch (direction) {
      case X:
        return rect.getMaxX();
      case Y:
        return rect.getMaxY();
      default:
        return Double.NaN;
    }
  }

  private static Point makePoint(Direction direction, double coord, double otherCoord) {
    switch (direction) {
      case X:
        return new Point(coord, otherCoord);
      case Y:
        return new Point(otherCoord, coord);
      default:
        return null;
    }
  }

  public void add(Graphic graphic) {
    if (gravity == null || direction == null) {
      throw new RuntimeException("no direction or gravity");
    }
    double otherOffset = 0.0;
    switch (gravity) {
      case MIN:
        otherOffset = 0.0;
        break;
      case MID:
        otherOffset = -extentInDirection(graphic.frame(), direction.otherDirection()) / 2;
        break;
      case MAX:
        otherOffset = -extentInDirection(graphic.frame(), direction.otherDirection());
        break;
    }
    add(graphic, makePoint(direction, offset, otherOffset));
    advance(extentInDirection(graphic.frame(), direction));
  }

  public void add(Graphic graphic, Double offset) {
    if (offset != null) {
      this.offset = offset;
    }
    add(graphic);
  }

  public void advance(Double d) {
    if (d != null) {
      offset += d;
      adjustBounds();
    }
  }

  public void moveTo(Double d) {
    if (d != null) {
      offset = d;
      adjustBounds();
    }
  }

  private void adjustBounds() {
    if (offset > maxCoordInDirection(bounds, direction)) {
      switch (direction) {
        case X:
          bounds.set(bounds.getX(), bounds.getY(), bounds.getWidth() + (offset - bounds.getMaxX()), bounds
              .getHeight());
          break;
        case Y:
          bounds.set(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()
              + (offset - bounds.getMaxY()));
          break;
        default:
          break;
      }
    }
    /*
     * System.err.format("%snew bounds: (%.2f + %.2f, %.2f + %.2f)\n", prefix, bounds.getX(),
     * bounds.getWidth(), bounds.getY(), bounds.getHeight());
     */
  }

  public void pad(double left, double right, double top, double bottom) {
    double x = bounds.getX();
    double y = bounds.getY();
    double w = bounds.getWidth();
    double h = bounds.getHeight();
    bounds.set(x - left, y - top, w + left + right, h + top + bottom);
  }

  public Graphic graphicAtPoint(Point p) {
    int r = rowForY(p.y);
    Map<Integer, List<Graphic>> row = graphics.get(r);
    if (row == null) {
      return null;
    }
    int c = columnForX(p.x);
    List<Graphic> list = row.get(c);
    if (list == null) {
      return null;
    }
    for (Graphic g : list) {
      if (g.frame().contains(p)) {
        return g;
      }
    }
    return null;
  }

  public Collection<Graphic> graphicsInRectangle(Rectangle rect) {
    int r0 = rowForY(rect.getMinY());
    int r1 = rowForY(rect.getMaxY());
    int c0 = columnForX(rect.getMinX());
    int c1 = columnForX(rect.getMaxX());
    Set<Graphic> result = new HashSet<Graphic>(100);
    for (int r = r0; r <= r1; r++) {
      Map<Integer, List<Graphic>> row = graphics.get(r);
      if (row == null) {
        continue;
      }
      for (int c = c0; c <= c1; c++) {
        List<Graphic> list = row.get(c);
        if (list == null) {
          continue;
        }
        for (Graphic g : list) {
          if (rect.intersects(g.frame())) {
            result.add(g);
          }
        }
      }
    }
    return result;
  }

  public boolean mouseDown(Point p) {
    Graphic child = graphicAtPoint(p);
    // System.err.format("MultiGraphic: mouse down at %s, hits %s\n", p, child);
    if (child != null) {
      Point q = child.frameToBounds(p);
      // System.err.format("MultiGraphic: forwarding mouse down at child coords
      // %s\n", q);
      mouseDownOn = child;
      return mouseDownOn.mouseDown(q);
    }
    return false;
  }

  public boolean mouseUp(Point p) {
    boolean result = false;
    if (mouseDownOn != null) {
      result = mouseDownOn.mouseUp(mouseDownOn.frameToBounds(p));
      mouseDownOn = null;
    }
    return result;
  }

  public boolean mouseDragged(Point p) {
    boolean result = false;
    if (mouseDownOn != null) {
      if (mouseDownOn.frame().contains(p)) {
        result = mouseDownOn.mouseDragged(mouseDownOn.frameToBounds(p));
      } else {
        result = mouseUp(p);
      }
    }
    if (mouseDownOn == null) {
      result |= mouseDown(p);
    }
    return result;
  }

  public boolean mouseMoved(Point p) {
    Graphic previousUnderMouse = underMouse;
    underMouse = graphicAtPoint(p);
    if (underMouse != null) {
      if (previousUnderMouse != underMouse) {
        if (previousUnderMouse != null)
          previousUnderMouse.mouseExited(previousUnderMouse.frameToBounds(p));
        return underMouse.mouseEntered(underMouse.frameToBounds(p));
      } else {
        return underMouse.mouseMoved(underMouse.frameToBounds(p));
      }
    } else if (previousUnderMouse != null) {
      return previousUnderMouse.mouseExited(previousUnderMouse.frameToBounds(p));
    }
    return false;
  }

  public boolean mouseExited(Point p) {
    boolean result = false;
    if (underMouse != null)
      result = underMouse.mouseExited(underMouse.frameToBounds(p));
    underMouse = null;
    return result;
  }

  public boolean mouseEntered(Point p) {
    return mouseMoved(p);
  }

}
