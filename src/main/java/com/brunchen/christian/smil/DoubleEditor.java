/*
 * DoubleEditor
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
import java.text.DecimalFormat;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

/**
 * @author Christian Brunschen
 */
public class DoubleEditor extends MemoryEditor {
  public static final long serialVersionUID = 0L;

  private static NumberFormatter doubleFormatter() {
    NumberFormatter doubleFormatter = new NumberFormatter(new DecimalFormat("0.00000000000000"));
    doubleFormatter.setCommitsOnValidEdit(true);
    return doubleFormatter;
  }

  public DoubleEditor(Memory memory) {
    super(memory, new JFormattedTextField(doubleFormatter()));
    ftf.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
          double val = ((Number) e.getNewValue()).doubleValue();
          DoubleEditor.this.memory.set(editingAddress, SMIL.word(Math.max(SMIL.MIN_DOUBLE, Math.min(
              SMIL.MAX_DOUBLE, val))));
        }
      }
    });
  }

}
