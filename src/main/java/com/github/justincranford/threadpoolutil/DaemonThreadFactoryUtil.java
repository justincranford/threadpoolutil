package com.github.justincranford.threadpoolutil;

import java.util.concurrent.ThreadFactory;

@SuppressWarnings("hiding")
public final class DaemonThreadFactoryUtil implements ThreadFactory {
	public static final DaemonThreadFactoryUtil     DAEMON_THREAD_FACTORY = new DaemonThreadFactoryUtil(true);
	public static final DaemonThreadFactoryUtil NON_DAEMON_THREAD_FACTORY = new DaemonThreadFactoryUtil(false);

	private boolean isDaemonThreadFactory;

	private DaemonThreadFactoryUtil(final boolean isDaemonThreadFactory) {
		// prevent class instantiation by making constructor private
		this.isDaemonThreadFactory = isDaemonThreadFactory;
	}

	@Override
	public Thread newThread(final Runnable runnable) {
		final Thread thread = new Thread(runnable);
		thread.setDaemon(this.isDaemonThreadFactory);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	}
}