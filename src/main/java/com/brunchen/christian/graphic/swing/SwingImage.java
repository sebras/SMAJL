package com.brunschen.christian.graphic.swing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.brunschen.christian.graphic.Image;
import com.brunschen.christian.graphic.Surface;

public class SwingImage implements Image {
  
  protected BufferedImage bufferedImage;
  protected SwingSurface swingSurface; 

  public SwingImage(int width, int height) {
    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
  }
  
  @Override
  public int getWidth() {
    return bufferedImage.getWidth();
  }

  @Override
  public int getHeight() {
    return bufferedImage.getHeight();
  }

  @Override
  public Surface makeSurface() {
    Graphics2D g = bufferedImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    return new SwingSurface(g);
  }

  public BufferedImage getBufferedImage() {
    return bufferedImage;
  }
}
