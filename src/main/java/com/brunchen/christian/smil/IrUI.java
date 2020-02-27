/*
 * IrUI
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.brunschen.christian.smil.SMIL;

/**
 * @author Christian Brunschen
 */
public class IrUI extends RegisterUI<ValueRegister> {
  public JLabel leftLabel;
  public JLabel rightLabel;
  public JFormattedTextField leftHexField;
  public JFormattedTextField rightHexField;
  public JTextField leftInstructionField;
  public JTextField rightInstructionField;
  public Operation[] operations;

  public void update() {
    // System.err.format("Updating IR UI from value %05X %05X\n", register.bits(20, 0),
    // register.bits(20, 20));
    leftHexField.setText(String.format("%05X", register.left()));
    rightHexField.setText(String.format("%05X", register.right()));
    leftInstructionField.setText(SMIL.printHalfword(operations, register.value(), false));
    rightInstructionField.setText(SMIL.printHalfword(operations, register.value(), true));
  }

  public IrUI(ValueRegister register, Operation[] operations) {
    super(register);
    this.operations = operations;

    leftLabel = new JLabel("Left");
    rightLabel = new JLabel("Right");

    leftHexField = new JFormattedTextField(SMILApp.maskFormatter("HHHHH"));
    leftHexField.setFont(SMILApp.memoryFont().awtFont());
    leftHexField.setColumns(5);
    leftHexField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          IrUI.this.register.setBits(0, SMIL.HALFWORD_BITS, Long.parseLong((String) e.getNewValue(), 16));
        }
      }
    });

    rightHexField = new JFormattedTextField(SMILApp.maskFormatter("HHHHH"));
    rightHexField.setFont(SMILApp.memoryFont().awtFont());
    rightHexField.setColumns(5);
    rightHexField.addPropertyChangeListener("value", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (shouldUpdate(e)) {
          IrUI.this.register.setBits(SMIL.HALFWORD_BITS, SMIL.HALFWORD_BITS, Long.parseLong((String) e.getNewValue(), 16));
        }
      }
    });

    leftInstructionField = new JTextField(60);
    leftInstructionField.setFont(SMILApp.memoryFont().awtFont());
    leftInstructionField.setEditable(false);

    rightInstructionField = new JTextField(60);
    rightInstructionField.setFont(SMILApp.memoryFont().awtFont());
    rightInstructionField.setEditable(false);

    update();
    leftHexField.setMaximumSize(leftHexField.getPreferredSize());
    rightHexField.setMaximumSize(rightHexField.getPreferredSize());
  }
}
