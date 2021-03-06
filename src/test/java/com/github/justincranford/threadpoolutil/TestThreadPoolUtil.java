package com.github.justincranford.threadpoolutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings({"static-method","unused","hiding","synthetic-access"})
public class TestThreadPoolUtil {
	private static final Logger LOG = Logger.getLogger(TestThreadPoolUtil.class.getName());
	private static final Method CLASS_SLEEP;
	private static final Method CLASS_GETTER;
	private static final Method CLASS_SETTER;
	private static final Method INSTANCE_GETTER;
	private static final Method INSTANCE_SETTER;
	static {
		Method classSleep		= null;
		Method classGetter		= null;
		Method classSetter		= null;
		Method instancegetter	= null;
		Method instanceSetter	= null;
		try {
			classSleep		= TestHelperClass.class.getMethod("sleep", Long.class);
			classGetter		= TestHelperClass.class.getMethod("getClassValue");
			classSetter		= TestHelperClass.class.getMethod("setClassValue", Integer.class);
			instancegetter	= TestHelperClass.class.getMethod("getInstanceValue");
			instanceSetter	= TestHelperClass.class.getMethod("setInstanceValue", Integer.class);
		} catch (Exception e) {
			// do nothing
		}
		CLASS_SLEEP		= classSleep;
		CLASS_GETTER	= classGetter;
		CLASS_SETTER	= classSetter;
		INSTANCE_GETTER = instancegetter;
		INSTANCE_SETTER = instanceSetter;
	}

	private static final Integer EXPECTED_INITIAL_11	= Integer.valueOf(11);
	private static final Integer EXPECTED_INITIAL_12	= Integer.valueOf(12);
	private static final Integer EXPECTED_INITIAL_13	= Integer.valueOf(13);
	private static final Integer EXPECTED_INITIAL_14	= Integer.valueOf(14);
	private static final Integer EXPECTED_INITIAL_15	= Integer.valueOf(15);
	private static final Integer EXPECTED_NEW_21		= Integer.valueOf(21);
	private static final Integer EXPECTED_NEW_22		= Integer.valueOf(22);
	private static final Integer EXPECTED_NEW_23		= Integer.valueOf(23);
	private static final Integer EXPECTED_NEW_24		= Integer.valueOf(24);
	private static final Integer EXPECTED_NEW_25		= Integer.valueOf(25);

	private static ThreadPoolUtil THREAD_POOL_UTIL;

	@BeforeClass
	public static void beforeClass() throws Exception {
		LOG.log(Level.INFO, "beforeClass()");
		Assert.assertNotNull("Method TestClassWithTestMethods.getClassValue() not found",			CLASS_SLEEP);
		Assert.assertNotNull("Method TestClassWithTestMethods.getClassValue() not found",			CLASS_GETTER);
		Assert.assertNotNull("Method TestClassWithTestMethods.setClassValue(String) not found",		CLASS_SETTER);
		Assert.assertNotNull("Method TestClassWithTestMethods.getInstanceValue() not found",		INSTANCE_GETTER);
		Assert.assertNotNull("Method TestClassWithTestMethods.setInstanceValue(String) not found",	INSTANCE_SETTER);
		TestThreadPoolUtil.THREAD_POOL_UTIL = new ThreadPoolUtil();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		LOG.log(Level.INFO, "afterClass()");
		TestThreadPoolUtil.THREAD_POOL_UTIL.getExecutorService().shutdown();
		TestThreadPoolUtil.THREAD_POOL_UTIL = null;
	}

	@Before
	public void before() throws Exception {
		LOG.log(Level.INFO, "beforeTest()");
		TestHelperClass.setClassValue(Integer.valueOf(0));	// clear out static value
	}

	@Test
	public void testPublicConstructor() throws Exception {
		ValidationUtil.assertPublicConstructorNoParameters(ThreadPoolUtil.class, true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorValidateBadNumThreads() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(-1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorValidateBadNumThreads2() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(true, -1);
	}

	@Test
	public void testConstructorValidateGoodNumThreads2() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(true, 1);
		threadPoolUtil.getExecutorService().shutdown();
	}

	@Test
	public void testConstructorValidateNonDaemon() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(false);
		threadPoolUtil.getExecutorService().shutdown();
	}

	@Test
	public void testConstructorValidateNonDaemon2() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(false, 1);
		threadPoolUtil.getExecutorService().shutdown();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorValidateNullExecutorService() throws Exception {
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(null);
	}

