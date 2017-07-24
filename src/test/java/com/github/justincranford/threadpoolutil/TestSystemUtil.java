package com.github.justincranford.threadpoolutil;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestSystemUtil {
	private static final String EMPTY_STRING = "";

	@Test
	public void testPrivateConstructor() throws Exception {
		ValidationUtil.assertPrivateConstructorNoParameters(SystemUtil.class, true);
	}

	@Test
	public void testAssertNonNullValues() throws Exception {
		Assert.assertNotNull(SystemUtil.SYS_PROP_OS_NAME);
		Assert.assertNotNull(SystemUtil.SYS_PROP_USER_HOME);
		Assert.assertNotEquals(EMPTY_STRING, SystemUtil.SYS_PROP_OS_NAME);
		Assert.assertNotEquals(EMPTY_STRING, SystemUtil.SYS_PROP_USER_HOME);
	}
}