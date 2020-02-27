package com.brunschen.christian.graphic;

public class Point {
  
  protected double x;
  protected double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Point() {
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public String toString() {
    return String.format("Point(x=%f, y=%f)", x, y);
  }

}
