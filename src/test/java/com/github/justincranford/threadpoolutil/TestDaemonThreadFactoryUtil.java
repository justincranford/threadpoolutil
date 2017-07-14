package com.github.justincranford.threadpoolutil;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestDaemonThreadFactoryUtil {
	@Test
	public void testNewDaemonThreadSettings() throws Exception {
		final Runnable r = null;
		final Thread newThread = DaemonThreadFactoryUtil.DAEMON_THREAD_FACTORY.newThread(r);
		Assert.assertTrue(newThread.isDaemon());
		Assert.assertEquals(Thread.MIN_PRIORITY, newThread.getPriority());
	}

	@Test
	public void testNewNonDaemonThreadSettings() throws Exception {
		final Runnable r = null;
		final Thread newThread = DaemonThreadFactoryUtil.NON_DAEMON_THREAD_FACTORY.newThread(r);
		Assert.assertFalse(newThread.isDaemon());
		Assert.assertEquals(Thread.MIN_PRIORITY, newThread.getPriority());
	}
}