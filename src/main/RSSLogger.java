package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import RSS.*;
import RSS.filer.*;
import controller.*;

/**
 * Class for controlling log process. Contains static link to loggers
 * Create three log files: log/RSS.log, log/Files.log, log/Main.log
 * Set log level to ALL for all. 
 * @author Nicolay Klimchuk
 */

public class RSSLogger {
	// package RSS
	public static Logger loggerRSSFile = Logger.getLogger(RSSFiler.class.getName());
	public static Logger loggerRSSNet = Logger.getLogger(RSSNet.class.getName());
	public static Logger loggerRSSThread = Logger.getLogger(RSSThread.class.getName());
	// package RSS.filer
	public static Logger loggerRSSFilerCrypt = Logger.getLogger(Crypt.class.getName());
	public static Logger loggerRSSFilerReader = Logger.getLogger(RSSFilesReader.class.getName());
	public static Logger loggerRSSFilerWriter = Logger.getLogger(RSSFileWriter.class.getName());
	public static Logger loggerRSSFilerServer = Logger.getLogger(RSSServerFile.class.getName());
	public static Logger loggerRSSFilerTreeFile = Logger.getLogger(TreeFile.class.getName());
	// package Controller
	public static Logger loggerController = Logger.getLogger(Controller.class.getName());
	public static Logger loggerControllerSchedule = Logger.getLogger(Schedule.class.getName());
	// package main
	public static Logger loggerMain = Logger.getLogger(Main.class.getName());
	// handlers
	private static Handler handlerRSS;
	private static Handler handlerRSSFiler;
	private static Handler handlerMain;
	static{
		try {
			handlerRSS = new FileHandler("log/RSS.log");
			handlerRSSFiler = new FileHandler("log/Files.log");
			handlerMain = new FileHandler("log/Main.log");
		} catch (SecurityException e) {
			loggerMain.throwing("FileHandler",
								"new FileHandler(\"*.log\");", 
								e);
			//e.printStackTrace();
		} catch (IOException e) {
			loggerMain.throwing("FileHandler",
					"new FileHandler(\"*.log\");", 
					e);
			//e.printStackTrace();
		}
		
		handlerRSS.setFormatter(new SimpleFormatter());
		handlerRSSFiler.setFormatter(new SimpleFormatter());
		handlerMain.setFormatter(new SimpleFormatter());
		
		handlerRSS.setLevel(Level.ALL);
		handlerRSSFiler.setLevel(Level.ALL);
		handlerMain.setLevel(Level.ALL);
		
		loggerMain.setLevel(Level.ALL);
		loggerController.setLevel(Level.ALL);
		loggerControllerSchedule.setLevel(Level.ALL);
		
		loggerRSSNet.setLevel(Level.ALL);
		loggerRSSNet.addHandler(handlerRSS);
		loggerRSSThread.setLevel(Level.ALL);
		loggerRSSThread.addHandler(handlerRSS);
		loggerRSSFile.setLevel(Level.ALL);
		loggerRSSFile.addHandler(handlerRSS);
		
		loggerRSSFilerCrypt.setLevel(Level.ALL);
		loggerRSSFilerCrypt.addHandler(handlerRSS);
		loggerRSSFilerReader.setLevel(Level.ALL);
		loggerRSSFilerReader.addHandler(handlerRSS);
		loggerRSSFilerWriter.setLevel(Level.ALL);
		loggerRSSFilerWriter.addHandler(handlerRSS);
		loggerRSSFilerServer.setLevel(Level.ALL);
		loggerRSSFilerServer.addHandler(handlerRSS);
		loggerRSSFilerTreeFile.setLevel(Level.ALL);
		loggerRSSFilerTreeFile.addHandler(handlerRSS);
		
	//	Logger.getLogger("RSS").addHandler(handlerRSS);
		Logger.getLogger("RSS.filer").addHandler(handlerRSSFiler);
		
		loggerController.addHandler(handlerMain);
		loggerControllerSchedule.addHandler(handlerMain);
		loggerMain.addHandler(handlerMain);
		
	}
}
