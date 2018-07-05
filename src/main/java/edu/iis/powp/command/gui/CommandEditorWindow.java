package edu.iis.powp.command.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.text.AbstractDocument.Content;

import edu.iis.powp.app.gui.WindowComponent;
import edu.iis.powp.command.ICompoundCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;
import edu.iis.powp.command.complex.ComplexCommand;
import edu.iis.powp.command.manager.PlotterCommandManager;
import edu.iis.powp.observer.Subscriber;

public class CommandEditorWindow extends JFrame implements WindowComponent {

	private static final long serialVersionUID = 9204679248304669948L;
	private PlotterCommandManager commandManager;
	private DefaultListModel model;
	private IPlotterCommand currentCommand;
	private int commandCount = 0;
	private Container content;
	private JList list;
	private GridBagConstraints c;
	private List<String> commands;

	public CommandEditorWindow(PlotterCommandManager commandManager) {
		this.commandManager = commandManager;
		setupContainerUIComponents();

	}

	private void setupContainerUIComponents() {
		this.setTitle("Command Editor");
		this.setSize(400, 800);
		content = this.getContentPane();
		content.setLayout(new GridBagLayout());
		this.commandManager = commandManager;

		c = new GridBagConstraints();

		JButton btnMoveUP = new JButton("Move Up");
		btnMoveUP.addActionListener((ActionEvent e) -> this.moveUp());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		c.weighty = 1;

		content.add(btnMoveUP, c);

		JButton btnMoveDown = new JButton("Move Down");
		btnMoveDown.addActionListener((ActionEvent e) -> this.moveDown());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		c.weighty = 1;
		content.add(btnMoveDown, c);

		JButton btnRemoveCommand = new JButton("Remove");
		btnRemoveCommand.addActionListener((ActionEvent e) -> this.removeCommand());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;

		content.add(btnRemoveCommand, c);

		JButton btnLoadCommand = new JButton("Load Command");
		btnLoadCommand.addActionListener((ActionEvent e) -> this.loadCommand());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;
		
		content.add(btnLoadCommand, c);

		JButton btnAddCommand = new JButton("Add Command");
		btnAddCommand.addActionListener((ActionEvent e) -> this.addCommand());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 0;

		content.add(btnAddCommand, c);

		String[] selections = { "init", "init", "init", "init", "init", "init", "init" };
		list = new JList(selections);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		content.add(list, c);
	}

	// TODO WAŻNE!!!
	private void addCommand() {
		AddCommandWindow temp = new AddCommandWindow(commandManager);
		
		//IPlotterCommand buff = iterator.next();\
		//commands.add("Command " + ++count + ": " + buff.toString());
		int selected = list.getSelectedIndex();
		if (selected > 0) {
			//ComplexCommand currentCommand = (ComplexCommand) commandManager.getCurrentCommand();
			IPlotterCommand buff = temp.addCommand();
			commands.add("Command " + 3 + ": ");
			System.out.println("test");
			//currentCommand.addCommand(selected + 1, temp.addCommand());
		}
		content.remove(list);
		list = new JList(commands.toArray());
		content.add(list, c);
		content.revalidate();
		list.setSelectedIndex(selected - 1);
	}

	private void moveUp() {
		int selected = list.getSelectedIndex();
		if (selected > 0) {
			Collections.swap(commands, selected, selected - 1);
			ComplexCommand currentCommand = (ComplexCommand) commandManager.getCurrentCommand();
			currentCommand.changeSequence(selected - 1, selected);
		}
		content.remove(list);
		list = new JList(commands.toArray());
		content.add(list, c);
		content.revalidate();
		list.setSelectedIndex(selected - 1);
	}

	private void moveDown() {
		int selected = list.getSelectedIndex();
		if (selected >= 0) {
			Collections.swap(commands, selected, selected + 1);
			ComplexCommand currentCommand = (ComplexCommand) commandManager.getCurrentCommand();
			currentCommand.changeSequence(selected + 1, selected);
		}
		content.remove(list);
		list = new JList(commands.toArray());
		content.add(list, c);
		content.revalidate();
		list.setSelectedIndex(selected + 1);
	}

	private void removeCommand() {
		int selected = list.getSelectedIndex();
		if (selected >= 0) {
			commands.remove(selected);
			ComplexCommand currentCommand = (ComplexCommand) commandManager.getCurrentCommand();
			currentCommand.removeCommand(selected);
			commandCount--;
		}
		content.remove(list);
		list = new JList(commands.toArray());
		content.add(list, c);
		content.revalidate();
	}

	private void loadCommand() {
		commandManager.setCurrentCommand(new ComplexCommand(commandManager.getCurrentCommand()));
		loadSimpleCommands(commandManager);
	}

	private void loadSimpleCommands(PlotterCommandManager commandManager) {
		ComplexCommand currentCommand = (ComplexCommand) commandManager.getCurrentCommand();
		if (currentCommand != null) {
			commands = new ArrayList<String>();
			commandCount = 0;
			int count = 0;
			Iterator<IPlotterCommand> iterator = currentCommand.iterator();
			content.remove(list);
			while (iterator.hasNext()) {
				IPlotterCommand buff = iterator.next();
				content.remove(list);
				commands.add("Command " + ++count + ": " + buff.toString());
				commandCount++;
			}
			list = new JList(commands.toArray());
			content.add(list, c);
			content.revalidate();
		}
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
