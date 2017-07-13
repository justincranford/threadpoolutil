package com.github.justincranford.threadpoolutil;

import java.util.concurrent.ThreadFactory;

public final class DaemonThreadFactoryUtil implements ThreadFactory {	// NOPMD
	public static final DaemonThreadFactoryUtil DAEMON_THREAD_FACTORY = new DaemonThreadFactoryUtil();	// NOPMD

	private DaemonThreadFactoryUtil() {
		// prevent class instantiation by making constructor private
	}

	@Override
	public Thread newThread(final Runnable runnable) {	// NOPMD
		final Thread thread = new Thread(runnable);	// NOPMD
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	}
}