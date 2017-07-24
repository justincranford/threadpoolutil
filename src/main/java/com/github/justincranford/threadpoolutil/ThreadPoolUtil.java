package com.github.justincranford.threadpoolutil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to wrap creating an ExecutorService with daemon or non-daemon thread factory.
 * Default values are to use daemon threads and a pool size matching the number of available CPUs, but both can be overridden.
 * Helper methods are provided to submit batch invocation requests via reflection, and optionally wait for batch results.
 */
@SuppressWarnings({"hiding","unchecked","rawtypes"})
public class ThreadPoolUtil {
	/*package*/ static final Logger LOG = Logger.getLogger(ThreadPoolUtil.class.getName());

	private static final String NUMBER_OF_THREADS_MUST_BE_CREATER_THAN_0 = "Number of threads must be creater than 0.";
	private static final String EXECUTOR_SERVICE_MUST_NOT_BE_NULL = "Executor service must not be null.";
	private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";
	private static final String METHOD_IS_MANDATORY = "Method is mandatory";
	private static final String ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY = "Array and elements of instances array are mandatory";
	private static final String ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT = "Array of parameters is mandatory, elements are not";
	private static final String ARRAY_AND_ELEMENTS_OF_PARAMETERS_DOUBLE_ARRAY_ARE_MANDATORY_LEAF_ELEMENTS_ARE_NOT = "Array and elements of parameters double array are mandatory, leaf elements are not";

	private final ExecutorService executorService;

	public ThreadPoolUtil() {
		this(true, SystemUtil.SYS_AVAILABLE_CPUS);
	}

	public ThreadPoolUtil(final int numThreads) {
		this(true, numThreads);
	}

	public ThreadPoolUtil(final boolean isDaemonThreadPool) {
		this(isDaemonThreadPool, SystemUtil.SYS_AVAILABLE_CPUS);
	}

	public ThreadPoolUtil(final boolean isDaemonThreadPool, final int numThreads) {
		ValidationUtil.assertGreaterThan(numThreads, 0, NUMBER_OF_THREADS_MUST_BE_CREATER_THAN_0);
		this.executorService = Executors.newFixedThreadPool(numThreads, (isDaemonThreadPool ? DaemonThreadFactoryUtil.DAEMON_THREAD_FACTORY : DaemonThreadFactoryUtil.NON_DAEMON_THREAD_FACTORY));
	}

	public ThreadPoolUtil(final ExecutorService executorService) {
		ValidationUtil.assertNonNullObject(executorService, EXECUTOR_SERVICE_MUST_NOT_BE_NULL);
		this.executorService = executorService;
	}

    public ExecutorService getExecutorService() {
    	return this.executorService;
	}

