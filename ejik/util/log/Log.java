package ejik.util.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

	/**
	 * 
	 *  logging subsystem
	 *
	 */
public class Log {
	
	private static Object _mutex = null;
	
	public static Object mutex() {
		if (Log._mutex == null) {
			Log._mutex = new Object();
		}
		return _mutex;
	}
	
	public static String fileName = "";
	
	private static FileWriter hSW = null;
	
	public static FileWriter getFileWriter() {
		if (hSW == null) {
			try {
				hSW = new FileWriter(Log.fileName, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return hSW;
	}
	
	/**
	 * 
	 * Proceed end-point write
	 * @param s String to be written 
	 */
	private static void write(String s) {
		synchronized (Log.mutex()) {
			System.out.println(s);
			if (!Log.fileName.equals("")) {
				try {
					FileWriter sw = Log.getFileWriter();
					sw.write(s + "\n");
				    //sw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String getDate() {
		return (new Date()).toString() + ", " + Thread.currentThread().getName();
	}
	
	private static void write(String s, StackTraceElement stack[]) {
		Log.write(s);
		Log.writeStackTrace(stack);
	}
	
	private static void writeStackTrace(StackTraceElement stack[]) {
		for (StackTraceElement stackEl : stack) {
			Log.write(stackEl.toString());
		}
	}
	
	/**
	 * <b>public static void debug(String s)</b></br>
	 * Writes to log on debug level
	 * @param s
	 * 	Message to be written
	 */
	synchronized
	public static void debug(String s) {
		Log.write("Debug (" + getDate() + "): " + s);
	}
	
	/**
	 * <b>public static void log(String s)</b></br>
	 * Writes to log on log level
	 * @param s
	 * 	Message to be written
	 */
	synchronized
	public static void log(String s) {
		Log.write("Log   (" + getDate() + "): " + s);
	}
	
	/**
	 * <b>public static void warning(String s)</b></br>
	 * Writes to log on warning level
	 * @param s
	 * 	Message to be written
	 */
	synchronized
	public static void warning(String s) {
		Log.write("Warning   (" + getDate() + "): " + s);
	}
	
	/**
	 * <b>public static void error(String s)</b></br>
	 * Writes to log on error level
	 * @param s
	 * 	Message to be written
	 */
	synchronized
	public static void error(String s) {
		Log.write("Error (" + getDate() + "): " + s);
	}
	
	/**
	 * <b>public static void error(String s)</b></br>
	 * Writes to log on error level
	 * @param s
	 * 	Message to be written
	 */
	synchronized
	public static void error(Exception e) {
		Log.write("ErrorX(" + getDate() + "): " + e.toString(), e.getStackTrace());
	}

}