	@Test
	public void testConstructorValidateExecutorService() throws Exception {
		final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
		final ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(newFixedThreadPool);
		newFixedThreadPool.shutdown();
	}

	@Test(expected=NullPointerException.class)
	public void testInvokeAllNullList() throws Exception {
		ThreadPoolUtil.LOG.setLevel(Level.OFF);
		try {
			TestThreadPoolUtil.THREAD_POOL_UTIL.invokeAll(null);
		} finally {
			ThreadPoolUtil.LOG.setLevel(Level.INFO);
		}
	}

	@Test(expected=NullPointerException.class)
	public void testStaticInvokeAllNullList() throws Exception {
		ThreadPoolUtil.LOG.setLevel(Level.OFF);
		try {
			ThreadPoolUtil.invokeAll(TestThreadPoolUtil.THREAD_POOL_UTIL.getExecutorService(), null);
		} finally {
			ThreadPoolUtil.LOG.setLevel(Level.INFO);
		}
	}

	@Test
	public void testInvokeAllEmptyList() throws Exception {
		TestThreadPoolUtil.THREAD_POOL_UTIL.invokeAll(new ArrayList<Object>(0));
	}

	@Test
	public void testStaticInvokeAllEmptyList() throws Exception {
		ThreadPoolUtil.invokeAll(TestThreadPoolUtil.THREAD_POOL_UTIL.getExecutorService(), new ArrayList<Object>(0));
	}

	@Test(expected=NullPointerException.class)
	public void testGetResultsNullList() throws Exception {
		ThreadPoolUtil.LOG.setLevel(Level.OFF);
		try {
			ThreadPoolUtil.getResults(null);
		} finally {
			ThreadPoolUtil.LOG.setLevel(Level.INFO);
		}
	}

	@Test
	public void testGetResultsEmptyList() throws Exception {
		ThreadPoolUtil.getResults(new ArrayList<Future<Object>>(0));
	}

	@Test(expected=Exception.class)
	public void testGenericTaskNullMethod() throws Exception {
		ThreadPoolUtil.LOG.setLevel(Level.OFF);
		try {
			new ThreadPoolUtil.InvokeViaReflectionTask(null, null, null).call();
		} finally {
			ThreadPoolUtil.LOG.setLevel(Level.INFO);
		}
	}

	@Test(expected=Exception.class)
	public void testWaitForDoneBadSleepGranularity() throws Exception {
		ThreadPoolUtil.LOG.setLevel(Level.OFF);
		try {
			ThreadPoolUtil.waitForDone(TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassAsynchronous(CLASS_GETTER), 0L);
		} finally {
			ThreadPoolUtil.LOG.setLevel(Level.INFO);
		}
	}

	@Test
	public void testWaitForDone() throws Exception {
		ThreadPoolUtil.waitForDone(TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassAsynchronous(CLASS_SLEEP, new Object[]{Long.valueOf(10L)}), 1L);
	}

	@Test
	public void testStatic() throws Exception {
		{	// Invoke static method directly
			TestHelperClass.setClassValue(EXPECTED_INITIAL_11);
			final Integer actualInitialClassValue1 = TestHelperClass.getClassValue();
			Assert.assertEquals("Direct class get mismatch", EXPECTED_INITIAL_11, actualInitialClassValue1);
		}

		{	// Invoke asynchronous static method indirectly via reflection
			final Future<Object> future2 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassAsynchronous(CLASS_SETTER, new Object[]{EXPECTED_NEW_21});
			final Object returnedValue2 = future2.get();	// Wait for asynchronous operation to finish
			final Integer actualNewClassValue2 = TestHelperClass.getClassValue();
			Assert.assertEquals("Indirect class set mismatch", EXPECTED_NEW_21, actualNewClassValue2);
		}

		{	// Invoke asynchronous static method indirectly via reflection
			final Future<Object> future3 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassAsynchronous(CLASS_GETTER);
			final Object returnedValue3 = future3.get();	// Wait for asynchronous operation to finish
			final Integer actualNewClassValue3 = (Integer) returnedValue3;
			Assert.assertEquals("Indirect class get mismatch", EXPECTED_NEW_21, actualNewClassValue3);
		}

		{	// Invoke synchronous static method indirectly via reflection
			final Object returnedValue4 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassSynchronous(CLASS_SETTER, new Object[]{EXPECTED_NEW_22});
			final Integer actualNewClassValue4 = TestHelperClass.getClassValue();
			Assert.assertEquals("Indirect class set mismatch", EXPECTED_NEW_22, actualNewClassValue4);
		}

		{	// Invoke synchronous static method indirectly via reflection
			final Integer actualNewClassValue5 = (Integer) TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnClassSynchronous(CLASS_GETTER);
			Assert.assertEquals("Indirect class get mismatch", EXPECTED_NEW_22, actualNewClassValue5);
		}
	}

