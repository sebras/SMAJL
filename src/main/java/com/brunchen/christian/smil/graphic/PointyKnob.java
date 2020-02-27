/*
 * PointyKnob
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

import java.util.ArrayList;
import java.util.Collection;

public class PointyKnob extends AbstractGraphic {

  private double minAngle;

  private double maxAngle;

  private double position;

  private Collection<ValueUpdatedListener<Double>> valueUpdatedListeners;

  private static Path SHAPE = new Path();
  static {
    SHAPE.moveTo(-5, 20);
    SHAPE.lineTo(-10, 10);
    SHAPE.lineTo(-10, -10);
    SHAPE.lineTo(0, -50);
    SHAPE.lineTo(10, -10);
    SHAPE.lineTo(10, 10);
    SHAPE.lineTo(5, 20);
    SHAPE.closePath();
  }

  private static StrokeStyle strokeStyle = new StrokeStyle(0.4);
  private Path path;

  // private static BasicStroke bgStroke = new BasicStroke(0.5f,
  // BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

  public PointyKnob(Size size, double minAngle, double maxAngle, double position)
  {
    super(size);
    this.minAngle = minAngle;
    this.maxAngle = maxAngle;
    setPosition(position);
    valueUpdatedListeners = new ArrayList<ValueUpdatedListener<Double>>();
    path = SHAPE.transformed(AffineTransform.makeScale(size.getWidth() / 110.0, size.getHeight() / 110.0));
  }

  public void setPosition(double newPosition) {
    position = Math.min(Math.max(newPosition, 0.0), 1.0);
  }

  public double position() {
    return position;
  }

  public double angle() {
    return minAngle + position * (maxAngle - minAngle);
  }

  @Override
  public void draw(Surface g) {
    g.save();
    
    g.translate(bounds().getCenterX(), bounds().getCenterY());
    /*
     * g.setStroke(bgStroke); g.setColor(Color.LIGHT_GRAY); g.draw(new Arc2D.Double(-4, -4, 8, 8,
     * minAngle * 180.0 / Math.PI + 90, (maxAngle - minAngle) * 180.0 / Math.PI, Arc2D.OPEN));
     */
    g.rotate(angle());
    g.setColor(Color.BLACK);
    g.fill(path);
    g.setStrokeStyle(strokeStyle);
    g.stroke(path);

    g.restore();
  }

  @Override
  public boolean mouseDown(Point p) {
    double angle = Math.min(maxAngle, Math.max(minAngle, Math.atan2(p.getX() - bounds().getCenterX(),
        -(p.getY() - bounds().getCenterY()))));
    double newPosition = (angle - minAngle) / (maxAngle - minAngle);
    for (ValueUpdatedListener<Double> listener : valueUpdatedListeners) {
      if (listener != null)
        listener.valueUpdated(position, newPosition);
    }
    setPosition(newPosition);
    repaint();
    return true;
  }

  @Override
  public boolean mouseUp(Point p) {
    // System.err.format("Button: mouseUp at %s\n", p);
    repaint();
    return true;
  }

  @Override
  public boolean mouseDragged(Point p) {
    return mouseDown(p);
  }

  @Override
  public boolean mouseExited(Point p) {
    return mouseUp(p);
  }

  public void addValueUpdatedListener(ValueUpdatedListener<Double> listener) {
    valueUpdatedListeners.add(listener);
  }

  public void removeValueUpdatedListener(ValueUpdatedListener<Double> listener) {
    valueUpdatedListeners.remove(listener);
  }
}
