package com.brunschen.christian.graphic.swing;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

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
    return new SwingFont(java.awt.Font.decode(s));
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