	@Test
	public void testInstance() throws Exception {
		final TestHelperClass instance = new TestHelperClass();

		{	// Invoke instance method directly
			instance.setInstanceValue(EXPECTED_INITIAL_11);
			final Integer actualInitialInstanceValue1 = instance.getInstanceValue();
			Assert.assertEquals("Direct instance get mismatch", EXPECTED_INITIAL_11, actualInitialInstanceValue1);
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			final Future<Object> future2 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceAsynchronous(INSTANCE_SETTER, instance, new Object[]{EXPECTED_NEW_21});
			final Object returnedValue2 = future2.get();	// Wait for asynchronous operation to finish
			final Integer actualNewInstanceValue2 = instance.getInstanceValue();
			Assert.assertEquals("Indirect instance set mismatch", EXPECTED_NEW_21, actualNewInstanceValue2);
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			final Future<Object> future3 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceAsynchronous(INSTANCE_GETTER, instance);
			final Object returnedValue3 = future3.get();	// Wait for asynchronous operation to finish
			final Integer actualNewInstanceValue3 = (Integer) returnedValue3;
			Assert.assertEquals("Indirect instance get mismatch", EXPECTED_NEW_21, actualNewInstanceValue3);
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final Object returnedValue4 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceSynchronous(INSTANCE_SETTER, instance, new Object[]{EXPECTED_NEW_22});
			final Integer actualNewInstanceValue4 = instance.getInstanceValue();
			Assert.assertEquals("Indirect instance set mismatch", EXPECTED_NEW_22, actualNewInstanceValue4);
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final Object returnedValue5 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceSynchronous(INSTANCE_GETTER, instance);
			final Integer actualNewInstanceValue5 = (Integer) returnedValue5;
			Assert.assertEquals("Indirect instance get mismatch", EXPECTED_NEW_22, actualNewInstanceValue5);
		}
	}

