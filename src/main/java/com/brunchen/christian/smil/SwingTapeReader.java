/*
 * SwingTapeReader
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

package com.brunschen.christian.smil;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.brunschen.christian.graphic.Font;
import com.brunschen.christian.graphic.swing.GraphicComponent;
import com.brunschen.christian.graphic.swing.ScrollableGraphicComponent;
import com.brunschen.christian.smil.Clock.UnitTick;

/**
 * @author Christian Brunschen
 */
public class SwingTapeReader extends TapeReader {

  private JComponent component;

  public SwingTapeReader(Clock<UnitTick> tickClock, long ticksPerSecond, Font commentFont) {
    super(tickClock, ticksPerSecond, commentFont);
  }

  public JComponent component() {
    if (component == null) {
      ScrollableGraphicComponent scg = new ScrollableGraphicComponent(graphic(), GraphicComponent.FindScale.Y,
          GraphicComponent.Spacing.MID, GraphicComponent.Spacing.MID, Color.LIGHT_GRAY);
      scg.setSize(350, 70);
      JScrollPane sp = new JScrollPane(scg, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      component = sp;
    }
    return component;
  }
}
