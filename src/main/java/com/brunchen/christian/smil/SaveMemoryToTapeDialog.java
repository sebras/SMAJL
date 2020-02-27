/*
 * SaveMemoryToTape
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

/**
 * @author Christian Brunschen
 */
public class SaveMemoryToTapeDialog extends JDialog implements PropertyChangeListener {
  public static final long serialVersionUID = 0L;

  private int min;
  private int max;
  private int start;
  private int end;

  private static int base = 16;
  private String pattern;
  private String format;

  private JFormattedTextField startField;
  private JFormattedTextField endField;
  private JTextArea startCommentArea;
  private JTextArea endCommentArea;
  private JButton cancelButton;
  private JButton saveButton;

  public static final int SAVE_OPTION = 1;
  public static final int CANCEL_OPTION = 0;

  private int result = CANCEL_OPTION;

  public SaveMemoryToTapeDialog(JFrame owner, int min, int max) {
    super(owner, "Save Memory to Tape", true);
    start = this.min = min;
    end = this.max = max;
    int nDigits = (int) Math.ceil(Math.log(max) / Math.log(base));
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nDigits; i++) {
      sb.append('H');
    }
    pattern = sb.toString();
    format = "%0" + nDigits + "x";

    try {
      GridBagConstraints c = new GridBagConstraints();
      setLayout(new GridBagLayout());

      c.ipadx = c.ipady = 1;
      c.insets = new Insets(1, 1, 1, 1);

      c.gridy = 0;
      c.gridx = 0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.EAST;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.fill = GridBagConstraints.NONE;
      this.add(new JLabel("Start Address:"), c);

      c.gridy = 0;
      c.gridx = 1;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.WEST;
      c.weightx = 0.0;
      c.weighty = 0.1;
      c.fill = GridBagConstraints.NONE;
      this.add(startField = new JFormattedTextField(new MaskFormatter(pattern)), c);
      startField.setValue(String.format(format, start));
      startField.addPropertyChangeListener("value", this);
      startField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

      c.gridy = 0;
      c.gridx = 2;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.EAST;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.fill = GridBagConstraints.NONE;
      this.add(new JLabel("End Address:"), c);

      c.gridy = 0;
      c.gridx = 3;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.WEST;
      c.weightx = 0.0;
      c.weighty = 0.1;
      c.fill = GridBagConstraints.NONE;
      this.add(endField = new JFormattedTextField(new MaskFormatter(pattern)), c);
      endField.setValue(String.format(format, end));
      endField.addPropertyChangeListener("value", this);
      endField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

      c.gridy = 1;
      c.gridx = 0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.EAST;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.fill = GridBagConstraints.NONE;
      this.add(new JLabel("Header Comment:"), c);

      c.gridy = 1;
      c.gridx = 1;
      c.gridwidth = 4;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.WEST;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.fill = GridBagConstraints.BOTH;
      this.add(new JScrollPane(startCommentArea = new JTextArea(2, 20), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), c);

      c.gridy = 2;
      c.gridx = 0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.EAST;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.fill = GridBagConstraints.NONE;
      this.add(new JLabel("Trailer Comment:"), c);

      c.gridy = 2;
      c.gridx = 1;
      c.gridwidth = 4;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.WEST;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.fill = GridBagConstraints.BOTH;
      this.add(new JScrollPane(endCommentArea = new JTextArea(2, 20), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), c);

      JComponent buttonsRow = new JComponent() {
        public static final long serialVersionUID = 0L;
      };
      buttonsRow.setLayout(new BoxLayout(buttonsRow, BoxLayout.X_AXIS));
      buttonsRow.add(cancelButton = new JButton("Cancel"));
      buttonsRow.add(saveButton = new JButton("Save"));

      c.gridy = 3;
      c.gridx = 0;
      c.gridwidth = 5;
      c.gridheight = 1;
      c.anchor = GridBagConstraints.SOUTHEAST;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.fill = GridBagConstraints.NONE;
      this.add(buttonsRow, c);

      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          cancel();
        }
      });
      cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          cancel();
        }
      });
      saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          save();
        }
      });

      pack();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public int start() {
    return start;
  }

  public int end() {
    return end;
  }

  public String startComment() {
    return startCommentArea.getText();
  }

  public String endComment() {
    return endCommentArea.getText();
  }

  public int showDialog() {
    setVisible(true);
    return result;
  }

  /** Called when a field's "value" property changes. */
  public void propertyChange(PropertyChangeEvent e) {
    Object source = e.getSource();
    if (source == startField) {
      int value = Integer.parseInt((String) startField.getValue(), base);
      start = Math.max(value, min);
      startField.setValue(String.format(format, start));
    } else if (source == endField) {
      int value = Integer.parseInt((String) endField.getValue(), base);
      end = Math.min(value, max);
      endField.setValue(String.format(format, end));
    } else {
      // should never happen
    }
  }

  public void cancel() {
    result = CANCEL_OPTION;
    setVisible(false);
  }

  public void save() {
    result = SAVE_OPTION;
    setVisible(false);
  }

}
