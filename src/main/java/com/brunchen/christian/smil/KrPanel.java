/*
 * KrPanel
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

/**
 * @author Christian Brunschen
 */
public class KrPanel extends RegisterPanel<ProgramCounter> {
  public static final long serialVersionUID = 0L;

  public KrPanel(KrUI ui) {
    super(ui);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(Box.createHorizontalStrut(4 * padding));
    add(ui.addressField);
    add(ui.leftRightField);
    add(Box.createHorizontalGlue());
    ui.update();
  }
}
