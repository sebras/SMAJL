package com.brunschen.christian.graphic;

public class RadialGradient {

  public double x;
  public double y;
  public double r;
  public Color[] colors;
  public double[] distances;
  
  public Object cached = null;
  
  public RadialGradient(double x, double y, double r, Color[] colors, double[] distances) {
    super();
    this.x = x;
    this.y = y;
    this.r = r;
    this.colors = colors;
    this.distances = distances;
  }

}
