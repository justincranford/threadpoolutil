package com.github.justincranford.threadpoolutil;

public class SystemUtil {
	public static final int     SYS_AVAILABLE_CPUS      = Runtime.getRuntime().availableProcessors();	// Assume static value, even though it could change.
	public static final String  SYS_PROP_OS_NAME		= System.getProperty("os.name");
	public static final String  SYS_PROP_LINE_SEPARATOR	= System.getProperty("line.separator");
	public static final String  SYS_PROP_USER_HOME		= System.getProperty("user.home");
	public static final boolean SYS_PROP_IS_WINDOWS		= SYS_PROP_OS_NAME.toLowerCase().contains("win");

	private SystemUtil() {
		// prevent class instantiation by making constructor private
	}
}