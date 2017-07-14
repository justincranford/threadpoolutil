package com.github.justincranford.threadpoolutil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"unchecked","rawtypes"})
public final class ThreadPoolUtil {
	/*package*/ static final Logger LOG = Logger.getLogger(ThreadPoolUtil.class.getName());

	public static final int SYS_AVAILABLE_CPUS = Runtime.getRuntime().availableProcessors();	// Assume static value, even though it could change at run-time (ex: client VMs).

	public static final ThreadPoolExecutor THREAD_POOL = (ThreadPoolExecutor) Executors.newFixedThreadPool(ThreadPoolUtil.SYS_AVAILABLE_CPUS, DaemonThreadFactoryUtil.DAEMON_THREAD_FACTORY);

	private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";
	private static final String METHOD_IS_MANDATORY = "Method is mandatory";
	private static final String ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY = "Array and elements of instances array are mandatory";
	private static final String ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT = "Array of parameters is mandatory, elements are not";
	private static final String ARRAY_AND_ELEMENTS_OF_PARAMETERS_DOUBLE_ARRAY_ARE_MANDATORY_LEAF_ELEMENTS_ARE_NOT = "Array and elements of parameters double array are mandatory, leaf elements are not";

	private ThreadPoolUtil() {
		// prevent class instantiation by making constructor private
	}

    public static List invokeAll(final List callables) throws Exception {
    	try {
	    	return getResults(ThreadPoolUtil.THREAD_POOL.invokeAll(callables));
    	} catch(Exception e) {
			LOG.log(Level.SEVERE, UNEXPECTED_EXCEPTION, e);
    		throw e;
    	}
    }

	public static List<Object> getResults(final List<Future<Object>> taskResults) throws Exception {
    	try {
    		final List<Object> results = new ArrayList<>(taskResults.size());
        	for (final Future<Object> asynchronousResult : taskResults) {
        		results.add(asynchronousResult.get());
        	}
    		return results;
    	} catch(Exception e) {
    		LOG.log(Level.SEVERE, UNEXPECTED_EXCEPTION, e);
    		throw e;
    	}
	}

	public static Future<Object> executeOnClassAsynchronous(final Method mandatoryMethod) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		return ThreadPoolUtil.THREAD_POOL.submit(new GenericTask(mandatoryMethod, null, null));
	}

	public static Future<Object> executeOnClassAsynchronous(final Method mandatoryMethod, final Object[] mandatoryParameters) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		return ThreadPoolUtil.THREAD_POOL.submit(new GenericTask(mandatoryMethod, null, mandatoryParameters));
	}

	public static Future<Object> executeOnInstanceAsynchronous(final Method mandatoryMethod, final Object mandatoryInstance) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullObject(mandatoryInstance, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		return ThreadPoolUtil.THREAD_POOL.submit(new GenericTask(mandatoryMethod, mandatoryInstance, null));
    }

	public static Future<Object> executeOnInstanceAsynchronous(final Method mandatoryMethod, final Object mandatoryInstance, final Object[] mandatoryParameters) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullObject(mandatoryInstance, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ThreadPoolUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		return ThreadPoolUtil.THREAD_POOL.submit(new GenericTask(mandatoryMethod, mandatoryInstance, mandatoryParameters));
    }

	public static List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (final Object mandatoryInstance : mandatoryInstances) {
			taskRequests.add(ThreadPoolUtil.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance));
		}
		return taskRequests;
    }

	public static List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[] mandatoryParameters) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ThreadPoolUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (final Object mandatoryInstance : mandatoryInstances) {
			taskRequests.add(ThreadPoolUtil.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters));
		}
		return taskRequests;
    }

	public static List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[][] mandatoryParametersPerInstance) throws Exception {
		ThreadPoolUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ThreadPoolUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ThreadPoolUtil.assertNonNullArrayAndElements(mandatoryParametersPerInstance, ARRAY_AND_ELEMENTS_OF_PARAMETERS_DOUBLE_ARRAY_ARE_MANDATORY_LEAF_ELEMENTS_ARE_NOT);
		ThreadPoolUtil.assertSameLength(mandatoryInstances, mandatoryParametersPerInstance);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (int i=0; i<mandatoryInstances.length; i++) {
			final Object mandatoryInstance = mandatoryInstances[i];
			final Object[] mandatoryParameters = mandatoryParametersPerInstance[i];
			taskRequests.add(ThreadPoolUtil.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters));
		}
		return taskRequests;
    }

	public static Object executeOnClassSynchronous(final Method mandatoryMethod) throws Exception {
		return ThreadPoolUtil.executeOnClassAsynchronous(mandatoryMethod).get();
    }
	public static Object executeOnClassSynchronous(final Method mandatoryMethod, final Object[] mandatoryParameters) throws Exception {
		return ThreadPoolUtil.executeOnClassAsynchronous(mandatoryMethod, mandatoryParameters).get();
	}
	public static Object executeOnInstanceSynchronous(final Method mandatoryMethod, final Object mandatoryInstance) throws Exception {
		return ThreadPoolUtil.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance).get();
	}
	public static Object executeOnInstanceSynchronous(final Method mandatoryMethod, final Object mandatoryInstance, final Object[] mandatoryParameters) throws Exception {
		return ThreadPoolUtil.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters).get();
	}
	public static List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances) throws Exception {
		return ThreadPoolUtil.getResults(ThreadPoolUtil.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances));
    }
	public static List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[] mandatoryParameters) throws Exception {
		return ThreadPoolUtil.getResults(ThreadPoolUtil.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances, mandatoryParameters));
    }
	public static List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[][] mandatoryParametersPerInstance) throws Exception {
		return ThreadPoolUtil.getResults(ThreadPoolUtil.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances, mandatoryParametersPerInstance));
    }

	public static void waitForDone(final Future<Object> future, final long sleepGranularity) throws InterruptedException {
		ThreadPoolUtil.assertGreaterThan(sleepGranularity, 0L);
		while (!future.isDone()) {
			Thread.sleep(sleepGranularity);
		}
	}

	/*protected*/ static void assertNonNullObject(final Object object, final String message) throws IllegalArgumentException {
		if (null == object) {
			throw new IllegalArgumentException(message);
		}
	}

	/*protected*/ static void assertNonNullArrayAndElements(final Object[] object, final String message) throws IllegalArgumentException {
		ThreadPoolUtil.assertNonNullObject(object, message);
		for (final Object element : object) {
			ThreadPoolUtil.assertNonNullObject(element, message);
		}
	}

	/*protected*/ static void assertGreaterThan(final long value1, final long value2) throws IllegalArgumentException {
		if (value1 <= value2) {
			throw new IllegalArgumentException("Value " + value1 + " must be greater than " + value2 + ".");
		}
	}

	/*protected*/ static void assertSameLength(final Object[] array1, final Object[] array2) throws IllegalArgumentException {
		ThreadPoolUtil.assertNonNullObject(array1, "Array 1 is mandatory");
		ThreadPoolUtil.assertNonNullObject(array2, "Array 2 is mandatory");
		if (array1.length != array2.length) {
			throw new IllegalArgumentException("Lengths " + array1.length + " and " + array2.length + " must be the same.");
		}
	}
}