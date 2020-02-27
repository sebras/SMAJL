/*
 * RegisterUI
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

/**
 * @author Christian Brunschen
 */
public abstract class RegisterUI<RegisterT extends Register> {
  public RegisterT register;

  protected long value() {
    return register.value();
  }

  protected boolean pushing = false;

  protected ActionListener listener;

  public boolean shouldUpdate(PropertyChangeEvent e) {
    boolean shouldUpdate = !pushing && "value".equals(e.getPropertyName()) && e.getNewValue() != null;
    // System.err.format("Should we update '%s'? pushing = %b, propertyName = '%s', newValue =
    // '%s' => %b\n",
    // register.name(), pushing, e.getPropertyName(), e.getNewValue(), shouldUpdate);
    return shouldUpdate;
  }

  public abstract void update();

  public long valueOf(String s) {
    try {
      return Long.parseLong(s.substring(0, 5), 16) << 20 | Long.parseLong(s.substring(6), 16);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0L;
    }
  }

  public RegisterUI(RegisterT register) {
    this.register = register;
  }

  public void addListener() {
    if (listener == null) {
      listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          pushing = true;
          update();
          pushing = false;
        }
      };
      register.addListener(listener);
    }
  }

  public void removeListener() {
    if (listener != null) {
      register.removeListener(listener);
    }
    listener = null;
  }
}
