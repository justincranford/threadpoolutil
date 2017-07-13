package com.github.justincranford.threadpoolutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestDaemonThreadFactoryUtil {
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Constructor<?> constructor = DaemonThreadFactoryUtil.class.getDeclaredConstructor();	// throws exception if zero-parameter constructor is not found
		if (!Modifier.isPrivate(constructor.getModifiers())) {
			throw new IllegalArgumentException("Constructor must be private.");
		}
		constructor.setAccessible(true);
		constructor.newInstance();	// ASSUMPTION: Never returns null, but could throw exception. We need to execute it to rule out an exception, and for code coverage reporting.
	}

	@Test
	public void testNewThreadSettings() throws Exception {
		final Thread newThread = DaemonThreadFactoryUtil.DAEMON_THREAD_FACTORY.newThread((Runnable)null);
		Assert.assertTrue(newThread.isDaemon());
		Assert.assertEquals(Thread.MIN_PRIORITY, newThread.getPriority());
	}
}