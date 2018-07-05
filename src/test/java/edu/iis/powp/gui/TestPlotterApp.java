package edu.iis.powp.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.iis.client.plottermagic.ClientPlotter;
import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.adapter.LineAdapterPlotterDriver;
import edu.iis.powp.app.Application;
import edu.iis.powp.command.gui.AddCommandWindow;
import edu.iis.powp.command.gui.CommandEditorWindow;
import edu.iis.powp.command.gui.CommandManagerWindow;
import edu.iis.powp.command.gui.CommandManagerWindowCommandChangeObserver;
import edu.iis.powp.events.SelectLoadSecretCommandOptionListener;
import edu.iis.powp.events.SelectRunCurrentCommandOptionListener;
import edu.iis.powp.events.SelectTestFigure2OptionListener;
import edu.iis.powp.events.predefine.SelectTestFigureOptionListener;
import edu.iis.powp.features.CommandsFeature;
import edu.iis.powp.features.DrawerFeature;
import edu.kis.powp.drawer.panel.DrawPanelController;
import edu.kis.powp.drawer.shape.LineFactory;

public class TestPlotterApp {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Setup test concerning preset figures in context.
	 * 
	 * @param application
	 *            Application context.
	 */
	private static void setupPresetTests(Application application) {
		SelectTestFigureOptionListener selectTestFigureOptionListener = new SelectTestFigureOptionListener(
				application.getDriverManager());
		SelectTestFigure2OptionListener selectTestFigure2OptionListener = new SelectTestFigure2OptionListener(
				application.getDriverManager());

		application.addTest("Figure Joe 1", selectTestFigureOptionListener);
		application.addTest("Figure Joe 2", selectTestFigure2OptionListener);
	}

	/**
	 * Setup test using plotter commands in context.
	 * 
	 * @param application
	 *            Application context.
	 */
	private static void setupCommandTests(Application application) {
		application.addTest("Load secret command", new SelectLoadSecretCommandOptionListener());

		application.addTest("Run command", new SelectRunCurrentCommandOptionListener(application.getDriverManager()));

	}

	/**
	 * Setup driver manager, and set default IPlotter for application.
	 * 
	 * @param application
	 *            Application context.
	 */
	private static void setupDrivers(Application application) {
		IPlotter clientPlotter = new ClientPlotter();
		application.addDriver("Client Plotter", clientPlotter);

		DrawPanelController drawerController = DrawerFeature.getDrawerController();
		IPlotter plotter = new LineAdapterPlotterDriver(drawerController, LineFactory.getBasicLine(), "basic");
		application.addDriver("Line Simulator", plotter);
		application.getDriverManager().setCurrentPlotter(plotter);

		plotter = new LineAdapterPlotterDriver(drawerController, LineFactory.getSpecialLine(), "special");
		application.addDriver("Special line Simulator", plotter);
		application.updateDriverInfo();
	}

	private static void setupWindows(Application application) {

		CommandManagerWindow commandManager = new CommandManagerWindow(CommandsFeature.getPlotterCommandManager());
		application.addWindowComponent("Command Manager", commandManager);

		CommandManagerWindowCommandChangeObserver windowObserver = new CommandManagerWindowCommandChangeObserver(
				commandManager);
		CommandsFeature.getPlotterCommandManager().getChangePublisher().addSubscriber(windowObserver);

		CommandEditorWindow commandedit = new CommandEditorWindow(CommandsFeature.getPlotterCommandManager());
		application.addWindowComponent("Current Command Edit", commandedit);

		AddCommandWindow add = new AddCommandWindow(CommandsFeature.getPlotterCommandManager());
		application.addWindowComponent("Add Command Window", add);
	}

	/**
	 * Setup menu for adjusting logging settings.
	 * 
	 * @param application
	 *            Application context.
	 */
	private static void setupLogger(Application application) {

		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Application app = new Application();
			DrawerFeature.setupDrawerPlugin(app);
			CommandsFeature.setupCommandManager();

			setupDrivers(app);
			setupPresetTests(app);
			setupCommandTests(app);
			setupLogger(app);
			setupWindows(app);

			app.setVisibility(true);
		});
	}

}
