/*
 * SMIL
 *
 * Copyright (C) 2005 - 2007 Christian Brunschen
 *
 * This program is free software; you can redistribute it and/processor.or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, processor.or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY processor.or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package com.brunschen.christian.smil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.security.AccessControlException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;

import com.brunschen.christian.graphic.Size;
import com.brunschen.christian.graphic.ValueUpdatedListener;
import com.brunschen.christian.graphic.swing.GraphicComponent;
import com.brunschen.christian.graphic.swing.SwingFont;
import com.brunschen.christian.smil.sound.SourceDataLineSoundGenerator;

/**
 * @author Christian Brunschen
 */
public class SMILApp {
  
  private SMIL smil;
  private SwingTypewriter typewriter;
  private SourceDataLineSoundGenerator soundGenerator;
  private ControlPanel controlPanel;
  private SwingTapeReader tapeReader;
  
  private JFileChooser fileChooser = null;

  private JFrame controlPanelFrame;
  private JFrame tapeReaderFrame;
  private JFrame typewriterFrame;
  private JFrame debugFrame;
  private JFrame optionsFrame;
  private JTextArea debugArea;
  private JFrame memoryFrame;
  private MemoryTableModel memoryTableModel;
  private JFrame registersFrame;
  private SaveMemoryToTapeDialog saveTapeDialog;
  private JFrame documentationFrame;
  private DocBrowser documentationBrowser;

  // menu actions
  private Action showTypewriterAction;
  private Action showTapeReaderAction;
  private Action showMemoryFrameAction;
  private Action showDebugFrameAction;
  private Action closeWindowAction;
  private Window activeWindow;

  // are we running as an applet?
  protected SMILApplet applet;

  protected boolean runAsFastAsPossible = false;
  protected boolean emulateAsyncDelay = true;
  protected boolean emulateMemoryDelay = true;

  private static Map<String, String> defaultFonts = new HashMap<String, String>();
  static {
    defaultFonts.put("label", "Monospaced 12");
    defaultFonts.put("debug", "Monospaced 12");
    defaultFonts.put("memory", "Monospaced 12");
    defaultFonts.put("clock", "Monospaced 12");
    defaultFonts.put("typewriter", "SMIL 20");
    defaultFonts.put("comment", "SansSerif 12");
    defaultFonts.put("default", "Default 12");
  }

  private static Map<String, SwingFont> fonts = new HashMap<String, SwingFont>();

  public static SwingFont font(String purpose) {
    SwingFont font = fonts.get(purpose);
    if (font == null) {
      String defaultFont = defaultFonts.get(purpose);
      if (defaultFont == null) {
        defaultFont = defaultFonts.get("default");
      }
      String propertyName = "smil." + purpose + "Font";
      try {
        font = SwingFont.decode(System.getProperty(propertyName, defaultFont));
      } catch (AccessControlException e) {
        font = SwingFont.decode(defaultFont);
      }
      fonts.put(purpose, font);
    }
    return font;
  }

  public static SwingFont labelFont() {
    return font("label");
  }

  public static SwingFont debugFont() {
    return font("debug");
  }

  public static SwingFont memoryFont() {
    return font("memory");
  }

  public static SwingFont clockFont() {
    return font("clock");
  }

  public static SwingFont typewriterFont() {
    return font("typewriter");
  }

  public static SwingFont commentFont() {
    return font("comment");
  }

  public static SwingFont defaultFont() {
    return font("default");
  }

  public SMILApp() {
    super();
  }
  
  public void createDevices() {
  }

  public void init() {
    smil = new SMIL();

    smil.setTapeReader(tapeReader = new SwingTapeReader(smil.asyncIoClock(), SMIL.ticksPerSecond, commentFont()));
    smil.setSoundGenerator(soundGenerator = new SourceDataLineSoundGenerator());
    smil.setControlPanel(controlPanel = new ControlPanel(clockFont(), labelFont()));
    smil.setTypewriter(typewriter = new SwingTypewriter());
    
    smil.setDebugDestination(new SMIL.DebugDestination() {
      @Override public void debug(String s) {
        SMILApp.this.debug(s);
      }
    });

    smil.init();

    try {
      fileChooser = new JFileChooser();
    } catch (AccessControlException e) {
      // we're not allowed to read&write files, it seems, possibly running as an applet
      fileChooser = null;
    }
    createAndShowGUI();
    
    tapeReader.setTape(SMIL.tape("A1"));
  }

