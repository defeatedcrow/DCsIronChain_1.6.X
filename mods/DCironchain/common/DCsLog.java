package mods.DCironchain.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

public class DCsLog {
	
	public static final Logger logger = java.util.logging.Logger.getLogger("IronChain");
	private static boolean debugMode = false;
	
	public static void loadModInfo(String modid) {
		logger.setParent(FMLLog.getLogger());
		logger.log(Level.FINER, "Succeeded to check other mod :" + modid);
	}
	
	public static void debugTrace(String msg) {
		if (debugMode) {
			logger.setParent(FMLLog.getLogger());
			logger.log(Level.FINEST, msg);
		}
	}
	
	public static void info(String msg) {
		logger.setParent(FMLLog.getLogger());
		logger.log(Level.INFO, msg);
	}

}
