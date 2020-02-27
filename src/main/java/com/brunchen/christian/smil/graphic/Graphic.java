package com.brunschen.christian.graphic;

public interface Graphic extends GraphicParent {

  public GraphicParent parent();

  public void setParent(GraphicParent newParent);

  public Rectangle frame();

  public void setFrame(Rectangle newFrame);

  public Rectangle bounds();

  public void setBounds(Rectangle newBounds);

  public void draw(Surface s);

  public AffineTransform getTransform();

  public Point frameToBounds(Point p);

  public Point boundsToFrame(Point p);

  public boolean mouseDown(Point p);

  public boolean mouseDragged(Point p);

  public boolean mouseUp(Point p);

  public boolean mouseMoved(Point p);

  public boolean mouseEntered(Point p);

  public boolean mouseExited(Point p);
}