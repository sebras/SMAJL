package com.brunschen.christian.graphic;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathTest {

  @Test
  public void testFlattenArc() {
    Path path = new Path();
    path.moveTo(30, 5);
    path.addArc(20, 5, 20, 10, 0, 360);
    
    Path flat = path.flattened(1);
    System.out.format("Path = %s; Flattened = %s%n", path, flat);
  }

  @Test
  public void testFlattenQuadratic() {
    Path path = new Path();
    path.moveTo(0, 0);
    path.quadraticTo(0, 10, 20, 0);
    
    Path flat = path.flattened(1);
    System.out.format("Path = %s; Flattened = %s%n", path, flat);
    
    path = path.withCubics();
    flat = path.flattened(1);
    System.out.format("Cubic Path = %s; Flattened = %s%n", path, flat);
  }

  @Test
  public void testFlattenCubic() {
    Path path = new Path();
    path.moveTo(0, 0);
    path.cubicTo(0, 10, 0, 10, 20, 0);
    
    Path flat = path.flattened(1);
    System.out.format("Path = %s; Flattened = %s%n", path, flat);
  }

  @Test
  public void testCubicForArc() {
    Path path = new Path();
    path.moveTo(1, 0);
    path.addArc(-1, -1, 2, 2, 0, 90);
    
    Path flat = path.withCubics();
    System.out.format("Path = %s; With Cubics = %s%n", path, flat);
  }
}
