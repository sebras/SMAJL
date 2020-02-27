/*
 * IrPanel
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
import javax.swing.JPanel;

/**
 * @author Christian Brunschen
 */
public class IrPanel extends RegisterPanel<ValueRegister> {
  public static final long serialVersionUID = 0L;

  public IrPanel(IrUI ui) {
    super(ui);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    JPanel column;

    column = new JPanel();
    column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
    column.add(ui.leftLabel);
    column.add(Box.createVerticalStrut(padding));
    column.add(ui.rightLabel);
    add(column);
    add(Box.createHorizontalStrut(padding));

    column = new JPanel();
    column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
    column.add(ui.leftHexField);
    column.add(Box.createVerticalStrut(padding));
    column.add(ui.rightHexField);
    add(column);
    add(Box.createHorizontalStrut(padding));

    column = new JPanel();
    column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
    column.add(ui.leftInstructionField);
    column.add(Box.createVerticalStrut(padding));
    column.add(ui.rightInstructionField);
    add(column);

    ui.update();
  }
}
