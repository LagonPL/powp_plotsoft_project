package edu.iis.powp.command.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;

import edu.iis.powp.app.gui.WindowComponent;
import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;
import edu.iis.powp.command.manager.PlotterCommandManager;

public class AddCommandWindow extends JFrame implements WindowComponent {

	private Container content;
	private PlotterCommandManager commandManager;
	private JList list;
	private GridBagConstraints c;
	private String commandName;
	private int posX, posY;
	private JTextField posXField;
	private JTextField posYField;

	public AddCommandWindow(PlotterCommandManager commandManager) {
		this.commandManager = commandManager;
		setupContainerUIComponents();

	}

	private void setupContainerUIComponents() {
		this.setTitle("Command Editor");
		this.setSize(400, 800);
		content = this.getContentPane();
		content.setLayout(new GridBagLayout());

		c = new GridBagConstraints();

		JButton btnAddCommand = new JButton("Add Command");
		btnAddCommand.addActionListener((ActionEvent e) -> this.addCommand());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;

		content.add(btnAddCommand, c);

		posXField = new JTextField();
		posYField = new JTextField();

		content.add(posXField, c);
		content.add(posYField, c);

		String[] commands = { "DrawToCommand", "SetPositionCommand" };
		list = new JList(commands);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		content.add(list, c);
	}

	public IPlotterCommand addCommand() {
		posX = Integer.parseInt(posXField.getText());
		posY = Integer.parseInt(posYField.getText());
		if (list.getSelectedValue() == "DrawToCommand") {
			System.out.println(new DrawToCommand(posX, posY).toString());
			return new DrawToCommand(posX, posY);
		} else if (list.getSelectedValue() == "SetPositionCommand") {
			System.out.println(new SetPositionCommand(posX, posY).toString());
			return new SetPositionCommand(posX, posY);
		} else
			return null;
	}

	@Override
	public void HideIfVisibleAndShowIfHidden() {
		if (this.isVisible()) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
		}
	}
}
