/*
 * Util
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

import java.io.PrintStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class Util {

  public static void fillCircle(Surface g, double x, double y, double r) {
    g.fill(circle(x, y, r));
  }

  public static void fillCircle(Surface g, double x, double y, double r, Color c) {
    g.setColor(c);
    fillCircle(g, x, y, r);
  }

  public static Ellipse circle(double x, double y, double r) {
    return new Ellipse(x - r, y - r, 2 * r, 2 * r);
  }

  public static void printRect(PrintStream ps, Rectangle r) {
    ps.print(printRect(r));
  }

  public static String printRect(Rectangle r) {
    return String.format("(%.1f + %.1f, %.1f + %.1f)", r.getX(), r.getWidth(), r.getY(), r.getHeight());
  }

  public static String toString(AttributedString s) {
    AttributedCharacterIterator it = s.getIterator();
    StringBuilder sb = new StringBuilder();
    it.first();
    while (it.current() != AttributedCharacterIterator.DONE) {
      sb.append(it.current());
      it.next();
    }
    return sb.toString();
  }

//  public static AffineTransform transformForAttributedStringInRect(Surface g, AttributedString s, Rectangle rect) {
//    FontRenderContext frc = g.getFontRenderContext();
//    TextLayout tl = new TextLayout(s.getIterator(), frc);
//    Rectangle2D stringRect = tl.getBounds();
//    double stringWidth = stringRect.getWidth();
//    double stringAscent = tl.getAscent();
//    double stringDescent = tl.getDescent();
//    double stringHeight = stringAscent + stringDescent;
//    // System.err.format("Metrics for '%s': Bounds = %s, ascent = %.2f, descent
//    // = %.2f\n", toString(s), printRect(stringRect), stringAscent,
//    // stringDescent);
//    double yScale = rect.getHeight() / stringHeight;
//    double xScale = Math.min(yScale, rect.getWidth() / stringWidth);
//    AffineTransform at = AffineTransform.makeTranslate(rect.getCenterX(), rect.getMinY());
//    at.scale(xScale, yScale);
//    at.translate(stringRect.getMinX() - stringRect.getCenterX(), stringAscent);
//    return at;
//  }
//
//  public static AffineTransform transformForStringInRect(Surface g, String s, Rectangle rect) {
//    return transformForAttributedStringInRect(g, attributedStringWithFont(s, g.getFont()), rect);
//  }
//
//  public static AttributedString attributedStringWithFont(String text, Font font) {
//    AttributedString attributedString = new AttributedString(text);
//    attributedString.addAttribute(TextAttribute.FAMILY, font.getFamily());
//    attributedString.addAttribute(TextAttribute.SIZE, font.getSize2D());
//    if (font.isBold()) {
//      attributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
//    }
//    if (font.isBold()) {
//      attributedString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
//    }
//    return attributedString;
//  }
//
//  public static AffineTransform subscriptTransform(double descent) {
//    AffineTransform as = AffineTransform.makeTranslate(0, descent);
//    as.scale(4.0 / 5.0, 4.0 / 5.0);
//    return as;
//  }
//
//  public static AttributedString attributedStringWithSubscript(String main, String subscript, Font font) {
//    String s = main + subscript;
//    AttributedString as = attributedStringWithFont(s, font);
//    FontRenderContext frc = new FontRenderContext(null, true, true);
//    TextLayout tl = new TextLayout(as.getIterator(), frc);
//    as.addAttribute(TextAttribute.TRANSFORM, new TransformAttribute(subscriptTransform(tl.getDescent())),
//        main.length(), s.length());
//    return as;
//  }
}
