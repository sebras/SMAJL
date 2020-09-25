package com.brunschen.christian.graphic.swing;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;

import com.brunschen.christian.graphic.Font;
import com.brunschen.christian.graphic.Rectangle;

public class SwingFont implements Font {
  public java.awt.Font awtFont;

  public SwingFont(java.awt.Font awtFont) {
    this.awtFont = awtFont;
  }

  public java.awt.Font awtFont() {
    return awtFont;
  }

  public static SwingFont decode(String s) {
    java.awt.Font font;

    System.setProperty("awt.useSystemAAFontSettings","on");
    System.setProperty("swing.aatext", "true");

    int idx = s.lastIndexOf(' ');
    if (idx == -1 || idx + 1 > s.length())
      return null;

    String name = s.substring(0, idx);
    Float size = Float.parseFloat(s.substring(idx + 1));

    String resource = "Fonts/" + name + ".ttf";
    InputStream is = SwingFont.class.getResourceAsStream(resource);

    if (is != null) {
      try {
        font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is).deriveFont(size);
      } catch (Exception e) {
        return null;
      }
    } else {
      font = java.awt.Font.decode(s);
    }

    return new SwingFont(font);
  }

  @Override
  public SwingFont atSize(double size) {
    return new SwingFont(awtFont.deriveFont((float) size));
  }

  @Override
  public Rectangle measureString(String s) {
    FontRenderContext frc = new FontRenderContext(null, true, true);
    Rectangle2D rect = awtFont.getStringBounds(s, frc);
    return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }
  
  @Override
  public Rectangle getBoundingBox(String s) {
    FontRenderContext frc = new FontRenderContext(null, true, true);
    GlyphVector glyphVector = awtFont.createGlyphVector(frc, s);
    Shape outline = glyphVector.getOutline();
    Rectangle2D rect = outline.getBounds2D();
    return new Rectangle(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
  }

}
