/*
 * TypewriterArea
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

package com.brunschen.christian.smil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Christian Brunschen
 */
public class TypewriterArea extends JTextArea {

  public static final long serialVersionUID = 0L;

  public TypewriterArea() {
    super();
  }

  public TypewriterArea(String text) {
    super(text);
  }

  public TypewriterArea(int rows, int columns) {
    super(rows, columns);
  }

  public TypewriterArea(String text, int rows, int columns) {
    super(text, rows, columns);
  }

  public TypewriterArea(Document doc) {
    super(doc);
  }

  public TypewriterArea(Document doc, String text, int rows, int columns) {
    super(doc, text, rows, columns);
  }

  @Override
  public void paintComponent(Graphics g0) {
    super.paintComponent(g0);
    Graphics2D g = (Graphics2D) g0.create();
    try {
      Rectangle2D r = modelToView(getDocument().getLength());
      double h = r.getHeight();
      float h2 = (float) (h / 2);
      float w = (float) (3 * h / 8);
      g.translate(r.getCenterX(), r.getCenterY());
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      GeneralPath gp = new GeneralPath();
      gp.moveTo(0.0f, 0.0f);
      gp.lineTo(w, h2);
      gp.lineTo(-w, h2);
      gp.closePath();
      g.setColor(Color.DARK_GRAY);
      g.fill(gp);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
