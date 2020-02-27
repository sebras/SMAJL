/*
 * ScrollableGraphicComponent
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.brunschen.christian.graphic.Graphic;

public class ScrollableGraphicComponent extends GraphicComponent implements Scrollable {

  static final long serialVersionUID = 0L;

  public ScrollableGraphicComponent(Graphic graphic, FindScale findScale, Spacing xSpacing, Spacing ySpacing,
      Color backgroundColor)
  {
    super(graphic, findScale, xSpacing, ySpacing);
  }

  public ScrollableGraphicComponent(Graphic graphic, Color backgroundColor) {
    super(graphic);
  }

  public void refreshSizes() {
    double xScale = findScale.scale(graphic.frame(), graphic.bounds());
    double yScale = findScale.scale(graphic.frame(), graphic.bounds());
    double width = graphic.bounds().getWidth() * xScale;
    double height = graphic.bounds().getHeight() * yScale;
    setPreferredSize(new Dimension((int) Math.ceil(width), (int) Math.ceil(height)));
    // System.err.format("refreshed %s preferred size = (%f x %f)\n", graphic,
    // getPreferredSize().getWidth(), getPreferredSize().getHeight());
    setMinimumSize(new Dimension((int) Math.ceil(width / 10), (int) Math.ceil(height / 10)));
    setMaximumSize(new Dimension((int) Math.ceil(width * 10), (int) Math.ceil(height * 10)));
  }

  public Dimension getPreferredScrollableViewportSize() {
    return new Dimension((int) Math.min(getWidth(), 5 * getHeight()), (int) Math.min(getHeight(), 5 * getWidth()));
  }

  private static int unitIncrement = 10;

  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return unitIncrement;
  }

  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    if (orientation == SwingConstants.HORIZONTAL) {
      return visibleRect.width - unitIncrement;
    } else {
      return visibleRect.height - unitIncrement;
    }
  }

  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  public boolean getScrollableTracksViewportHeight() {
    return true;
  }

}
