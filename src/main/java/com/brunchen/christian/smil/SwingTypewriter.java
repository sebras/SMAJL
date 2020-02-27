/*
 * SwingTypewriter
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

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

/**
 * @author Christian Brunschen
 */
public class SwingTypewriter extends Typewriter.Default {

  protected TypewriterArea textArea;

  protected JComponent component;

  public SwingTypewriter() {
    super();
    textArea = new TypewriterArea(10, 40);
    textArea.setFont(SMILApp.typewriterFont().awtFont());
    textArea.setEditable(false);

    component = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  public int length() {
    return textArea.getDocument().getLength();
  }

  public String text() {
    try {
      return textArea.getDocument().getText(0, length());
    } catch (BadLocationException e) {
      return null;
    }
  }

  public void append(String s) {
    textArea.append(s);
    textArea.setCaretPosition(length());
  }

  public void clear() {
    textArea.selectAll();
    textArea.replaceSelection("");
  }

  public JComponent component() {
    return component;
  }

  public void adjustFontSize(float delta) {
    Font font = textArea.getFont();
    textArea.setFont(font.deriveFont(font.getSize2D() + delta));
  }

  public void increaseFontSize() {
    adjustFontSize(1.0f);
  }

  public void decreaseFontSize() {
    adjustFontSize(-1.0f);
  }

}