    public List invokeAll(final List callables) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
    	return ThreadPoolUtil.invokeAll(this.executorService, callables);
    }

	public Future<Object> executeOnClassAsynchronous(final Method mandatoryMethod) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		return this.executorService.submit(new InvokeViaReflectionTask(mandatoryMethod, null, null));
	}

	public Future<Object> executeOnClassAsynchronous(final Method mandatoryMethod, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		return this.executorService.submit(new InvokeViaReflectionTask(mandatoryMethod, null, mandatoryParameters));
	}

	public Future<Object> executeOnInstanceAsynchronous(final Method mandatoryMethod, final Object mandatoryInstance) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullObject(mandatoryInstance, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		return this.executorService.submit(new InvokeViaReflectionTask(mandatoryMethod, mandatoryInstance, null));
    }

	public Future<Object> executeOnInstanceAsynchronous(final Method mandatoryMethod, final Object mandatoryInstance, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullObject(mandatoryInstance, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ValidationUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		return this.executorService.submit(new InvokeViaReflectionTask(mandatoryMethod, mandatoryInstance, mandatoryParameters));
    }

	public List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (final Object mandatoryInstance : mandatoryInstances) {
			taskRequests.add(this.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance));
		}
		return taskRequests;
    }

	public List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ValidationUtil.assertNonNullObject(mandatoryParameters, ARRAY_OF_PARAMETERS_IS_MANDATORY_ELEMENTS_ARE_NOT);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (final Object mandatoryInstance : mandatoryInstances) {
			taskRequests.add(this.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters));
		}
		return taskRequests;
    }

	public List<Future<Object>> executeOnInstancesAsynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[][] mandatoryParametersPerInstance) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		ValidationUtil.assertNonNullObject(mandatoryMethod, METHOD_IS_MANDATORY);
		ValidationUtil.assertNonNullArrayAndElements(mandatoryInstances, ARRAY_AND_ELEMENTS_OF_INSTANCES_ARRAY_ARE_MANDATORY);
		ValidationUtil.assertNonNullArrayAndElements(mandatoryParametersPerInstance, ARRAY_AND_ELEMENTS_OF_PARAMETERS_DOUBLE_ARRAY_ARE_MANDATORY_LEAF_ELEMENTS_ARE_NOT);
		ValidationUtil.assertSameLength(mandatoryInstances, mandatoryParametersPerInstance);
		final List<Future<Object>> taskRequests = new ArrayList<>(mandatoryInstances.length);
		for (int i=0; i<mandatoryInstances.length; i++) {
			final Object mandatoryInstance = mandatoryInstances[i];
			final Object[] mandatoryParameters = mandatoryParametersPerInstance[i];
			taskRequests.add(this.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters));
		}
		return taskRequests;
    }

	public Object executeOnClassSynchronous(final Method mandatoryMethod) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return this.executeOnClassAsynchronous(mandatoryMethod).get();
    }
	public Object executeOnClassSynchronous(final Method mandatoryMethod, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return this.executeOnClassAsynchronous(mandatoryMethod, mandatoryParameters).get();
	}
	public Object executeOnInstanceSynchronous(final Method mandatoryMethod, final Object mandatoryInstance) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return this.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance).get();
	}
	public Object executeOnInstanceSynchronous(final Method mandatoryMethod, final Object mandatoryInstance, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return this.executeOnInstanceAsynchronous(mandatoryMethod, mandatoryInstance, mandatoryParameters).get();
	}
	public List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return ThreadPoolUtil.getResults(this.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances));
    }
	public List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[] mandatoryParameters) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return ThreadPoolUtil.getResults(this.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances, mandatoryParameters));
    }
	public List<Object> executeOnInstancesSynchronous(final Method mandatoryMethod, final Object[] mandatoryInstances, final Object[][] mandatoryParametersPerInstance) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
		return ThreadPoolUtil.getResults(this.executeOnInstancesAsynchronous(mandatoryMethod, mandatoryInstances, mandatoryParametersPerInstance));
    }

	public static class InvokeViaReflectionTask implements Callable<Object> {
		private final Method   mandatoryMethod;
		private final Object   optionalInstance;
		private final Object[] optionalParameters;
		public InvokeViaReflectionTask(final Method mandatoryMethod, final Object optionalInstance, final Object[] optionalParameters) {
			this.mandatoryMethod = mandatoryMethod;
			this.optionalInstance = optionalInstance;
			this.optionalParameters = optionalParameters;
		}
		@Override
		public Object call() throws Exception  {
			try {
				return this.mandatoryMethod.invoke(this.optionalInstance, this.optionalParameters);
			} catch(Throwable t) {	// NOSONAR Catch Exception instead of Throwable.
				LOG.log(Level.WARNING, UNEXPECTED_EXCEPTION, t);
				throw new Exception(t);	// Catch and rethrow wrapped Throwable, otherwise some loggable unchecked exceptions may be missed.
			}
		}
	}

	public static List<Object> getResults(final List<Future<Object>> taskResults) throws Exception {
    	try {
    		final List<Object> results = new ArrayList<>(taskResults.size());
        	for (final Future<Object> asynchronousResult : taskResults) {
        		results.add(asynchronousResult.get());
        	}
    		return results;
    	} catch(Throwable t) {	// NOSONAR Catch Exception instead of Throwable.
    		LOG.log(Level.SEVERE, UNEXPECTED_EXCEPTION, t);
    		throw t;	// Catch and rethrow wrapped Throwable, otherwise some loggable unchecked exceptions may be missed.
    	}
	}

	public static void waitForDone(final Future<Object> future, final long sleepGranularity) throws InterruptedException {
		ValidationUtil.assertGreaterThan(sleepGranularity, 0L);
		while (!future.isDone()) {
			Thread.sleep(sleepGranularity);
		}
	}

    public static List invokeAll(final ExecutorService executorService, final List callables) throws Exception {	// NOSONAR Define and throw a dedicated exception instead of using a generic one.
    	try {
	    	return getResults(executorService.invokeAll(callables));
    	} catch(Exception e) {
			LOG.log(Level.SEVERE, UNEXPECTED_EXCEPTION, e);
    		throw e;
    	}
    }
}