  public void stopWithError(Exception e) {
    JOptionPane.showMessageDialog(null, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
    smil.stopWithError(e);
  }

  public void clearDebugArea() {
    if (debugArea != null) {
      debugArea.selectAll();
      debugArea.replaceSelection("");
    }
  }

  public void debug(String s) {
    showDebugFrame();
    debugArea.append(s);
  }

  public void loadTape() {
    int returnVal = fileChooser.showOpenDialog(controlPanelFrame);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      try {
        FileReader r = new FileReader(file);
        Tape tape = new Tape(new BufferedReader(r));
        tapeReader.setTape(tape);
        tapeReader.scrollToHead();
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
    }
  }

  public void saveMemoryToTape() {
    int returnVal = saveTapeDialog().showDialog();
    if (returnVal != SaveMemoryToTapeDialog.SAVE_OPTION) {
      return;
    }

    returnVal = fileChooser.showSaveDialog(controlPanelFrame);
    if (returnVal != JFileChooser.APPROVE_OPTION) {
      return;
    }

    File file = fileChooser.getSelectedFile();
    try {
      FileWriter fw = new FileWriter(file);
      PrintWriter pw = new PrintWriter(fw);
      int start = saveTapeDialog().start();
      int end = saveTapeDialog().end();
      BufferedReader br;
      String line;
      br = new BufferedReader(new StringReader(saveTapeDialog().startComment()));
      while ((line = br.readLine()) != null) {
        pw.format("# %s\n", line);
      }
      pw.format("%03X00 %03X00\n", start, end);
      for (int i = start; i <= end; i++) {
        long word = smil.memory().get(i);
        long left = (word & 0xfffff00000L) >>> 20;
        long right = word & 0xfffffL;
        pw.format("%05X %05X\n", left, right);
      }
      br = new BufferedReader(new StringReader(saveTapeDialog().endComment()));
      while ((line = br.readLine()) != null) {
        pw.format("# %s\n", line);
      }
      fw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SaveMemoryToTapeDialog saveTapeDialog() {
    if (saveTapeDialog == null) {
      saveTapeDialog = new SaveMemoryToTapeDialog(controlPanelFrame, 0, smil.memory().length() - 1);
    }
    return saveTapeDialog;
  }

  public Action showTypewriterAction() {
    if (showTypewriterAction == null) {
      showTypewriterAction = new AbstractAction("Typewriter") {
        public static final long serialVersionUID = 0L;

        public void actionPerformed(ActionEvent e) {
          showTypewriter();
        }
      };
    }
    return showTypewriterAction;
  }

  public Action showTapeReaderAction() {
    if (showTapeReaderAction == null) {
      showTapeReaderAction = new AbstractAction("Tape Reader") {
        public static final long serialVersionUID = 0L;

        public void actionPerformed(ActionEvent e) {
          showTapeReader();
        }
      };
    }
    return showTapeReaderAction;
  }

  public Action showMemoryFrameAction() {
    if (showMemoryFrameAction == null) {
      showMemoryFrameAction = new AbstractAction("Memory") {
        public static final long serialVersionUID = 0L;

        public void actionPerformed(ActionEvent e) {
          showMemoryFrame();
        }
      };
    }
    return showMemoryFrameAction;
  }

  public Action showDebugFrameAction() {
    if (showDebugFrameAction == null) {
      showDebugFrameAction = new AbstractAction("Debug Output") {
        public static final long serialVersionUID = 0L;

        public void actionPerformed(ActionEvent e) {
          showDebugFrameAction();
        }
      };
    }
    return showDebugFrameAction;
  }

  public Action closeWindowAction() {
    if (closeWindowAction == null) {
      closeWindowAction = new AbstractAction("Close Window") {
        public static final long serialVersionUID = 0L;

        public void actionPerformed(ActionEvent e) {
          closeActiveWindow();
        }
      };
    }
    return closeWindowAction;
  }

  public void closeActiveWindow() {
    activeWindow.setVisible(false);
  }

  public JMenuItem addMenuItem(JMenu menu, String title, int mnemonic, KeyStroke accelerator, ActionListener listener) {
    JMenuItem menuItem = new JMenuItem(title, mnemonic);
    menuItem.setAccelerator(accelerator);
    menuItem.addActionListener(listener);
    menu.add(menuItem);
    return menuItem;
  }

  public JMenuItem addMenuItem(JMenu menu, String title, int mnemonic, int acceleratorKey, int acceleratorMask,
      ActionListener listener)
  {
    return addMenuItem(menu, title, mnemonic, KeyStroke.getKeyStroke(acceleratorKey, acceleratorMask
        | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), listener);
  }

  public JMenuItem addMenuItem(JMenu menu, String title, int mnemonic, int acceleratorKey, ActionListener listener) {
    return addMenuItem(menu, title, mnemonic, acceleratorKey, 0, listener);
  }

  public JMenuItem addMenuItem(JMenu menu, String title, int mnemonic, ActionListener listener) {
    return addMenuItem(menu, title, mnemonic, null, listener);
  }

  public JFrame newFrame(String title) {
    JFrame frame = new JFrame(title);
    frame.addWindowFocusListener(new WindowFocusListener() {
      public void windowGainedFocus(WindowEvent e) {
        activeWindow = e.getWindow();
        closeWindowAction().setEnabled(activeWindow != controlPanelFrame);
      }

      public void windowLostFocus(WindowEvent e) {
      }
    });
    // JPanel root = new JPanel();
    // root.setBackground(backgroundColor);
    // root.setOpaque(true);
    // frame.setContentPane(root);
    return frame;
  }

  private static void setEnabled(List<JComponent> components, boolean enabled) {
    for (JComponent component : components) {
      component.setEnabled(enabled);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" }) // JComboBox became generic from Java 6 to Java 7.
  public void showOptionsPanel() {
    if (optionsFrame == null) {
      optionsFrame = newFrame("Options");
      optionsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

      BorderLayout borderLayout = new BorderLayout();
      optionsFrame.setLayout(borderLayout);

      // running speed options
      final List<JComponent> speedFormComponents = new LinkedList<JComponent>();

      // Create the radio buttons.
      JRadioButton asFastAsPossibleButton = new JRadioButton("run as fast as possible");
      asFastAsPossibleButton.setMnemonic(KeyEvent.VK_F);
      asFastAsPossibleButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          runAsFastAsPossible = true;
          setEnabled(speedFormComponents, false);
          smil.tickClock().setWillWait(false);
          smil.tickClock().interruptSleepers();
        }
      });
      asFastAsPossibleButton.setSelected(runAsFastAsPossible);

      JRadioButton originalSmilSpeedButton = new JRadioButton("emulate original SMIL speed");
      originalSmilSpeedButton.setMnemonic(KeyEvent.VK_E);
      originalSmilSpeedButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          runAsFastAsPossible = false;
          setEnabled(speedFormComponents, true);
          smil.tickClock().setWillWait(true);
          smil.tickClock().interruptSleepers();
        }
      });
      originalSmilSpeedButton.setSelected(!runAsFastAsPossible);

      // Group the radio buttons.
      ButtonGroup group = new ButtonGroup();
      group.add(asFastAsPossibleButton);
      group.add(originalSmilSpeedButton);

      // a slider to specify speed relative to original SMIL
      JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 4000, 2000);
      speedSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          JSlider slider = (JSlider) e.getSource();
          double exponent = (slider.getValue() - 2000) / 1000.0;
          smil.tickClock().setSpeedup(Math.exp(Math.log(10.0) * exponent));
          smil.tickClock().interruptSleepers();
        }
      });

      speedSlider.setMajorTickSpacing(1000);
      speedSlider.setMinorTickSpacing(0);
      speedSlider.setPaintTicks(true);

      Dictionary<Integer, JComponent> labelTable = new Hashtable<Integer, JComponent>();
      for (int i = speedSlider.getMinimum(); i <= speedSlider.getMaximum(); i += 1000) {
        int exponent = (i - 2000) / 1000;
        String labelText;
        if (exponent < 0) {
          labelText = String.format("÷%d", (int) Math.round(Math.exp(Math.log(10) * -exponent)));
        } else {
          labelText = String.format("×%d", (int) Math.round(Math.exp(Math.log(10) * exponent)));
        }
        JLabel label = new JLabel(labelText);
        labelTable.put(i, label);
      }
      speedSlider.setLabelTable(labelTable);
      speedSlider.setPaintLabels(true);

      JCheckBox emulateAsyncDelayBox = new JCheckBox("Asynchronous I/O Delays", emulateAsyncDelay);
      emulateAsyncDelayBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          emulateAsyncDelay = !emulateAsyncDelay;
          smil.asyncIoClock().setWillWait(emulateAsyncDelay);
          smil.asyncIoClock().interruptSleepers();
        }
      });

      JCheckBox emulateMemoryDelayBox = new JCheckBox("Drum Memory Latency", emulateMemoryDelay);
      emulateMemoryDelayBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          emulateMemoryDelay = !emulateMemoryDelay;
          smil.memoryClock().setWillWait(emulateMemoryDelay);
          smil.memoryClock().interruptSleepers();
        }
      });

      JPanel speedDetailsPanel = new JPanel();
      speedDetailsPanel.setLayout(new BoxLayout(speedDetailsPanel, BoxLayout.Y_AXIS));
      speedSlider.setAlignmentX(0.0f);
      speedDetailsPanel.add(speedSlider);
      emulateAsyncDelayBox.setAlignmentX(0.0f);
      speedDetailsPanel.add(emulateAsyncDelayBox);
      emulateMemoryDelayBox.setAlignmentX(0.0f);
      speedDetailsPanel.add(emulateMemoryDelayBox);

      JPanel speedDetailsRow = new JPanel();
      speedDetailsRow.setLayout(new BoxLayout(speedDetailsRow, BoxLayout.X_AXIS));
      speedDetailsRow.add(Box.createHorizontalStrut(20));
      speedDetailsRow.add(speedDetailsPanel);

      // Put the radio buttons, slider, and checkboxes in a column in a panel.
      JPanel emulatorSpeedPanel = new JPanel();
      emulatorSpeedPanel.setLayout(new BoxLayout(emulatorSpeedPanel, BoxLayout.Y_AXIS));
      asFastAsPossibleButton.setAlignmentX(0.0f);
      emulatorSpeedPanel.add(asFastAsPossibleButton);
      originalSmilSpeedButton.setAlignmentX(0.0f);
      emulatorSpeedPanel.add(originalSmilSpeedButton);
      speedDetailsRow.setAlignmentX(0.0f);
      emulatorSpeedPanel.add(speedDetailsRow);
      emulatorSpeedPanel.setBorder(BorderFactory.createTitledBorder("Emulator Speed"));

      speedFormComponents.add(speedDetailsPanel);
      speedFormComponents.add(speedSlider);
      speedFormComponents.add(emulateAsyncDelayBox);
      speedFormComponents.add(emulateMemoryDelayBox);

      // sound options
      final List<JComponent> soundFormComponents = new LinkedList<JComponent>();

      int logBufferLength = soundGenerator.canGenerateSound() 
          ? (int) Math.max(0, Math.min(3000, 1000 * Math.log10(soundGenerator.bufferLengthMillis)))
          : 1000;

      // a slider to specify the sound buffer size (in seconds)
      final JSlider soundBufferLengthSlider = new JSlider(JSlider.HORIZONTAL, 0, 3000, logBufferLength);

      // a value synchronizer to keep the slider and the sound generator in synch
      final ValueSynchronizer<Integer> bufferSizeMillisSynchronizer = new ValueSynchronizer<Integer>();
      bufferSizeMillisSynchronizer.addParticipant(soundBufferLengthSlider, new ValueSynchronizer.Setter<Integer>() {
        public void setValue(Integer value) {
          soundBufferLengthSlider.setValue((int) Math.max(0, Math.min(3000, 1000 * Math.log10(value))));
        }
      });
      bufferSizeMillisSynchronizer.addParticipant(soundGenerator, new ValueSynchronizer.Setter<Integer>() {
        public void setValue(Integer value) {
          soundGenerator.setBufferLengthMillis(value);
        }
      });

      soundBufferLengthSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          JSlider slider = (JSlider) e.getSource();
          double exponent = slider.getValue() / 1000.0;
          int value = (int) Math.exp(Math.log(10.0) * exponent);
          bufferSizeMillisSynchronizer.setValueFromParticipant(value, soundBufferLengthSlider);
        }
      });
      soundBufferLengthSlider.setMajorTickSpacing(1000);
      soundBufferLengthSlider.setMinorTickSpacing(0);
      soundBufferLengthSlider.setPaintTicks(true);

      soundGenerator.addBufferLengthMillisUpdatedListener(new ValueUpdatedListener<Integer>() {
        public void valueUpdated(Integer oldValue, Integer newValue) {
          bufferSizeMillisSynchronizer.setValueFromParticipant(newValue, soundGenerator);
        }
      });

      labelTable = new Hashtable<Integer, JComponent>();
      for (int i = soundBufferLengthSlider.getMinimum(); i <= soundBufferLengthSlider.getMaximum(); i += 1000) {
        int exponent = i / 1000;
        int millis = (int) Math.round(Math.exp(Math.log(10.0) * exponent));
        JLabel label = new JLabel(String.format("%d", millis));
        labelTable.put(i, label);
      }
      soundBufferLengthSlider.setLabelTable(labelTable);
      soundBufferLengthSlider.setPaintLabels(true);

      JLabel soundBufferLengthLabel = new JLabel("Sound Buffer Size (ms):");
      JTextField soundBufferLengthTextField = new JTextField(String.format("%d", soundGenerator.bufferLengthMillis));
      soundBufferLengthLabel.setLabelFor(soundBufferLengthSlider);

      soundFormComponents.add(soundBufferLengthLabel);
      soundFormComponents.add(soundBufferLengthTextField);
      soundFormComponents.add(soundBufferLengthSlider);

      JLabel soundSourceBitLabel = new JLabel("Bit:", JLabel.TRAILING);
      final JComboBox soundSourceBitComboBox = new JComboBox(smil.soundSourceRegister().bitNames().toArray());
      String n = smil.soundSourceRegister().nameForBit(smil.soundSourceBit());
      soundSourceBitComboBox.setSelectedItem(n);
      soundSourceBitLabel.setLabelFor(soundSourceBitComboBox);
      soundSourceBitComboBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox comboBox = (JComboBox) e.getSource();
          String bitName = (String) comboBox.getSelectedItem();
          if (bitName != null) {
            smil.connectSound(smil.soundSourceRegister(), smil.soundSourceRegister().bitWithName(bitName));
          }
        }
      });

      JLabel soundSourceRegisterLabel = new JLabel("Register:", JLabel.TRAILING);

      // construct the list of register names and the map from register name to register
      String[] registerNames = new String[smil.processor().registers.length];
      final Map<String, Register> registersByName = new HashMap<String, Register>();
      int i = 0;
      for (Register r : smil.processor().registers) {
        registerNames[i++] = r.name();
        registersByName.put(r.name(), r);
      }

      JComboBox soundSourceRegisterComboBox = new JComboBox(registerNames);
      soundSourceRegisterComboBox.setSelectedItem(smil.soundSourceRegister().name());
      soundSourceRegisterLabel.setLabelFor(soundSourceRegisterComboBox);
      soundSourceRegisterComboBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox comboBox = (JComboBox) e.getSource();
          String registerName = (String) comboBox.getSelectedItem();
          Register register = registersByName.get(registerName);
          String newSourceBitName = smil.soundSourceRegister().nameForBit(smil.soundSourceBit());
          if (register.bitWithName(newSourceBitName) == null) {
            newSourceBitName = register.bitNames().iterator().next();
          }
          smil.connectSound(register, register.bitWithName(newSourceBitName));

          // now, update the bit combo box
          soundSourceBitComboBox.removeAllItems();
          for (String bitName : register.bitNames()) {
            soundSourceBitComboBox.addItem(bitName);
          }
          soundSourceBitComboBox.setSelectedItem(newSourceBitName);
        }
      });

      JPanel soundSourceForm = new JPanel();
      soundSourceForm.setLayout(new SpringLayout());
      soundSourceForm.add(soundSourceRegisterLabel);
      soundSourceForm.add(soundSourceRegisterComboBox);
      soundSourceForm.add(soundSourceBitLabel);
      soundSourceForm.add(soundSourceBitComboBox);
      SpringUtilities.makeCompactGrid(soundSourceForm, 2, 2, 0, 0, 0, 0);
      soundFormComponents.add(soundSourceRegisterLabel);
      soundFormComponents.add(soundSourceRegisterComboBox);
      soundFormComponents.add(soundSourceBitLabel);
      soundFormComponents.add(soundSourceBitComboBox);

      JPanel soundSourceFormRow = new JPanel();
      soundSourceFormRow.setLayout(new BoxLayout(soundSourceFormRow, BoxLayout.X_AXIS));
      soundSourceFormRow.add(soundSourceForm);
      soundSourceFormRow.add(Box.createHorizontalGlue());

      JCheckBox soundEnabledCheckbox = new JCheckBox("Enabled", smil.isSoundEnabled());
      soundEnabledCheckbox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (smil.isSoundEnabled()) {
            smil.stopSound();
          } else {
            smil.startSound();
          }
          setEnabled(soundFormComponents, smil.isSoundEnabled());
        }
      });
      JPanel soundEnabledRow = new JPanel();
      soundEnabledRow.setLayout(new BoxLayout(soundEnabledRow, BoxLayout.X_AXIS));
      soundEnabledRow.add(soundEnabledCheckbox);
      soundEnabledRow.add(Box.createHorizontalGlue());

      JPanel soundPanel = new JPanel();
      soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.Y_AXIS));
      soundPanel.add(soundEnabledRow);
      soundPanel.add(soundBufferLengthLabel);
      soundPanel.add(soundBufferLengthSlider);
      soundPanel.add(soundSourceFormRow);
      soundPanel.setBorder(BorderFactory.createTitledBorder("Sound"));
      if (!soundGenerator.canGenerateSound()) {
        soundPanel.setEnabled(false);
      }

      // we want some more border around things, so we put the whole thing onto another panel with a
      // border
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      emulatorSpeedPanel.setAlignmentX(0.5f);
      mainPanel.add(emulatorSpeedPanel);
      // redrawPanel.setAlignmentX(0.5f);
      // mainPanel.add(redrawPanel);
      soundPanel.setAlignmentX(0.5f);
      mainPanel.add(soundPanel);

      mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      optionsFrame.add(mainPanel, BorderLayout.CENTER);
      optionsFrame.pack();
    }

    optionsFrame.setVisible(true);
  }

  public void showControlPanel() {
    if (controlPanelFrame == null) {
      // Create and set up the control panel window.
      controlPanelFrame = newFrame("Control Panel");
      try {
        controlPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      } catch (AccessControlException e) {
        if (applet != null) {
          controlPanelFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          controlPanelFrame.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
              System.err.format("WindowEvent: %s\n", e);
              if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                System.err.format("  closing window\n");
                applet.stop();
              }
            }
          });
        }
      }
      controlPanelFrame.setLayout(new BorderLayout());
      GraphicComponent component = new GraphicComponent(controlPanel.graphic(), new com.brunschen.christian.graphic.Color(0.95f, 0.95f, 0.95f));
      component.addSizeChangedListener(new ValueUpdatedListener<Size>() {
        public void valueUpdated(Size oldValue, Size newValue) {
          controlPanel.onSizeChanged(oldValue, newValue);
        }
      });
      controlPanelFrame.add(component, BorderLayout.CENTER);
      JMenuBar menuBar = new JMenuBar();

      // add the (sparsely populated) 'File' menu
      JMenu tapeMenu = new JMenu("Tape");
      JMenuItem loadTapeMenuItem = addMenuItem(tapeMenu, "Load Tape ...", KeyEvent.VK_L, KeyEvent.VK_O,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              loadTape();
            }
          });
      if (fileChooser == null) {
        loadTapeMenuItem.setEnabled(false);
      }
      JMenu standardTapesMenu = new JMenu("Load Standard Tape");
      for (final String tape : SMIL.tapes) {
        addMenuItem(standardTapesMenu, tape, 0, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              tapeReader.setTape(SMIL.tape(tape));
              tapeReader.scrollToHead();
            } catch (RuntimeException ex) {
              System.err.format("failed to read standard tape '%s':\n", tape);
              ex.printStackTrace(System.err);
            }
          }
        });
      }
      tapeMenu.add(standardTapesMenu);
      JMenuItem saveToTapeMenuItem = addMenuItem(tapeMenu, "Save Memory to Tape ...", KeyEvent.VK_S, KeyEvent.VK_S,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              saveMemoryToTape();
            }
          });
      if (fileChooser == null) {
        saveToTapeMenuItem.setEnabled(false);
      }
      menuBar.add(tapeMenu);

      // add the 'Windows' menu
      JMenu windowsMenu = new JMenu("Windows");
      addMenuItem(windowsMenu, "Tape Reader", KeyEvent.VK_R, KeyEvent.VK_R, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showTapeReader();
        }
      });
      addMenuItem(windowsMenu, "Typewriter", KeyEvent.VK_T, KeyEvent.VK_T, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showTypewriter();
        }
      });
      addMenuItem(windowsMenu, "Debug Output", KeyEvent.VK_D, KeyEvent.VK_D, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showDebugFrame();
        }
      });
      addMenuItem(windowsMenu, "Memory", KeyEvent.VK_M, KeyEvent.VK_M, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showMemoryFrame();
        }
      });
      addMenuItem(windowsMenu, "Registers", KeyEvent.VK_G, KeyEvent.VK_G, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showRegistersFrame();
        }
      });
      addMenuItem(windowsMenu, "Options", KeyEvent.VK_COMMA, KeyEvent.VK_COMMA, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showOptionsPanel();
        }
      });
      JMenuItem helpMenuItem = addMenuItem(windowsMenu, "Help", KeyEvent.VK_H, KeyEvent.VK_H, InputEvent.ALT_MASK,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              showDocumentationFrame(null);
            }
          });
      if (quickStartUrl == null || manualUrl == null) {
        helpMenuItem.setEnabled(false);
      }
      menuBar.add(windowsMenu);

      JMenu debugMenu = new JMenu("Debug");
      addMenuItem(debugMenu, "Memory", KeyEvent.VK_M, KeyEvent.VK_M, InputEvent.SHIFT_MASK, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          smil.dumpMemory();
        }
      });
      addMenuItem(debugMenu, "Tape", KeyEvent.VK_T, KeyEvent.VK_T, InputEvent.SHIFT_MASK, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          smil.dumpTape();
        }
      });
      JCheckBoxMenuItem traceMenuItem = new JCheckBoxMenuItem("Trace Execution");
      traceMenuItem.setMnemonic(KeyEvent.VK_T);
      traceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.ALT_MASK
          | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      traceMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          smil.setTrace(!smil.trace());
        }
      });
      debugMenu.add(traceMenuItem);
      menuBar.add(debugMenu);

      JMenu documentationMenu = new JMenu("Help");
      JMenuItem quickStartItem = addMenuItem(documentationMenu, "Quick Start", KeyEvent.VK_Q, KeyEvent.VK_Q,
          InputEvent.ALT_MASK, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              showQuickStart();
            }
          });
      if (quickStartUrl == null) {
        quickStartItem.setEnabled(false);
      }
      JMenuItem manualItem = addMenuItem(documentationMenu, "Manual", KeyEvent.VK_M, KeyEvent.VK_M,
          InputEvent.ALT_MASK, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              showManual();
            }
          });
      if (manualUrl == null) {
        manualItem.setEnabled(false);
      }
      JMenuItem extractItem = addMenuItem(documentationMenu, "Extract...", KeyEvent.VK_E, KeyEvent.VK_E,
          InputEvent.ALT_MASK, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              extractDocumentation();
            }
          });
      if (fileChooser == null
          || extractDocUrls == null || extractDocUrls.length == 0 || extractDocUrls[0] == null) {
        extractItem.setEnabled(false);
      }
      menuBar.add(documentationMenu);

      // put the menu bar on the window
      controlPanelFrame.setJMenuBar(menuBar);
      controlPanelFrame.setFocusable(true);
      // Display the window.
      controlPanelFrame.pack();
    }
    controlPanelFrame.setVisible(true);
    controlPanelFrame.toFront();
  }

  public JButton newButton(String title, ActionListener listener) {
    JButton button = new JButton(title);
    button.addActionListener(listener);
    return button;
  }

  public void showTapeReader() {
    if (tapeReaderFrame == null) {
      GridBagConstraints c = new GridBagConstraints();

      // Create, set up and show the tape reader window.
      tapeReaderFrame = newFrame("Tape Reader");
      tapeReaderFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      tapeReaderFrame.setLayout(new GridBagLayout());
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.gridwidth = 3;
      c.gridheight = 1;
      c.fill = GridBagConstraints.BOTH;
      tapeReaderFrame.add(tapeReader.component(), c);
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 1.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_START;
      tapeReaderFrame.add(new Box.Filler(new Dimension(0, 0), new Dimension(1, 1), new Dimension(10, 10)), c);
      c.gridx = 1;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_END;
      tapeReaderFrame.add(newButton("Remove Tape", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          tapeReader.setTape(null);
        }
      }), c);
      c.gridx = 2;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_END;
      tapeReaderFrame.add(newButton("Load Tape", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          loadTape();
        }
      }), c);
      tapeReaderFrame.pack();
      tapeReaderFrame.setLocation((int) controlPanelFrame.getBounds().getMaxX() + 5, (int) controlPanelFrame
          .getBounds().getY());
      tapeReader.scrollToHead();
    }
    tapeReaderFrame.setVisible(true);
    tapeReaderFrame.toFront();
  }

  public void showTypewriter() {
    if (typewriterFrame == null) {
      GridBagConstraints c = new GridBagConstraints();
      typewriterFrame = newFrame("Typewriter");
      typewriterFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      typewriterFrame.setLayout(new GridBagLayout());

      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.gridwidth = 4;
      c.gridheight = 1;
      c.fill = GridBagConstraints.BOTH;
      typewriterFrame.add(typewriter.component(), c);

      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_START;
      typewriterFrame.add(newButton("-", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          typewriter.decreaseFontSize();
        }
      }), c);

      c.gridx = 1;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      typewriterFrame.add(newButton("+", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          typewriter.increaseFontSize();
        }
      }), c);

      c.gridx = 2;
      c.gridy = 1;
      c.weightx = 1.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      typewriterFrame.add(new Box.Filler(new Dimension(0, 0), new Dimension(1, 1), new Dimension(10, 10)), c);

      c.gridx = 3;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_END;
      typewriterFrame.add(newButton("Clear Typewriter", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          typewriter.clear();
        }
      }), c);

      typewriterFrame.pack();
      typewriterFrame.setLocation((int) tapeReaderFrame.getBounds().getX(),
          (int) tapeReaderFrame.getBounds().getMaxY() + 5);
    }
    typewriterFrame.setVisible(true);
    typewriterFrame.toFront();
  }

  public void showDebugFrame() {
    if (debugFrame == null) {
      GridBagConstraints c = new GridBagConstraints();
      debugFrame = newFrame("Debug Output");
      debugFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      debugFrame.setLayout(new GridBagLayout());
      debugArea = new JTextArea(30, 90);
      debugArea.setFont(debugFont().awtFont());
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.gridwidth = 2;
      c.gridheight = 1;
      c.fill = GridBagConstraints.BOTH;
      debugFrame.add(new JScrollPane(debugArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
          JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), c);
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 1.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_START;
      debugFrame.add(new Box.Filler(new Dimension(0, 0), new Dimension(1, 1), new Dimension(10, 10)), c);
      c.gridx = 1;
      c.gridy = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.LAST_LINE_END;
      debugFrame.add(newButton("Clear", new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearDebugArea();
        }
      }), c);
      debugFrame.pack();
    }
    debugFrame.setVisible(true);
    debugFrame.toFront();
  }

  public synchronized void showMemoryFrame() {
    if (memoryFrame == null) {
      memoryFrame = newFrame("Memory");
      memoryFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      memoryTableModel = new MemoryTableModel(smil.memory(), smil.processor().operations);
      MemoryTable memoryTable = new MemoryTable(memoryTableModel);
      memoryTable.setFont(memoryFont().awtFont());
      JScrollPane scrollPane = new JScrollPane(memoryTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
          JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane.setAlignmentX(1.0f);
      mainPanel.add(scrollPane);
      JButton clearButton = new JButton("Clear Memory");
      clearButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          smil.memory().clear();
        }
      });
      clearButton.setAlignmentX(1.0f);
      mainPanel.add(clearButton);

      memoryFrame.setLayout(new BorderLayout());
      memoryFrame.add(mainPanel, BorderLayout.CENTER);
      memoryFrame.pack();
      memoryFrame.setSize((int) memoryTable.getPreferredSize().getWidth(), (int) Math.min(memoryTable
          .getPreferredSize().getHeight(), Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2));
      memoryFrame.addWindowListener(new WindowAdapter() {
        MemoryTableModel mtm = memoryTableModel;

        public void show() {
          mtm.memoryChanged(smil.memory(), 0, smil.memory().length());
          memoryTableModel = mtm;
        }

        public void hide() {
          memoryTableModel = null;
        }

        public void windowActivated(WindowEvent e) {
          show();
        }

        public void windowGainedFocus(WindowEvent e) {
          show();
        }

        public void windowClosing(WindowEvent e) {
          hide();
        }
      });
    }
    memoryFrame.setVisible(true);
    memoryFrame.toFront();
  }

  public static MaskFormatter maskFormatter(String mask) {
    try {
      MaskFormatter formatter = new MaskFormatter(mask);
      formatter.setCommitsOnValidEdit(true);
      return formatter;
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static MaskFormatter bitMaskFormatter() {
    try {
      MaskFormatter formatter = new MaskFormatter("H");
      formatter.setCommitsOnValidEdit(true);
      formatter.setValidCharacters("01");
      return formatter;
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static long value(String s) {
    try {
      return Long.parseLong(s.substring(0, 5), 16) << 20 | Long.parseLong(s.substring(6), 16);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0L;
    }
  }

  public void showRegistersFrame() {
    if (registersFrame == null) {
      registersFrame = newFrame("Registers");
      registersFrame.setLayout(new BorderLayout());

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      
      Processor processor = smil.processor();

      final ValueRegisterUI<ValueRegister> mdUi = new ValueRegisterUI<ValueRegister>(processor.md);
      final ArUI arUi = new ArUI(processor.ar);
      final ValueRegisterUI<ValueRegister> mrUi = new ValueRegisterUI<ValueRegister>(processor.mr);
      final IrUI irUi = new IrUI(processor.ir, processor.operations);
      final KrUI krUi = new KrUI(processor.kr, irUi);

      final Collection<RegisterUI<? extends Register>> registerUIs = new ArrayList<RegisterUI<? extends Register>>(5);
      registerUIs.add(mdUi);
      registerUIs.add(arUi);
      registerUIs.add(mrUi);
      registerUIs.add(irUi);
      registerUIs.add(krUi);

      final Collection<RegisterPanel<? extends Register>> registerPanels = new ArrayList<RegisterPanel<? extends Register>>(
          5);
      registerPanels.add(new ValueRegisterPanel(mdUi, (int) arUi.extraBitFieldWidth(), (int) arUi.lowBitFieldWidth()));
      registerPanels.add(new ArPanel(arUi));
      registerPanels.add(new ValueRegisterPanel(mrUi, (int) arUi.extraBitFieldWidth(), (int) arUi.lowBitFieldWidth()));
      registerPanels.add(new IrPanel(irUi));
      registerPanels.add(new KrPanel(krUi));

      for (RegisterPanel<? extends Register> rp : registerPanels) {
        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createTitledBorder(rp.ui.register.name()));
        borderPanel.add(rp, BorderLayout.CENTER);
        mainPanel.add(borderPanel);
      }

      registersFrame.add(mainPanel, BorderLayout.CENTER);
      registersFrame.pack();

      registersFrame.addWindowListener(new WindowAdapter() {
        public void show() {
          for (RegisterUI<? extends Register> rui : registerUIs) {
            rui.addListener();
            rui.update();
          }
        }

        public void hide() {
          for (RegisterUI<? extends Register> rui : registerUIs) {
            rui.removeListener();
          }
        }

        public void windowActivated(WindowEvent e) {
          show();
        }

        public void windowGainedFocus(WindowEvent e) {
          show();
        }

        public void windowClosing(WindowEvent e) {
          hide();
        }
      });
    }
    registersFrame.setVisible(true);
    registersFrame.toFront();
  }

  private static URL quickStartUrl;
  private static URL manualUrl;
  static {
    try {
      quickStartUrl = SMILApp.class.getResource("Documentation/QuickStart.html");
      manualUrl = SMILApp.class.getResource("Documentation/Manual.html");
    } catch (AccessControlException e) {
      quickStartUrl = null;
      manualUrl = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showDocumentationFrame(URL url) {
    if (documentationFrame == null) {
      documentationFrame = newFrame("Help");
      documentationFrame.setLayout(new BorderLayout());
      List<DocBrowser.TreeNode> docNodes = new ArrayList<DocBrowser.TreeNode>(2);
      docNodes.add(new DocBrowser.TreeNode("Quick Start", quickStartUrl.toString()));
      docNodes.add(new DocBrowser.TreeNode("SMILemu Manual", manualUrl.toString()));
      documentationBrowser = new DocBrowser(docNodes);
      documentationFrame.add(documentationBrowser);
      documentationFrame.pack();
    }
    if (url != null) {
      documentationBrowser.navigateTo(url.toString());
    }
    documentationFrame.setVisible(true);
    documentationFrame.toFront();
  }

  public void showQuickStart() {
    showDocumentationFrame(quickStartUrl);
  }

  public void showManual() {
    showDocumentationFrame(manualUrl);
  }

  static URL[] extractDocUrls = new URL[] { SMILApp.class.getResource("Documentation/QuickStart.html"),
      SMILApp.class.getResource("Documentation/Manual.html"), SMILApp.class.getResource("Documentation/style.css"), };
  static URL[] extractImageUrls = new URL[] { SMILApp.class.getResource("Documentation/images/ControlPanel.png"),
      SMILApp.class.getResource("Documentation/images/DebugMenu.png"),
      SMILApp.class.getResource("Documentation/images/LoadStandardTapeMenu.png"),
      SMILApp.class.getResource("Documentation/images/Memory.png"),
      SMILApp.class.getResource("Documentation/images/Options.png"),
      SMILApp.class.getResource("Documentation/images/PulseLabel.png"),
      SMILApp.class.getResource("Documentation/images/Registers.png"),
      SMILApp.class.getResource("Documentation/images/SaveMemoryToTape.png"),
      SMILApp.class.getResource("Documentation/images/TapeMenu.png"),
      SMILApp.class.getResource("Documentation/images/TapeReader.png"),
      SMILApp.class.getResource("Documentation/images/Typewriter.png"),
      SMILApp.class.getResource("Documentation/images/WindowsMenu.png"), };

  public void extractDocumentation() {
    int chooserMode = fileChooser.getFileSelectionMode();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int returnVal = fileChooser.showDialog(controlPanelFrame, "Extract To");
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      final ExtractCallback cb = new ExtractCallback(controlPanelFrame);
      final File dir = fileChooser.getSelectedFile();
      Thread extractThread = new Thread() {
        public void run() {
          cb.starting(extractDocUrls.length + extractImageUrls.length);
          dir.mkdir();
          smil.copy(cb, extractDocUrls, dir);
          File imageDir = new File(dir, "images");
          imageDir.mkdir();
          smil.copy(cb, extractImageUrls, imageDir);
          cb.finished();
        }
      };
      extractThread.start();
    }
    fileChooser.setFileSelectionMode(chooserMode);
  }

  public void createAndShowGUI() {
    // Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    showControlPanel();
    showTapeReader();
    showTypewriter();
    showControlPanel();
  }


  public void deinit() {
    smil.stop();
  }

  public ValueUpdatedListener<Double> volumeUpdatedListener() {
    return new ValueUpdatedListener<Double>() {
      public void valueUpdated(Double oldValue, Double newValue) {
        soundGenerator.setVolume(newValue);
      }
    };
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    final SMILApp smil = new SMILApp();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        smil.init();
      }
    });
    // sg.stop();
  }
}
