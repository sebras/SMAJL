package com.brunschen.christian.graphic;

public interface Font {

  public Font atSize(double size);
  
  public Rectangle measureString(String s);

  public Rectangle getBoundingBox(String s);
}
