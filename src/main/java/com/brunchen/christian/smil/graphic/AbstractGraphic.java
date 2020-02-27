/*
 * AbstractGraphic
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

public class AbstractGraphic implements Graphic {

  protected GraphicParent parent;
  protected Rectangle frame;
  protected Rectangle bounds;
  protected AffineTransform drawTransform;

  public AbstractGraphic(Size size) {
    super();
    Rectangle bounds = new Rectangle(0, 0, size.getWidth(), size.getHeight());
    setFrame(bounds);
    setBounds(bounds);
  }

  public GraphicParent parent() {
    return parent;
  }

  public void setParent(GraphicParent newParent) {
    parent = newParent;
  }

  public Rectangle frame() {
    return frame;
  }

  public void setFrame(Rectangle newFrame) {
    frame = newFrame;
    resetTransforms();
  }

  public Rectangle bounds() {
    return bounds;
  }

  public void setBounds(Rectangle newBounds) {
    bounds = newBounds;
    resetTransforms();
  }

  public void draw(Surface s) {
  }

  public void repaint(Rectangle rect) {
    if (parent != null) {
      parent.repaint(boundsToFrame(rect.intersection(bounds())));
    }
  }

  public void repaint() {
    repaint(bounds);
  }

  public void scrollRectToVisible(Rectangle rect) {
    if (parent != null) {
      Rectangle boundsRect = rect.intersection(bounds());
      Rectangle frameRect = boundsToFrame(boundsRect);
      parent.scrollRectToVisible(frameRect);
    }
  }

  public Rectangle visibleRect() {
    Rectangle parentRect = null;
    if (parent != null) {
      parentRect = parent.visibleRect();
    }
    if (parentRect != null) {
      return frameToBounds(parentRect);
    }
    return bounds();
  }

  private void resetTransforms() {
    drawTransform = null;
  }

  public AffineTransform getTransform() {
    if (drawTransform == null) {
      drawTransform = AffineTransform.makeTranslate(frame.getX(), frame.getY());
      drawTransform.scale(frame.getWidth() / bounds.getWidth(), frame.getHeight() / bounds.getHeight());
      drawTransform.translate(-bounds.getX(), -bounds.getY());
    }
    return drawTransform;
  }

  private static double transform(double x, double a0, double aw, double b0, double bw) {
    return (x - a0) * (bw / aw) + b0;
  }

  public Point frameToBounds(Point p) {
    double x = transform(p.getX(), frame.getX(), frame.getWidth(), bounds.getX(), bounds.getWidth());
    double y = transform(p.getY(), frame.getY(), frame.getHeight(), bounds.getY(), bounds.getHeight());
    Point q = new Point(x, y);
    /*
     * System.err.format("frame (%.3f + %3f, %3f + %3f) to bounds (%.3f, + %3f, %3f + %3f): (%.3f,
     * %.3f) => (%.3f, %.3f)\n", frame.getX(), frame.getWidth(), frame.getY(), frame.getHeight(),
     * bounds.getX(), bounds.getWidth(), bounds.getY(), bounds.getHeight(), p.getX(), p.getY(),
     * q.getX(), q.getY());
     */
    return q;
  }

  public Point boundsToFrame(Point p) {
    double x = transform(p.getX(), bounds.getX(), bounds.getWidth(), frame.getX(), frame.getWidth());
    double y = transform(p.getY(), bounds.getY(), bounds.getHeight(), frame.getY(), frame.getHeight());
    Point q = new Point(x, y);
    /*
     * System.err.format("bounds (%.3f + %3f, %3f + %3f) to frame (%.3f + %3f + %3f, %3f): (%.3f,
     * %.3f) => (%.3f, %.3f)\n", bounds.getX(), bounds.getWidth(), bounds.getY(),
     * bounds.getHeight(), frame.getX(), frame.getWidth(), frame.getY(), frame.getHeight(),
     * p.getX(), p.getY(), q.getX(), q.getY());
     */
    return q;
  }

  public Rectangle frameToBounds(Rectangle r) {
    double x = transform(r.x, frame.x, frame.width, bounds.x, bounds.width);
    double y = transform(r.y, frame.y, frame.height, bounds.y, bounds.height);
    double width = r.width * bounds.width / frame.width;
    double height = r.height * bounds.height / frame.height;
    Rectangle s = new Rectangle(x, y, width, height);
    /*
     * System.err.format("frame (%.3f + %3f, %3f + %3f) to bounds (%.3f, + %3f, %3f + %3f): (%.3f,
     * %.3f) => (%.3f, %.3f)\n", frame.getX(), frame.getWidth(), frame.getY(), frame.getHeight(),
     * bounds.getX(), bounds.getWidth(), bounds.getY(), bounds.getHeight(), p.getX(), p.getY(),
     * q.getX(), q.getY());
     */
    return s;
  }

  public Rectangle boundsToFrame(Rectangle r) {
    double x = transform(r.x, bounds.x, bounds.width, frame.x, frame.width);
    double y = transform(r.y, bounds.y, bounds.height, frame.y, frame.height);
    double width = r.width * frame.width / bounds.width;
    double height = r.height * frame.height / bounds.height;
    Rectangle s = new Rectangle(x, y, width, height);
    /*
     * System.err.format("bounds (%.3f + %3f, %3f + %3f) to frame (%.3f + %3f + %3f, %3f): (%.3f,
     * %.3f) => (%.3f, %.3f)\n", bounds.getX(), bounds.getWidth(), bounds.getY(),
     * bounds.getHeight(), frame.getX(), frame.getWidth(), frame.getY(), frame.getHeight(),
     * p.getX(), p.getY(), q.getX(), q.getY());
     */
    return s;
  }

  public boolean mouseDown(Point p) {
    return false;
  }

  public boolean mouseDragged(Point p2) {
    return false;
  }

  public boolean mouseUp(Point p) {
    return false;
  }

  public boolean mouseMoved(Point p2) {
    return false;
  }

  public boolean mouseEntered(Point p2) {
    return false;
  }

  public boolean mouseExited(Point p2) {
    return false;
  }

  public void revalidate() {
    if (parent != null) {
      parent.revalidate();
    }
  }
}
