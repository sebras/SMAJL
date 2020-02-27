package com.brunschen.christian.graphic;

public class Color {
  
  public static final Color BLACK = new Color(0, 0, 0);
  public static final Color DARK_GRAY = new Color(1f/3f, 1f/3f, 1f/3f);
  public static final Color LIGHT_GRAY = new Color(2f/3f, 2f/3f, 2f/3f);
  public static final Color WHITE = new Color(1, 1, 1);
  public static final Color RED = new Color(1, 0, 0);
  public static final Color GREEN = new Color(0, 1, 0);
  public static final Color BLUE = new Color(0, 0, 1);
  public static final Color YELLOW = new Color(1, 1, 0);
  public static final Color CREAM = new Color(1, 1, 204f/255f);
  
  public float r;
  public float g;
  public float b;
  public float a;

  public Color(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  public Color(float r, float g, float b) {
    this(r, g, b, 1f);
  }

  public static Color hex(int r, int g, int b, int a) {
    return new Color(r/255f, g/255f, b/255f, a/255f);
  }

  public static Color hex(int r, int g, int b) {
    return new Color(r/255f, g/255f, b/255f);
  }
}
