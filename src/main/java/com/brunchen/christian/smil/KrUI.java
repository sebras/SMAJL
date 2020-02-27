/*
 * KrUI
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
public class KrUI extends RegisterUI<ProgramCounter> {
  public JFormattedTextField addressField;
  public JFormattedTextField leftRightField;
  private Color nextToExecuteColor = new Color(0.8f, 1.0f, 0.8f);
  public IrUI irUi;

  public void update() {
    // System.err.format("Updating KR UI from value %03X.%X\n", register.bits(12, 16),
    // register.bit(28));
    addressField.setValue(String.format("%03X", register.value()));
    boolean right = register.isBitSet(ProgramCounter.RIGHT_BIT);
    leftRightField.setValue(right ? "1" : "0");
    irUi.leftInstructionField.setBackground(right ? Color.WHITE : nextToExecuteColor);
    irUi.rightInstructionField.setBackground(right ? nextToExecuteColor : Color.WHITE);
  }

  public KrUI(ProgramCounter register, IrUI irUi) {
    super(register);
    this.irUi = irUi;
    addressField = new JFormattedTextField(SMILApp.maskFormatter("HHH"));
    addressField.setFont(SMILApp.memoryFont().awtFont());
    addressField.setColumns(3);
    addressField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          KrUI.this.register.setValue(Long.parseLong((String) e.getNewValue(), 16));
        }
      }
    });
    leftRightField = new JFormattedTextField(SMILApp.bitMaskFormatter());
    leftRightField.setFont(SMILApp.memoryFont().awtFont());
    leftRightField.setColumns(1);
    leftRightField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          KrUI.this.register.setValue(Long.parseLong((String) e.getNewValue(), 16));
        }
      }
    });
    update();
    addressField.setMaximumSize(addressField.getPreferredSize());
    leftRightField.setMaximumSize(leftRightField.getPreferredSize());
  }
}
