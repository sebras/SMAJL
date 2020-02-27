/*
 * ValueRegisterPanel
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * @author Christian Brunschen
 */
public class ValueRegisterPanel extends RegisterPanel<ValueRegister> {
  public static final long serialVersionUID = 0L;

  public ValueRegisterPanel(ValueRegisterUI<ValueRegister> ui, int leftSpace, int rightSpace) {
    super(ui);
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(Box.createHorizontalStrut(leftSpace));
    add(ui.hexField);
    add(Box.createHorizontalStrut(rightSpace + padding));
    JLabel equalsLabel = new JLabel("=");
    equalsLabel.setFont(SMILApp.memoryFont().awtFont());
    add(equalsLabel);
    add(Box.createHorizontalStrut(padding));
    add(ui.doubleField);
    add(Box.createHorizontalGlue());
    ui.update();
  }
}
