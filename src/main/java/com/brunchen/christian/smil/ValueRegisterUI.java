/*
 * ValueRegisterUI
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
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

import com.brunschen.christian.smil.RegisterUI;

/**
 * @author Christian Brunschen
 */
public class ValueRegisterUI<ValueRegisterT extends ValueRegister> extends RegisterUI<ValueRegisterT> {
  public JFormattedTextField hexField;
  public JFormattedTextField doubleField;
  
  protected double doubleValue() {
    return register.doubleValue();
  }

  public ValueRegisterUI(ValueRegisterT register) {
    super(register);

    hexField = new JFormattedTextField(SMILApp.maskFormatter(mask()));
    hexField.setFont(SMILApp.memoryFont().awtFont());
    hexField.setColumns(11);
    hexField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          ValueRegisterUI.this.register.setValue(valueOf((String) e.getNewValue()));
        }
      }
    });

    NumberFormatter doubleFormatter = new NumberFormatter(new DecimalFormat("0.0000000000000000"));
    doubleField = new JFormattedTextField(doubleFormatter);
    doubleField.setFont(SMILApp.memoryFont().awtFont());
    doubleField.setColumns(20);
    doubleField.setBackground(Color.WHITE);
    doubleField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e) && (e.getNewValue() instanceof Number)) {
          double newValue = ((Number) e.getNewValue()).doubleValue();
          ValueRegisterUI.this.register.setDoubleValue(Math.max(SMIL.MIN_DOUBLE, Math.min(SMIL.MAX_DOUBLE, newValue)));
        }
      }
    });
    doubleField.setFocusLostBehavior(JFormattedTextField.COMMIT);

    update();
    hexField.setMaximumSize(hexField.getPreferredSize());
    doubleField.setMaximumSize(doubleField.getPreferredSize());
  }

  protected String mask() {
    return "HHHHH HHHHH";
  }

  public void update() {
    //System.err.format("Updating %s UI from value %05X %05X\n", register.name(), register.bits(20, 0), register.bits(20, 20));
    if (hexField != null)
      hexField.setValue(String.format("%05X %05X", (register.value() >>> 20) & 0xfffff, register.value() & 0xfffff));
    if (doubleField != null)
      doubleField.setValue(register.doubleValue());
  }
}
