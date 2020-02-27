/*
 * MemoryEditor
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

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * @author Christian Brunschen
 */
public class MemoryEditor extends DefaultCellEditor {

  public static final long serialVersionUID = 0L;
  protected Memory memory;
  protected int editingAddress;
  protected JFormattedTextField ftf;

  public MemoryEditor(Memory memory, JFormattedTextField textField) {
    super(textField);
    this.memory = memory;
    ftf = (JFormattedTextField) getComponent();
    ftf.setFont(SMILApp.memoryFont().awtFont());

    ftf.setHorizontalAlignment(SwingConstants.LEADING);

    ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

    // React when the user presses Enter while the editor is
    // active. (Tab is handled as specified by
    // JFormattedTextField's focusLostBehavior property.)
    ftf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
    ftf.getActionMap().put("check", new AbstractAction() {
      public static final long serialVersionUID = 0L;

      public void actionPerformed(ActionEvent e) {
        if (!ftf.isEditValid()) { // The text is invalid.
          if (userSaysRevert()) {
            ftf.postActionEvent(); // inform the editor
          }
        } else {
          try { // The text is valid,
            ftf.commitEdit(); // so use it.
            ftf.postActionEvent(); // stop editing
          } catch (java.text.ParseException exc) {
          }
        }
      }
    });
  }

  // Override to invoke setValue on the formatted text field.
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    editingAddress = row / 2;
    ftf.setValue(value);
    return ftf;
  }

  // Override to check whether the edit is valid,
  // setting the value if it is and complaining if
  // it isn't. If it's OK for the editor to go
  // away, we need to invoke the superclass' version
  // of this method so that everything gets cleaned up.
  @Override
  public boolean stopCellEditing() {
    if (getComponent() instanceof JFormattedTextField) {
      if (ftf.isEditValid()) {
        try {
          ftf.commitEdit();
        } catch (java.text.ParseException exc) {
        }
      } else { // text is invalid
        if (!userSaysRevert()) { // user wants to edit
          return false; // don't let the editor go away
        }
      }
    }
    return super.stopCellEditing();
  }

  /**
   * Lets the user know that the text they entered is bad. Returns true if the user elects to revert
   * to the last good value. Otherwise, returns false, indicating that the user wants to continue
   * editing.
   */
  protected boolean userSaysRevert() {
    Toolkit.getDefaultToolkit().beep();
    ftf.selectAll();
    Object[] options = { "Edit", "Revert" };
    int answer = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(ftf), "The value is invalid.\n"
        + "You can either continue editing " + "or revert to the last valid value.", "Invalid Text Entered",
        JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);

    if (answer == 1) { // Revert!
      ftf.setValue(ftf.getValue());
      return true;
    }
    return false;
  }

}
