/*
 * HSLColorSpace
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

public class HSLColorSpace {

  static final long serialVersionUID = 0L;

  public HSLColorSpace() {
  }

  private static float oneThird = 1.0f / 3.0f;

  private static float twoThirds = 2.0f / 3.0f;

  private static float hueToRgb(float v1, float v2, float vH) {
    if (vH < 0)
      vH += 1.0f;
    if (vH > 1)
      vH -= 1.0f;
    if ((6 * vH) < 1)
      return (v1 + (v2 - v1) * 6 * vH);
    if ((2 * vH) < 1)
      return (v2);
    if ((3 * vH) < 2)
      return (v1 + (v2 - v1) * (twoThirds - vH) * 6);
    return v1;
  }

  public float[] toRGB(float[] colorvalue) {
    float h = colorvalue[0];
    float s = colorvalue[1];
    float l = colorvalue[2];
    if (s == 0) { // HSL values = 0 ... 1
      return new float[] { l, l, l };
    } else {
      float t1 = l < 0.5f ? l * (1 + s) : (l + s) - (s * l);
      float t2 = 2 * l - t1;
      return new float[] { hueToRgb(t2, t1, h + oneThird), hueToRgb(t2, t1, h), hueToRgb(t2, t1, h - oneThird) };
    }
  }

  public float[] fromRGB(float[] rgbvalue) {
    float r = rgbvalue[0];
    float g = rgbvalue[1];
    float b = rgbvalue[2];

    float minRgb; // Min. value of RGB
    float maxRgb; // Max. value of RGB
    float deltaRgb; // Delta RGB value

    if (r > g) {
      minRgb = g;
      maxRgb = r;
    } else {
      minRgb = r;
      maxRgb = g;
    }

    if (b > maxRgb) {
      maxRgb = b;
    }
    if (b < minRgb) {
      minRgb = b;
    }

    deltaRgb = maxRgb - minRgb;

    float h = 0, s, l;
    l = (maxRgb + minRgb) / 2.0f;

    if (deltaRgb == 0) {
      h = 0;
      s = 0; // gray
    } else { // Chroma
      if (l < 0.5) {
        s = deltaRgb / (maxRgb + minRgb);
      } else {
        s = deltaRgb / (2 - maxRgb - minRgb);
      }

      float deltaR = (((maxRgb - r) / 6) + (deltaRgb / 2)) / deltaRgb;
      float deltaG = (((maxRgb - g) / 6) + (deltaRgb / 2)) / deltaRgb;
      float deltaB = (((maxRgb - b) / 6) + (deltaRgb / 2)) / deltaRgb;

      if (r == maxRgb) {
        h = deltaB - deltaG;
      } else if (g == maxRgb) {
        h = oneThird + deltaR - deltaB;
      } else if (b == maxRgb) {
        h = twoThirds + deltaG - deltaR;
      }
      if (h < 0.0f) {
        h += 1;
      }
      if (h > 1.0f) {
        h -= 1;
      }
    }
    return new float[] { h, s, l };
  }
}
