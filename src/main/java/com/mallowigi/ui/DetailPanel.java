package com.mallowigi.ui;

/*
 * Color Browser - Color browser plugin for IDEA
 * Copyright (C) 2006 Rick Maddy. All Rights Reserved.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.SupportCode;
import com.mallowigi.utils.ColorInfo;
import com.mallowigi.utils.EditorUtil;
import org.jdom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailPanel {
  private final ColorBrowserToolWindow toolWindow;
  private final ColorNameButtonListener colorNameButtonListener;
  private boolean isAdjusting = false;
  private int indexHtml;
  private int indexColor;
  private int indexColorUIResource;
  private JPanel pnlMain;
  private JButton btnHtmlName;
  private JLabel lblHSL;
  private JLabel lblHSB;
  private JLabel lblRGB;
  private JPanel pnlColor;
  private JButton btnColorName;
  private JButton btnCursor;
  private JButton btnColor;
  private JComboBox cbColor;
  private JButton btnHtml;
  private JComboBox cbHtml;
  private JComboBox cbColorUIResource;
  private JButton btnColorUIResource;
  private JButton btnColorUIResourceName;
  private JCheckBox cbHexCase;

  {
    // GUI initializer generated by IntelliJ IDEA GUI Designer
    // >>> IMPORTANT!! <<<
    // DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  DetailPanel(final Project project, final ColorBrowserToolWindow window) {
    toolWindow = window;

    colorNameButtonListener = new ColorNameButtonListener(project);
    setupControls(project);
  }

  public void setColor(final Color color) {
    if (color == null) {
      disable();
      return;
    }

    isAdjusting = true;

    final ColorInfo info = new ColorInfo(color);
    pnlColor.setBackground(color);

    lblRGB.setText(info.getTxtRGB());
    lblHSB.setText(info.getTxtHSB());
    lblHSL.setText(info.getTxtHSL());

    String name = info.getSVGName();
    if (name != null && name.length() > 0) {
      btnHtmlName.setText(name);
      btnHtmlName.setEnabled(true);
    } else {
      btnHtmlName.setText("<None>");
      btnHtmlName.setEnabled(false);
    }

    name = info.getJavaName();
    if (name != null && name.length() > 0) {
      btnColorName.setText("Color." + name);
      btnColorName.setEnabled(true);
      btnColorUIResourceName.setText("ColorUIResource." + name);
      btnColorUIResourceName.setEnabled(true);
    } else {
      btnColorName.setText("<None>");
      btnColorName.setEnabled(false);
      btnColorUIResourceName.setText("<None>");
      btnColorUIResourceName.setEnabled(false);
    }

    cbHtml.setEnabled(true);
    btnHtml.setEnabled(true);
    cbHtml.setModel(new DefaultComboBoxModel(new String[]{
      info.getShortHex(),
      info.getHex(),
      info.getDecimalRGB(),
      info.getDecimalRGBa(),
      info.getPercentRGB(),
      info.getPercentRGBa(),
      info.getHsl(),
      info.getHsla()
    }));
    cbHtml.setSelectedIndex(indexHtml);

    cbColor.setEnabled(true);
    btnColor.setEnabled(true);
    cbColor.setModel(new DefaultComboBoxModel(new String[]{
      info.getColorFFF(),
      info.getColorFFFF(),
      info.getColorI(),
      info.getColorIB(),
      info.getColorIII(),
      info.getColorIIII()
    }));
    cbColor.setSelectedIndex(indexColor);

    cbColorUIResource.setEnabled(true);
    btnColorUIResource.setEnabled(true);
    cbColorUIResource.setModel(new DefaultComboBoxModel(new String[]{
      info.getColorUIResourceFFF(),
      info.getColorUIResourceI(),
      info.getColorUIResourceIII()
    }));
    cbColorUIResource.setSelectedIndex(indexColorUIResource);

    isAdjusting = false;
  }

  JComponent getMainComponent() {
    return pnlMain;
  }

  void disable() {
    pnlColor.setBackground(pnlMain.getParent().getBackground());
    lblRGB.setText("<None>");
    lblHSB.setText("<None>");
    lblHSL.setText("<None>");

    cbHtml.setEnabled(false);
    btnHtml.setEnabled(false);
    btnHtmlName.setText("<None>");
    btnHtmlName.setEnabled(false);

    cbColor.setEnabled(false);
    btnColor.setEnabled(false);
    btnColorName.setText("<None>");
    btnColorName.setEnabled(false);

    cbColorUIResource.setEnabled(false);
    btnColorUIResource.setEnabled(false);
    btnColorUIResourceName.setText("<None>");
    btnColorUIResourceName.setEnabled(false);
  }

  void initComponent() {
    isAdjusting = true;
    cbHtml.setSelectedIndex(indexHtml);
    cbColor.setSelectedIndex(indexColor);
    cbColorUIResource.setSelectedIndex(indexColorUIResource);
    isAdjusting = false;
  }

  void readExternal(final Element element) throws InvalidDataException {

    String index = null;
    Element elem = element.getChild("indexHtml");
    if (elem != null) {
      index = elem.getValue();
    }
    if (index != null && index.length() > 0) {
      indexHtml = Integer.parseInt(index);
    }

    elem = element.getChild("indexColor");
    if (elem != null) {
      index = elem.getValue();
    }
    if (index != null && index.length() > 0) {
      indexColor = Integer.parseInt(index);
    }

    elem = element.getChild("indexColorUIResource");
    if (elem != null) {
      index = elem.getValue();
    }
    if (index != null && index.length() > 0) {
      indexColorUIResource = Integer.parseInt(index);
    }

    elem = element.getChild("hexUppercase");
    if (elem != null) {
      final String hex = elem.getValue();
      cbHexCase.setSelected("Y".equals(hex));
      ColorInfo.setHexUppercase("Y".equals(hex));
    }
  }

  void writeExternal(final Element element) throws WriteExternalException {

    Element index = new Element("indexHtml");
    index.addContent(Integer.toString(indexHtml));
    element.addContent(index);

    index = new Element("indexColor");
    index.addContent(Integer.toString(indexColor));
    element.addContent(index);

    index = new Element("indexColorUIResource");
    index.addContent(Integer.toString(indexColorUIResource));
    element.addContent(index);

    final Element hex = new Element("hexUppercase");
    hex.addContent(cbHexCase.isSelected() ? "Y" : "N");
    element.addContent(hex);
  }

  private void setupControls(final Project project) {
    btnCursor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        toolWindow.showAtCursor();
      }
    });

    cbHexCase.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        ColorInfo.setHexUppercase(cbHexCase.isSelected());
        toolWindow.resetColor();
      }
    });

    cbHtml.setModel(new DefaultComboBoxModel(new String[]{"",
      "",
      "",
      "",
      "",
      "",
      "",
      ""}));
    cbColor.setModel(new DefaultComboBoxModel(new String[]{"",
      "",
      "",
      "",
      "",
      ""}));
    cbColorUIResource.setModel(new DefaultComboBoxModel(new String[]{"",
      "",
      ""}));

    cbHtml.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        if (!isAdjusting) {
          indexHtml = cbHtml.getSelectedIndex();
        }
      }
    });
    cbColor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        if (!isAdjusting) {
          indexColor = cbColor.getSelectedIndex();
        }
      }
    });
    cbColorUIResource.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent actionEvent) {
        if (!isAdjusting) {
          indexColorUIResource = cbColorUIResource.getSelectedIndex();
        }
      }
    });

    btnHtmlName.addActionListener(colorNameButtonListener);
    btnHtml.addActionListener(new ColorButtonListener(project, cbHtml));
    btnColorName.addActionListener(colorNameButtonListener);
    btnColor.addActionListener(new ColorButtonListener(project, cbColor));
    btnColorUIResourceName.addActionListener(colorNameButtonListener);
    btnColorUIResource.addActionListener(new ColorButtonListener(project, cbColorUIResource));
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
   * code!
   */
  private void $$$setupUI$$$() {
    pnlMain = new JPanel();
    pnlMain.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    pnlMain.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH,
      GridConstraints.FILL_HORIZONTAL,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 2, 2), -1, -1));
    panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null));
    panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Color Details"));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel2.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
    final JLabel label1 = new JLabel();
    label1.setText("RGB:");
    panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JLabel label2 = new JLabel();
    label2.setText("HSB:");
    panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JLabel label3 = new JLabel();
    label3.setText("HSL:");
    panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    lblRGB = new JLabel();
    lblRGB.setText("");
    panel3.add(lblRGB, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    lblHSB = new JLabel();
    lblHSB.setText("");
    panel3.add(lblHSB, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    lblHSL = new JLabel();
    lblHSL.setText("");
    panel3.add(lblHSL, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    pnlColor = new JPanel();
    panel2.add(pnlColor, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(50, -1),
      new Dimension(50, -1), null));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_BOTH,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
    btnCursor = new JButton();
    btnCursor.setText("Cursor");
    btnCursor.setMnemonic(83);
    SupportCode.setDisplayedMnemonicIndex(btnCursor, 3);
    btnCursor.setToolTipText("Display the color around the editor cursor");
    panel4.add(btnCursor, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST,
      GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    cbHexCase = new JCheckBox();
    cbHexCase.setText("Hex Uppercase");
    panel4.add(cbHexCase, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
    pnlMain.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
    btnHtml = new JButton();
    btnHtml.setText("Insert -->");
    panel5.add(btnHtml, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    cbHtml = new JComboBox();
    panel5.add(cbHtml, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    btnHtmlName = new JButton();
    btnHtmlName.setText("Name");
    btnHtmlName.setToolTipText("Insert button label text into current editor");
    panel5.add(btnHtmlName, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    btnColor = new JButton();
    btnColor.setText("Insert -->");
    panel5.add(btnColor, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    cbColor = new JComboBox();
    panel5.add(cbColor, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    btnColorName = new JButton();
    btnColorName.setText("Color.name");
    btnColorName.setToolTipText("Insert button label text into current editor");
    panel5.add(btnColorName, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    btnColorUIResource = new JButton();
    btnColorUIResource.setText("Insert -->");
    panel5.add(btnColorUIResource, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST,
      GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    cbColorUIResource = new JComboBox();
    panel5.add(cbColorUIResource, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST,
      GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
      null, null));
    btnColorUIResourceName = new JButton();
    btnColorUIResourceName.setText("Color.name");
    panel5.add(btnColorUIResourceName, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST,
      GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
      GridConstraints.SIZEPOLICY_FIXED, null, null, null));
  }

  private static class ColorButtonListener implements ActionListener {
    private final Project project;
    private final JComboBox cb;

    ColorButtonListener(final Project project, final JComboBox cb) {
      this.project = project;
      this.cb = cb;
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
      final String label = (String) cb.getSelectedItem();

      EditorUtil.insertColor(label, project);
      EditorUtil.refocusEditor(project);
    }
  }

  private static class ColorNameButtonListener implements ActionListener {
    private final Project project;

    ColorNameButtonListener(final Project project) {
      this.project = project;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final JButton btn = (JButton) e.getSource();
      final String label = btn.getText();

      EditorUtil.insertColor(label, project);
      EditorUtil.refocusEditor(project);
    }
  }
}
