/*
 * Light
 * 
 * Copyright (C) 2005  Christian Brunschen
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package com.brunschen.christian.graphic;


public class Light extends AbstractGraphic {

  private static HSLColorSpace cs = new HSLColorSpace();
  private BooleanValue booleanValue;
  private Color color;
  private RadialGradient onGradient;
  private RadialGradient offGradient;
  private Ellipse shape;

  public Light(Size size, String text, BooleanValue booleanValue, Color color) {
    super(size);
    this.booleanValue = booleanValue;
    this.color = color;
    double cx = bounds.width / 2.0;
    double cy = bounds.height / 2.0;
    double r = Math.min(bounds.width, bounds.height) / 2.0;
    this.onGradient = makeGradient(true, cx, cy, r, 21);
    this.offGradient = makeGradient(false, cx, cy, r, 21);
    this.shape = new Ellipse(bounds());
  }

  public void clamp(float[] floats) {
    for (int i = 0; i < floats.length; i++) {
      if (floats[i] > 1.0f)
        floats[i] = 1.0f;
      else if (floats[i] < 0.0f)
        floats[i] = 0.0f;
    }
  }

  public Color color(float[] hsl, float lightness) {
    float[] tmp = new float[3];
    tmp[0] = hsl[0];
    tmp[1] = hsl[1];
    tmp[2] = hsl[2];
    if (lightness < 0.5) {
      tmp[2] *= 2 * lightness;
    } else {
      tmp[2] += (2 * (lightness - 0.5)) * (1.0 - tmp[2]);
    }
    clamp(tmp);
    float[] rgb = cs.toRGB(tmp);
    clamp(rgb);
    return new Color(rgb[0], rgb[1], rgb[2], 1.0f);
  }

  private RadialGradient makeGradient(boolean on, double x, double y, double rMax, int steps) {
    float[] hsl = cs.fromRGB(new float[] { color.r, color.g, color.b });
    float minLightness = 0.05f;
    float maxLightness = on ? 0.9f : 0.15f;
    
    Color[] colors = new Color[steps];
    double[] distances = new double[steps];
    
    if (steps <= 1) {
      steps = 2;
    }

    for (int i = 0; i < steps; i++) {
      double angle = (Math.PI * i) / (2 * (steps - 1));
      distances[i] = (float) Math.sin(angle);
      float dl = (float) Math.cos(angle);
      float l = dl * (maxLightness - minLightness) + minLightness;
      colors[i] = color(hsl, l);
    }
    
    return new RadialGradient(x, y, rMax, colors, distances);
  }

  public void draw(Surface g) {
    g.setGradient(booleanValue.value() ? onGradient : offGradient);
    g.fill(shape);
  }

}
