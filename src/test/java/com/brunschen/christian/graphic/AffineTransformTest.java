package com.brunschen.christian.graphic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brunschen.christian.graphic.AffineTransform;
import com.brunschen.christian.graphic.Point;

public class AffineTransformTest {

  @Test
  public void testTranslate() {
    float tx = 3.5f;
    float ty = -6.7f;
    AffineTransform t = AffineTransform.makeTranslate(tx, ty);
    assertEquals(t, AffineTransform.makeIdentity().translate(tx, ty));

    assertTransform(0, 0, t, -tx, -ty);
    assertTransform(tx, ty, t, 0, 0);
    assertTransform(2*tx, 2*ty, t, tx, ty);
    
    assertTransform(10+tx, 45+ty, t, 10, 45);
    assertTransform(-10+tx, -45+ty, t, -10, -45);
  }
  
  @Test
  public void testScale() {
    float sx = 3.5f;
    float sy = -6.7f;
    AffineTransform t = AffineTransform.makeScale(sx, sy);
    assertEquals(t, AffineTransform.makeIdentity().scale(sx, sy));
    
    assertTransform(0, 0, t, 0, 0);
    assertTransform(sx, sy, t, 1, 1);
    assertTransform(sx*sx, sy*sy, t, sx, sy);
    assertTransform(sx*4.6f, sy*-3.5f, t, 4.6f, -3.5f);
  }

  @Test
  public void testRotate45() {
    float theta = (float) Math.PI / 4.0f;
    
    AffineTransform t = AffineTransform.makeRotate(theta);
    assertEquals(t, AffineTransform.makeIdentity().rotate(theta));
    
    assertTransform((float) Math.sqrt(2), (float) Math.sqrt(2), t, 2, 0);
    assertTransform((float) -Math.sqrt(2), (float) Math.sqrt(2), t, 0, 2);
    assertTransform((float) -Math.sqrt(2), (float) -Math.sqrt(2), t, -2, 0);
    assertTransform((float) Math.sqrt(2), (float) -Math.sqrt(2), t, 0, -2);
  }

  @Test
  public void testRotate30() {
    float theta = (float) Math.PI / 6.0f;
    
    AffineTransform t = AffineTransform.makeRotate(theta);
    assertEquals(t, AffineTransform.makeIdentity().rotate(theta));
    
    assertTransform((float) Math.sqrt(3), 1, t, 2, 0);
  }

  @Test
  public void testRotateMinus30() {
    float theta = (float) -Math.PI / 6.0f;
    
    AffineTransform t = AffineTransform.makeRotate(theta);
    assertEquals(t, AffineTransform.makeIdentity().rotate(theta));
    
    assertTransform((float) Math.sqrt(3), -1, t, 2, 0);
  }

  @Test
  public void testScaleThenTranslate() {
    float sx = 2;
    float sy = 3;
    
    float tx = 4;
    float ty = 5;
    
    AffineTransform t = new AffineTransform();
    t.scale(sx, sy);
    t.translate(tx, ty);
    
    assertTransform(sx*tx, sy*ty, t, 0, 0);
    assertTransform(sx*(tx+1), sy*(ty+1), t, 1, 1);
    assertTransform(sx*(tx-4.7f), sy*(ty+3.7f), t, -4.7f, 3.7f);
  }

  @Test
  public void testTranslateThenScale() {
    float sx = 2;
    float sy = 3;
    
    float tx = 4;
    float ty = 5;
    
    AffineTransform t = new AffineTransform();
    t.translate(tx, ty);
    t.scale(sx, sy);
    
    assertTransform(tx, ty, t, 0, 0);
    assertTransform(tx + sx, ty + sy, t, 1, 1);
    assertTransform(tx + sx*-4.7f, ty + sy*3.7f, t, -4.7f, 3.7f);
  }
  
  @Test
  public void testTranslateThenRotate30() {
    float tx = 4;
    float ty = 5;

    float theta = (float) (Math.PI / 6.0);

    AffineTransform t = AffineTransform.makeIdentity();
    t.translate(tx, ty);
    t.rotate(theta);
    
    assertTransform(tx, ty, t, 0, 0);
    assertTransform(tx + (float) Math.sqrt(3), ty + 1, t, 2, 0);
  }

  private void assertTransform(float ex, float ey, AffineTransform t, float x, float y) {
    Point q = t.transform(x, y);
    
    assertEquals(ex, q.x, 0.001);
    assertEquals(ey, q.y, 0.001);
  }
}
