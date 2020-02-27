/*
 * GraphicComponent
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

package com.brunschen.christian.graphic.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import com.brunschen.christian.graphic.AffineTransform;
import com.brunschen.christian.graphic.Color;
import com.brunschen.christian.graphic.Graphic;
import com.brunschen.christian.graphic.GraphicParent;
import com.brunschen.christian.graphic.Point;
import com.brunschen.christian.graphic.Rectangle;
import com.brunschen.christian.graphic.Size;
import com.brunschen.christian.graphic.ValueUpdatedListener;

public class GraphicComponent extends JComponent implements MouseListener, MouseMotionListener {

  static final long serialVersionUID = 0L;

  public static enum Spacing {
    ADJUST {
      public double scale(double wanted, double available) {
        if (wanted > 0) {
          return available / wanted;
        } else {
          return super.scale(wanted, available);
        }
      }
    },
    MIN {
    },
    MID {
      public double offset(double wanted, double available) {
        return (available - wanted) / 2.0;
      }
    },
    MAX {
      public double offset(double wanted, double available) {
        return (available - wanted);
      }
    };
    public double offset(double wanted, double available) {
      return 0.0;
    }

    public double scale(double wanted, double available) {
      return 1.0;
    }
  }

  public static enum FindScale {
    X {
      public double scale(Rectangle frame, Rectangle bounds) {
        return frame.getWidth() / bounds.getWidth();
      }
    },
    Y {
      public double scale(Rectangle frame, Rectangle bounds) {
        return frame.getHeight() / bounds.getHeight();
      }
    },
    MIN {
      public double scale(Rectangle frame, Rectangle bounds) {
        return Math.min(frame.getWidth() / bounds.getWidth(), frame.getHeight() / bounds.getHeight());
      }
    },
    MAX {
      public double scale(Rectangle frame, Rectangle bounds) {
        return Math.max(frame.getWidth() / bounds.getWidth(), frame.getHeight() / bounds.getHeight());
      }
    },
    EACH {
      public double xScale(Rectangle frame, Rectangle bounds) {
        return frame.getWidth() / bounds.getWidth();
      }

      public double yScale(Rectangle frame, Rectangle bounds) {
        return frame.getHeight() / bounds.getHeight();
      }
    };
    public double scale(Rectangle frame, Rectangle bounds) {
      return Double.NaN;
    }

    public double xScale(Rectangle frame, Rectangle bounds) {
      return scale(frame, bounds);
    }

    public double yScale(Rectangle frame, Rectangle bounds) {
      return scale(frame, bounds);
    }
  }

  protected GraphicWrapper wrapper;
  protected Graphic graphic;
  protected boolean mouseInGraphic = false;
  protected Spacing xSpacing = Spacing.MID;
  protected Spacing ySpacing = Spacing.MID;
  protected FindScale findScale = FindScale.MIN;
  protected Color backgroundColor = null;
  
  protected List<ValueUpdatedListener<Size>> sizeChangedListeners = 
    new LinkedList<ValueUpdatedListener<Size>>();

  public GraphicComponent(Graphic graphic, FindScale findScale,
      Spacing xSpacing, Spacing ySpacing,
      Color backgroundColor)
  {
    super();
    this.graphic = graphic;
    this.wrapper = new GraphicWrapper(graphic);
    addMouseListener(this);
    addMouseMotionListener(this);
    this.findScale = findScale;
    this.xSpacing = xSpacing;
    this.ySpacing = ySpacing;
    this.backgroundColor = backgroundColor;
    refreshSizes();
  }

  public GraphicComponent(Graphic graphic, FindScale findScale, Spacing xSpacing, Spacing ySpacing) {
    this(graphic, findScale, xSpacing, ySpacing, null);
  }

  public GraphicComponent(Graphic graphic, Color backgroundColor) {
    this(graphic, FindScale.MIN, Spacing.MID, Spacing.MID, backgroundColor);
  }

  public GraphicComponent(Graphic graphic) {
    this(graphic, null);
  }

  public void refreshSizes() {
    setPreferredSize(new Dimension((int) Math.ceil(graphic.bounds().getWidth()), (int) Math.ceil(graphic.bounds()
        .getHeight())));
    // System.err.format("refreshed %s preferred size = (%f x %f)\n", graphic,
    // getPreferredSize().getWidth(), getPreferredSize().getHeight());
    setMinimumSize(new Dimension((int) Math.ceil(graphic.bounds().getWidth() / 10), (int) Math.ceil(graphic.bounds()
        .getHeight() / 10)));
    setMaximumSize(new Dimension((int) Math.ceil(graphic.bounds().getWidth() * 10), (int) Math.ceil(graphic.bounds()
        .getHeight() * 10)));
  }

  public void paintComponent(Graphics graphics) {
    // System.err.format("Painting GraphicComponent\n");
    Graphics2D g = (Graphics2D) graphics;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    // System.err.format("Drawing graphic %s\n", graphic);
    // System.err.format("transform = %s\n", at);
    java.awt.geom.AffineTransform transform = g.getTransform();
    AffineTransform at = graphic.getTransform();
    java.awt.geom.AffineTransform gTransform = new java.awt.geom.AffineTransform(at.xx, at.yx, at.yx, at.yy, at.tx, at.ty);
    g.transform(gTransform);
    if (backgroundColor != null) {
      Graphics2D g1 = (Graphics2D) g.create();
      g1.setColor(new java.awt.Color(backgroundColor.r, backgroundColor.g, backgroundColor.b));
      Rectangle b = graphic.bounds();
      g1.fill(new Rectangle2D.Double(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight()));
    }
    SwingSurface s = new SwingSurface(g); 
    graphic.draw(s);
    g.setTransform(transform);
  }

  public void adjustGraphicFrame() {
    // System.err.format("Adjusting bounds and frame for %s\n", graphic);
    Rectangle bounds = new Rectangle(0.0, 0.0, getWidth(), getHeight());
    double xScale = findScale.xScale(bounds, graphic.bounds());
    double yScale = findScale.yScale(bounds, graphic.bounds());
    double width = (Double.isInfinite(xScale) ? 1.0 : xScale) * graphic.bounds().getWidth();
    double height = (Double.isInfinite(yScale) ? 1.0 : yScale) * graphic.bounds().getHeight();
    double xRescale = xSpacing.scale(width, getWidth());
    double yRescale = ySpacing.scale(height, getHeight());
    if (xRescale != 1.0 || yRescale != 1.0) {
      width *= xRescale;
      height *= yRescale;
    }
    double xOffset = xSpacing.offset(width, getWidth());
    double yOffset = ySpacing.offset(height, getHeight());
    Rectangle newFrame = new Rectangle(xOffset, yOffset, width, height);
    // System.err.format("updating %s's frame to %s\n", graphic,
    // Util.printRect(newFrame));
    graphic.setFrame(newFrame);
    // System.err.format("%s's new transform = %s\n", graphic,
    // graphic.transform());
    refreshSizes();
  }

  public void setBounds(int x, int y, int width, int height) {
    // System.err.format("setBounds(%d, %d, %d, %d)\n", x, y, width, height);
    boolean sizeChanged = ((width != getWidth()) || (height != getHeight()));
    Size oldSize = new Size(getWidth(), getHeight());
    super.setBounds(x, y, width, height);
    if (sizeChanged) {
      adjustGraphicFrame();
      for (ValueUpdatedListener<Size> listener : sizeChangedListeners) {
        listener.valueUpdated(oldSize, new Size(width, height));
      }
    }
  }

  public void setBounds(Rectangle newBounds) {
    int x0 = (int) Math.floor(newBounds.getMinX());
    int x1 = (int) Math.ceil(newBounds.getMaxX());    
    int y0 = (int) Math.floor(newBounds.getMinY());
    int y1 = (int) Math.ceil(newBounds.getMaxY());
    setBounds(x0, y0, x1 - x0, y1 - y0);
  }

  public void repaint(int x, int y, int width, int height) {
    // System.err.format("Repainting (%d + %d, %d + %d)\n", x, width, y, height);
    super.repaint(x, y, width, height);
  }

  private boolean pointInGraphic(Point p) {
    return graphic.frame().contains(p);
  }

  private Point point(MouseEvent e) {
    return new Point(e.getX(), e.getY());
  }

  public void mousePressed(MouseEvent e) {
    if (mouseInGraphic) {
      graphic.mouseDown(graphic.frameToBounds(point(e)));
    }
  }

  public void mouseReleased(MouseEvent e) {
    if (mouseInGraphic) {
      graphic.mouseUp(graphic.frameToBounds(point(e)));
    }
  }

  public void mouseClicked(MouseEvent e) {
    // no-op, handled through mousePressed and mouseReleased
  }

  public void mouseEntered(MouseEvent e) {
    Point p = point(e);
    if ((mouseInGraphic = pointInGraphic(p))) {
      graphic.mouseEntered(graphic.frameToBounds(p));
    }
  }

  public void mouseExited(MouseEvent e) {
    if (mouseInGraphic) {
      graphic.mouseExited(graphic.frameToBounds(point(e)));
    }
    mouseInGraphic = false;
  }

  public void mouseDragged(MouseEvent e) {
    Point p = point(e);
    Point q = graphic.frameToBounds(p);
    boolean newMouseInGraphic = pointInGraphic(p);
    if (mouseInGraphic && !newMouseInGraphic) { // exiting
      graphic.mouseUp(q);
      graphic.mouseExited(q);
    } else if (!mouseInGraphic && newMouseInGraphic) { // entering
      graphic.mouseEntered(q);
      graphic.mouseDown(q);
    } else if (mouseInGraphic && newMouseInGraphic) { // within graphic
      graphic.mouseDragged(q);
    } else {
      // mouse dragged outside graphic, nothing to forward
    }
    mouseInGraphic = newMouseInGraphic;
  }

  public void mouseMoved(MouseEvent e) {
    Point p = point(e);
    Point q = graphic.frameToBounds(p);
    boolean newMouseInGraphic = pointInGraphic(p);
    if (mouseInGraphic && !newMouseInGraphic) { // exiting
      graphic.mouseExited(q);
    } else if (!mouseInGraphic && newMouseInGraphic) { // entering
      graphic.mouseEntered(q);
    } else if (mouseInGraphic && newMouseInGraphic) { // within graphic
      graphic.mouseMoved(q);
    } else {
      // mouse dragged outside graphic, nothing to forward
    }
    mouseInGraphic = newMouseInGraphic;
  }

  public void revalidate() {
    refreshSizes();
    super.revalidate();
  }
  
  public void addSizeChangedListener(ValueUpdatedListener<Size> listener) {
    sizeChangedListeners.add(listener);
  }
  
  public void removeSizeChangedListener(ValueUpdatedListener<Size> listener) {
    sizeChangedListeners.remove(listener);
  }

  public class GraphicWrapper implements GraphicParent {

    protected Graphic graphic;

    public GraphicWrapper(Graphic graphic) {
      super();
      this.graphic = graphic;
      graphic.setParent(this);
    }

    public void repaint(Rectangle r) {
      int x0 = (int) Math.floor(r.getMinX());
      int x1 = (int) Math.ceil(r.getMaxX());
      int y0 = (int) Math.floor(r.getMinY());
      int y1 = (int) Math.ceil(r.getMaxY());
      GraphicComponent.this.repaint(x0, y0, x1 - x0, y1 - y0);
    }

    public void repaint() {
      Rectangle frame = graphic.frame();
      int x0 = (int) Math.floor(frame.getMinX());
      int x1 = (int) Math.ceil(frame.getMaxX());
      int y0 = (int) Math.floor(frame.getMinY());
      int y1 = (int) Math.ceil(frame.getMaxY());
      GraphicComponent.this.repaint(x0, y0, x1 - x0, y1 - y0);
    }

    public void scrollRectToVisible(Rectangle r) {
      int x0 = (int) Math.floor(r.getMinX());
      int x1 = (int) Math.ceil(r.getMaxX());
      int y0 = (int) Math.floor(r.getMinY());
      int y1 = (int) Math.ceil(r.getMaxY());
      GraphicComponent.this.scrollRectToVisible(new java.awt.Rectangle(x0, y0, x1 - x0, y1 - y0));
    }

    public Rectangle visibleRect() {
      java.awt.Rectangle cvr = new java.awt.Rectangle();
      GraphicComponent.this.computeVisibleRect(cvr);
      return new Rectangle(cvr.getMinX(), cvr.getMinY(), cvr.getWidth(), cvr.getHeight());
    }

    public void revalidate() {
      GraphicComponent.this.revalidate();
    }
  }
}

