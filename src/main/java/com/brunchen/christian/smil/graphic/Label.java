/*
 * Label
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


public class Label extends AbstractGraphic {

  private static final double SUBSCRIPT_SCALE = 0.8;
  
  protected Color color;
  protected Font font;
  protected String title;
  protected String subscript;
  protected AffineTransform titleTransform;
  protected AffineTransform subscriptTransform;

  public Label(Size size, Color color, Font font, String title, String subscript) {
    super(size);
    this.font = font;
    this.color = color;
    this.title = title;
    this.subscript = subscript;
  }

  private void calculateTransforms(Surface g) {
    Rectangle titleRect = g.measureString(title);
    double wTitle = titleRect.getWidth();
    double wSuffix = subscript != null ? SUBSCRIPT_SCALE * g.measureString(subscript).getWidth() : 0;
    double w = wTitle + wSuffix;
    double ascent = -titleRect.getMinY();
    double descent = titleRect.getMaxY();
    
    double yScale = bounds().getHeight() / titleRect.getHeight();
    double xScale = Math.min(yScale, bounds().getWidth() / w);
    titleTransform = AffineTransform.makeTranslate(bounds().getCenterX(), 0);
    titleTransform.scale(xScale, yScale);
    titleTransform.translate(-w / 2, ascent);

    if (subscript != null) {
      subscriptTransform = new AffineTransform();
      subscriptTransform.translate(wTitle, descent);
      subscriptTransform.scale(SUBSCRIPT_SCALE, SUBSCRIPT_SCALE);
    } else {
      subscriptTransform = new AffineTransform();
    }
  }

  public Label(Size size, Color color, Font font, String text) {
    this(size, color, font, text, null);
  }

  public void draw(Surface g) {
    g.setFont(font);
    g.setColor(color);
    if (titleTransform == null) {
      calculateTransforms(g);
    }
    g.transform(titleTransform);
    g.drawString(title, 0, 0);
    if (subscript != null) {
      g.transform(subscriptTransform);
      g.drawString(subscript, 0, 0);
    }
  }   

}
