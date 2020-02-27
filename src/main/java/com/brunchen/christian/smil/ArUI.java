/*
 * ArUI
 *
 * Copyright (C) 2016  Christian Brunschen
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

/**
 * @author Christian Brunschen
 */
public class ArUI extends ValueRegisterUI<Accumulator> {
  private Color overflowColor = new Color(1.0f, 0.8f, 0.8f);
  public JFormattedTextField extraBitField;
  public JFormattedTextField lowBitField;

  public void update() {
    super.update();
    if (extraBitField != null) {
      extraBitField.setValue(register.isExtraBitSet() ? "1" : "0");
    }
    if (lowBitField != null) {
      lowBitField.setValue(register.isLowBitSet() ? "1" : "0");
    }
    if (register.overflow()) {
      doubleField.setBackground(overflowColor);
    } else {
      doubleField.setBackground(Color.WHITE);
    }
  }

  public ArUI(Accumulator register) {
    super(register);
    extraBitField = new JFormattedTextField(SMILApp.bitMaskFormatter());
    extraBitField.setFont(SMILApp.memoryFont().awtFont());
    extraBitField.setColumns(1);
    extraBitField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          ArUI.this.register.setBit(ArUI.this.register.EXTRA_BIT, "1".equals(e.getNewValue()));
        }
      }
    });
    lowBitField = new JFormattedTextField(SMILApp.bitMaskFormatter());
    lowBitField.setFont(SMILApp.memoryFont().awtFont());
    lowBitField.setColumns(1);
    lowBitField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          ArUI.this.register.setBit(ArUI.this.register.LOW_BIT, "1".equals(e.getNewValue()));
        }
      }
    });

    update();
    extraBitField.setMaximumSize(extraBitField.getPreferredSize());
    lowBitField.setMaximumSize(lowBitField.getPreferredSize());
  }

  public double extraBitFieldWidth() {
    return extraBitField.getPreferredSize().getWidth();
  }

  public double lowBitFieldWidth() {
    return lowBitField.getPreferredSize().getWidth();
  }
}