	@Test
	public void testInstancesSameParameters() throws Exception {
		final TestHelperClass[] instances = new TestHelperClass[] {new TestHelperClass(), new TestHelperClass()};

		{	// Invoke instances method directly
			for (final TestHelperClass instance : instances) {
				instance.setInstanceValue(EXPECTED_INITIAL_11);
				final Integer actualInitialInstanceValue1 = instance.getInstanceValue();
				Assert.assertEquals("Direct instances get mismatch", EXPECTED_INITIAL_11, actualInitialInstanceValue1);
			}
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			final List<Future<Object>> futures = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesAsynchronous(INSTANCE_SETTER, instances, new Object[]{EXPECTED_NEW_21});
			for (final Future<Object> future: futures) {
				future.get();	// Wait for asynchronous operation to finish
			}
			for (final TestHelperClass instance : instances) {
				final Integer actualNewInstanceValue3 = instance.getInstanceValue();
				Assert.assertEquals("Indirect instances set mismatch", EXPECTED_NEW_21, actualNewInstanceValue3);
			}
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			for (final TestHelperClass instance : instances) {
				final Future<Object> future3 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceAsynchronous(INSTANCE_GETTER, instance);
				final Object returnedValue3 = future3.get();	// Wait for asynchronous operation to finish
				final Integer actualNewInstanceValue3 = (Integer) returnedValue3;
				Assert.assertEquals("Indirect instance get mismatch", EXPECTED_NEW_21, actualNewInstanceValue3);
			}
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final List<Object> results = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesSynchronous(INSTANCE_SETTER, instances, new Object[]{EXPECTED_NEW_22});
			for (final TestHelperClass instance : instances) {
				final Integer actualNewInstanceValue3 = instance.getInstanceValue();
				Assert.assertEquals("Indirect instances set mismatch", EXPECTED_NEW_22, actualNewInstanceValue3);
			}
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final List<Object> results = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesSynchronous(INSTANCE_GETTER, instances);
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Object returnedValue3 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceSynchronous(INSTANCE_GETTER, instance);
				final Integer actualNewInstanceValue3 = (Integer) returnedValue3;
				final Integer actualResultValue3 = (Integer) results.get(i);
				Assert.assertEquals("Indirect instance get mismatch", EXPECTED_NEW_22, actualNewInstanceValue3);
				Assert.assertEquals("Indirect instance get mismatch", EXPECTED_NEW_22, actualResultValue3);
			}
		}
	}

	@Test
	public void testInstancesDifferentParameters() throws Exception {
		final TestHelperClass[] instances  = new TestHelperClass[] {new TestHelperClass(), new TestHelperClass()};
		final Object[][] perInstanceParameters1 = {
				new Object[]{EXPECTED_NEW_21},
				new Object[]{EXPECTED_NEW_22}
		};
		final Object[][] perInstanceParameters2 = {
				new Object[]{EXPECTED_NEW_23},
				new Object[]{EXPECTED_NEW_24}
		};

		{	// Invoke instances method directly
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Integer expectedValue = (Integer) perInstanceParameters1[i][0];
				instance.setInstanceValue(expectedValue);
				final Integer actualInitialInstanceValue1 = instance.getInstanceValue();
				Assert.assertEquals("Direct instances get mismatch", expectedValue, actualInitialInstanceValue1);
			}
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			final List<Future<Object>> futures = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesAsynchronous(INSTANCE_SETTER, instances, perInstanceParameters1);
			for (final Future<Object> future: futures) {
				future.get();	// Wait for asynchronous operation to finish
			}
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Integer expectedValue = (Integer) perInstanceParameters1[i][0];
				final Integer actualNewInstanceValue3 = instance.getInstanceValue();
				Assert.assertEquals("Indirect instances set mismatch", expectedValue, actualNewInstanceValue3);
			}
		}

		{	// Invoke asynchronous instance method indirectly via reflection
			final List<Future<Object>> futures = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesAsynchronous(INSTANCE_GETTER, instances);
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Integer expectedValue = (Integer) perInstanceParameters1[i][0];
				final Future<Object> future3 = futures.get(i);
				final Object returnedValue3 = future3.get();	// Wait for asynchronous operation to finish
				final Integer actualNewInstanceValue3 = (Integer) returnedValue3;
				Assert.assertEquals("Indirect instances get mismatch", expectedValue, actualNewInstanceValue3);
			}
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final List<Object> returnValues = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesSynchronous(INSTANCE_SETTER, instances, perInstanceParameters1);
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Integer expectedValue = (Integer) perInstanceParameters1[i][0];
				final Integer actualNewInstanceValue3 = instance.getInstanceValue();
				Assert.assertEquals("Indirect instances set mismatch", expectedValue, actualNewInstanceValue3);
			}
		}

		{	// Invoke synchronous instance method indirectly via reflection
			final List<Object> results = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstancesSynchronous(INSTANCE_GETTER, instances);
			for (int numInstances=instances.length, i=0; i<numInstances; i++) {
				final TestHelperClass instance = instances[i];
				final Integer expectedValue = (Integer) perInstanceParameters1[i][0];
				final Object returnedValue3 = TestThreadPoolUtil.THREAD_POOL_UTIL.executeOnInstanceSynchronous(INSTANCE_GETTER, instance);
				final Integer actualNewInstanceValue3 = (Integer) returnedValue3;
				final Integer actualtResultValue3 = (Integer) results.get(i);
				Assert.assertEquals("Indirect instances get mismatch", expectedValue, actualNewInstanceValue3);
				Assert.assertEquals("Indirect instances get mismatch", expectedValue, actualtResultValue3);
			}
		}
	}

	private static class TestHelperClass {
		private static	Integer classValue		= Integer.valueOf(0);
		private			Integer instanceValue	= Integer.valueOf(0);

		public static Integer getClassValue() {
			LOG.log(Level.FINE, "Getting classValue=" + classValue);
			return TestHelperClass.classValue;
		}
		public static void setClassValue(final Integer classValue) {
			TestHelperClass.classValue = classValue;
			LOG.log(Level.FINE, "Setting classValue=" + classValue);
		}

		public Integer getInstanceValue() {
			LOG.log(Level.FINE, "Getting instanceValue=" + this.instanceValue);
			return this.instanceValue;
		}
		public void setInstanceValue(final Integer instanceValue) {
			this.instanceValue = instanceValue;
			LOG.log(Level.FINE, "Setting instanceValue=" + this.instanceValue);
		}

		public static void sleep(final Long millis) throws InterruptedException {
			Thread.sleep(millis.longValue());	// NOSONAR Remove this use of "Thread.sleep()".
		}
	}
}