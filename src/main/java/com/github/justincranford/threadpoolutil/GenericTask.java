package com.github.justincranford.threadpoolutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("hiding")
public class GenericTask implements Callable<Object> {	// NOPMD
	/*package*/ static final Logger LOG = Logger.getLogger(GenericTask.class.getName());	// NOPMD

	private final Method   mandatoryMethod;		// NOPMD
	private final Object   optionalInstance;	// NOPMD
	private final Object[] optionalParameters;	// NOPMD

	public GenericTask(final Method mandatoryMethod, final Object optionalInstance, final Object[] optionalParameters) {	// NOPMD
		this.mandatoryMethod = mandatoryMethod;
		this.optionalInstance = optionalInstance;
		this.optionalParameters = optionalParameters;
	}

	@Override
	public Object call() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {	// NOPMD
		try {
			return this.mandatoryMethod.invoke(this.optionalInstance, this.optionalParameters);
		} catch(Throwable t) {	// NOSONAR NOPMD Catch Exception instead of Throwable.
			LOG.log(Level.WARNING, "Unexpected exception", t);
			throw t;	// NOPMD Catch to log, or some exceptions not logged (ex: NullPointerException).
		}
	}
}