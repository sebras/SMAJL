/*
 * PushButton
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

/**
 * 
 */
package com.brunschen.christian.graphic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author cb
 */
public class PushButton extends AbstractGraphic {

  private boolean highlighted = false;

  private String text;

  private Collection<Listener> listeners;

  public PushButton(Size size, String text, Listener... listeners) {
    super(size);
    this.text = text;
    this.listeners = new ArrayList<Listener>(Arrays.asList(listeners));
  }

  @Override
  public void draw(Surface g) {
    double r = 0.475 * Math.min(bounds.getWidth(), bounds.getHeight());
    double x = bounds.getCenterX();
    double y = bounds.getCenterY();

    // outer ring
    Util.fillCircle(g, x, y, r, Color.BLACK);

    // inner ring
    Util.fillCircle(g, x, y, r * 0.8, highlighted ? Color.LIGHT_GRAY : Color.WHITE);

    // core
    Util.fillCircle(g, x, y, r * 0.6, highlighted ? Color.BLACK : Color.DARK_GRAY);
  }

  public void addListeners(Listener... listenersToAdd) {
    for (Listener al : listenersToAdd) {
      listeners.add(al);
    }
  }

  public void addListener(Listener listener) {
    addListeners(listener);
  }

  public void removeListeners(Listener... listenersToRemove) {
    for (Listener al : listenersToRemove) {
      listeners.remove(al);
    }
  }

  public void removeListener(Listener listener) {
    removeListeners(listener);
  }

  @Override
  public boolean mouseDown(Point p) {
    // System.err.format("Button: mouseDown at %s\n", p);
    highlighted = true;
    repaint();
    for (Listener listener : listeners) {
      if (listener != null) {
        listener.buttonPushed(this);
      }
    }
    return true;
  }

  @Override
  public boolean mouseUp(Point p) {
    // System.err.format("Button: mouseUp at %s\n", p);
    highlighted = false;
    parent.repaint(frame());
    for (Listener listener : listeners) {
      if (listener != null) {
        listener.buttonReleased(this);
      }
    }
    return true;
  }

  @Override
  public boolean mouseExited(Point p) {
    return mouseUp(p);
  }

  public String text() {
    return text;
  }

  public static interface Listener {
    void buttonPushed(PushButton button);
    void buttonReleased(PushButton button);
  }
}
