/*
 * WordEditor
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

/**
 * Implements a cell editor that uses a formatted text field to edit Words.
 *
 * @author Christian Brunschen
 */
public class WordEditor extends MemoryEditor {
  public static final long serialVersionUID = 0L;

  private static MaskFormatter maskFormatter(String mask) {
    try {
      MaskFormatter formatter = new MaskFormatter(mask);
      formatter.setCommitsOnValidEdit(true);
      return formatter;
    } catch (ParseException e) {
      return null;
    }
  }

  private static long valueOf(String s) {
    try {
      String leftString = s.substring(1, 6);
      String rightString = s.substring(7, 12);
      long left = Long.parseLong(leftString, 16);
      long right = Long.parseLong(rightString, 16);
      long value = left << 20 | right;
      return value;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0L;
    }
  }

  public WordEditor(Memory memory) {
    super(memory, new JFormattedTextField(maskFormatter(" HHHHH HHHHH")));
    ftf.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
          String newString = (String) e.getNewValue();
          long newValue = valueOf(newString);
          WordEditor.this.memory.set(editingAddress, newValue);
        }
      }
    });
  }
}
