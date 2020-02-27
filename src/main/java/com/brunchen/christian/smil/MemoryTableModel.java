/*
 * MemoryTableModel
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

import javax.swing.table.AbstractTableModel;

/**
 * @author Christian Brunschen
 */
class MemoryTableModel extends AbstractTableModel implements MemoryChangeListener {
  public static final long serialVersionUID = 0L;
  
  private Memory memory;
  private Operation[] operations;

  public MemoryTableModel(Memory memory, Operation[] operations) {
    super();
    this.memory = memory;
    this.operations = operations;
    memory.addChangeListener(this);
  }
  
  public Memory memory() {
    return memory;
  }

  /*
   * Each word in memory will be presented by 2 rows of 3 columns in the table:  
   * |  address  |  hex value          |  left instruction   |
   * |           |  fixed-point value  |  right instruction  |
   */

  public int getRowCount() {
    return memory().length() * 2;
  }

  public int getColumnCount() {
    return 3;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  private String[] columnNames = new String[] { "Address", "Value", "Instruction" };

  public String getColumnName(int columnIndex) {
    if (columnIndex < columnNames.length) {
      return columnNames[columnIndex];
    }
    return null;
  }

  public boolean isCellEditable(int row, int column) {
    return column == 1;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    int address = rowIndex / 2;
    boolean right = rowIndex % 2 == 1;
    long v = memory().get(address);
    if (columnIndex == 2) {
      return SMIL.printHalfword(operations, v, right);
    }
    if (right) {
      switch (columnIndex) {
        case 0: // address
          return "";
        case 1: // double value
          return SMIL.doubleValue(v);
      }
    } else {
      switch (columnIndex) {
        case 0: // address
          return String.format("%03X", address);
        case 1: // long value
          return String.format(" %05X %05X", (v & 0xfffff00000L) >>> 20, (v & 0xfffff));
      }
    }
    return null;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (rowIndex % 2 == 0 && columnIndex == 1 && aValue instanceof String) {
      String s = (String) aValue;
      int address = rowIndex / 2;
      long value = 0L;
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        int v = Character.digit(c, 16);
        if (0 <= v && v < 16) {
          value = value << 4 | v & 0xf;
        }
      }
      memory().set(address, value);
    } else {
      super.setValueAt(aValue, rowIndex, columnIndex);
    }
  }

  public void memoryChanged(Memory memory, int startAddress, int length) {
    fireTableRowsUpdated(2 * startAddress, 2 * (startAddress + length - 1) + 1);
  }

  public void memoryChanged(Memory memory, int address) {
    memoryChanged(memory, address, 1);
  }
}
