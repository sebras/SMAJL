package com.brunschen.christian.graphic;

public interface GraphicParent {
  
  public void scrollRectToVisible(Rectangle r);

  public Rectangle visibleRect();

  public void repaint(Rectangle r);

  public void repaint();

  public void revalidate();
}
