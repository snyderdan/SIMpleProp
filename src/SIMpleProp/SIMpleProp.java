package SIMpleProp;

import javax.swing.JFrame;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class SIMpleProp extends JFrame {
	public SIMpleProp() {
		setTitle("SIMpleProp - Parallax Propeller Simulator");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 9;
		gbc_panel.gridheight = 22;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		getContentPane().add(panel, gbc_panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 22;
		gbc_tabbedPane.gridwidth = 25;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 9;
		gbc_tabbedPane.gridy = 1;
		getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("COG 0", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 0;
		gbc_rigidArea.gridy = 0;
		panel_1.add(rigidArea, gbc_rigidArea);
		
		JButton btnStepClock = new JButton("Step Clock");
		GridBagConstraints gbc_btnStepClock = new GridBagConstraints();
		gbc_btnStepClock.insets = new Insets(0, 0, 5, 5);
		gbc_btnStepClock.gridx = 1;
		gbc_btnStepClock.gridy = 0;
		panel_1.add(btnStepClock, gbc_btnStepClock);
		
		JButton btnStepInstruction = new JButton("Step Instruction");
		GridBagConstraints gbc_btnStepInstruction = new GridBagConstraints();
		gbc_btnStepInstruction.insets = new Insets(0, 0, 5, 5);
		gbc_btnStepInstruction.gridx = 2;
		gbc_btnStepInstruction.gridy = 0;
		panel_1.add(btnStepInstruction, gbc_btnStepInstruction);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_2 = new GridBagConstraints();
		gbc_rigidArea_2.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_2.gridx = 3;
		gbc_rigidArea_2.gridy = 0;
		panel_1.add(rigidArea_2, gbc_rigidArea_2);
		
		JLabel lblUnits = new JLabel("Units:");
		GridBagConstraints gbc_lblUnits = new GridBagConstraints();
		gbc_lblUnits.insets = new Insets(0, 0, 5, 5);
		gbc_lblUnits.anchor = GridBagConstraints.EAST;
		gbc_lblUnits.gridx = 4;
		gbc_lblUnits.gridy = 0;
		panel_1.add(lblUnits, gbc_lblUnits);
		
		String[] options = {"Hex", "Dec", "Bin"};
		JComboBox comboBox = new JComboBox(options);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.gridx = 5;
		gbc_comboBox.gridy = 0;
		panel_1.add(comboBox, gbc_comboBox);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_1.gridx = 6;
		gbc_rigidArea_1.gridy = 0;
		panel_1.add(rigidArea_1, gbc_rigidArea_1);
		
		JLabel lblPc = new JLabel("PC: 0x000");
		GridBagConstraints gbc_lblPc = new GridBagConstraints();
		gbc_lblPc.anchor = GridBagConstraints.WEST;
		gbc_lblPc.insets = new Insets(0, 0, 5, 5);
		gbc_lblPc.gridx = 7;
		gbc_lblPc.gridy = 0;
		panel_1.add(lblPc, gbc_lblPc);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		panel_1.add(scrollPane, gbc_scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("COG 1", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_3 = new GridBagConstraints();
		gbc_rigidArea_3.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_3.gridx = 0;
		gbc_rigidArea_3.gridy = 0;
		panel_2.add(rigidArea_3, gbc_rigidArea_3);
		
		JButton button = new JButton("Step Clock");
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 0;
		panel_2.add(button, gbc_button);
		
		JButton button_1 = new JButton("Step Instruction");
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 5);
		gbc_button_1.gridx = 2;
		gbc_button_1.gridy = 0;
		panel_2.add(button_1, gbc_button_1);
		
		Component rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_4 = new GridBagConstraints();
		gbc_rigidArea_4.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_4.gridx = 3;
		gbc_rigidArea_4.gridy = 0;
		panel_2.add(rigidArea_4, gbc_rigidArea_4);
		
		JLabel label = new JLabel("Units:");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 4;
		gbc_label.gridy = 0;
		panel_2.add(label, gbc_label);
		
		JComboBox comboBox_1 = new JComboBox(options);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.anchor = GridBagConstraints.WEST;
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.gridx = 5;
		gbc_comboBox_1.gridy = 0;
		panel_2.add(comboBox_1, gbc_comboBox_1);
		
		Component rigidArea_5 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_5 = new GridBagConstraints();
		gbc_rigidArea_5.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_5.gridx = 6;
		gbc_rigidArea_5.gridy = 0;
		panel_2.add(rigidArea_5, gbc_rigidArea_5);
		
		JLabel label_1 = new JLabel("PC: 0x000");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 7;
		gbc_label_1.gridy = 0;
		panel_2.add(label_1, gbc_label_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 7;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 1;
		panel_2.add(scrollPane_1, gbc_scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		
		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("HUBRAM", null, panel_9, null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SIMpleProp frame = new SIMpleProp();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
}
