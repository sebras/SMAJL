/*
 * PaddingGraphic
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


public class PaddingGraphic extends AbstractGraphic {

  private static Size sizeFromContentAndPadding(Graphic content, double left, double top, double right,
      double bottom)
  {
    Size s = new Size(content.frame().getWidth() + left + right, content.frame().getHeight() + top + bottom);
    return s;
  }

  private AbstractGraphic content;

  private boolean mouseIsDown;

  public PaddingGraphic(AbstractGraphic content, double left, double top, double right, double bottom) {
    super(sizeFromContentAndPadding(content, left, top, right, bottom));
    this.content = content;
    // adjust the content's frame
    content.setFrame(new Rectangle(left, top, content.frame().getWidth(), content.frame.getHeight()));
    content.setParent(this);
  }

  public PaddingGraphic(AbstractGraphic content, double x, double y) {
    this(content, x, y, x, y);
  }

  public PaddingGraphic(AbstractGraphic content, double p) {
    this(content, p, p, p, p);
  }

  public void draw(Surface g) {
    g.save();

    g.transform(content.getTransform());
    content.draw(g);
    
    g.restore();
  }

  public boolean mouseDown(Point p) {
    if (content.bounds.contains(p)) {
      mouseIsDown = true;
      return content.mouseDown(content.frameToBounds(p));
    }
    return false;
  }

  public boolean mouseUp(Point p) {
    if (mouseIsDown) {
      mouseIsDown = false;
      return content.mouseUp(content.frameToBounds(p));
    }
    return false;
  }

  public boolean mouseDragged(Point p) {
    boolean result = false;
    if (mouseIsDown) {
      if (content.frame().contains(p)) {
        result = content.mouseDragged(content.frameToBounds(p));
      } else {
        result = mouseUp(p);
      }
    }
    if (!mouseIsDown) {
      result |= mouseDown(p);
    }
    return result;
  }

}
