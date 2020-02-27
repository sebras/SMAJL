/*
 * ExtractCallback
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Christian Brunschen
 */
class ExtractCallback extends ProgressCallback {
  JFrame parent;
  JDialog dialog;
  JLabel statusLabel;
  JLabel filenameLabel;
  JProgressBar progressBar;
  JButton okButton;

  public ExtractCallback(JFrame parent) {
    super();
    this.parent = parent;
  }

  public void starting(int n) {
    super.starting(n);
    dialog = new JDialog(parent, "Extracting Help", false);
    JPanel main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
    JPanel labelRow = new JPanel();
    labelRow.setLayout(new BoxLayout(labelRow, BoxLayout.X_AXIS));
    labelRow.add(statusLabel = new JLabel("Waiting"));
    labelRow.add(Box.createHorizontalGlue());
    filenameLabel = new JLabel();
    labelRow.add(filenameLabel);
    labelRow.setAlignmentX(0.5f);
    main.add(labelRow);
    int max = SMILApp.extractDocUrls.length + SMILApp.extractImageUrls.length;
    progressBar = new JProgressBar(0, max);
    progressBar.setAlignmentX(0.5f);
    main.add(progressBar);
    JPanel buttonRow = new JPanel();
    buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
    buttonRow.add(Box.createHorizontalGlue());
    okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dialog.setVisible(false);
      }
    });
    buttonRow.add(okButton);
    okButton.setEnabled(false);
    buttonRow.setAlignmentX(0.5f);
    main.add(buttonRow);
    dialog.add(main);
    dialog.pack();
    dialog.setMinimumSize(new Dimension(400, (int) dialog.getMinimumSize().getHeight()));
    dialog.setPreferredSize(new Dimension(400, (int) dialog.getPreferredSize().getHeight()));
    dialog.setSize(400, dialog.getHeight());
    dialog.setVisible(true);
  }

  public void starting(String filename) {
    super.starting(filename);
    statusLabel.setText("Extracting");
    filenameLabel.setText(filename);
  }

  public void finished(String filename) {
    super.finished(filename);
    statusLabel.setText("Finished");
    filenameLabel.setText(filename);
    progressBar.setValue(done);
  }

  public void exception(String filename, Exception e) {
    super.exception(filename, e);
  }

  public void finished() {
    super.finished();
    statusLabel.setText("Finished");
    filenameLabel.setText(String.format("%d items of %d", done, total));
    okButton.setEnabled(true);
  }
}
