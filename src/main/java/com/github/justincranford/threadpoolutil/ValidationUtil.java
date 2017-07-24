package com.github.justincranford.threadpoolutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

public class ValidationUtil {
	private ValidationUtil() {
		// prevent class instantiation by making constructor private
	}

	public static void assertTrue(final boolean value) throws IllegalArgumentException {
		ValidationUtil.assertTrue(value, "Value must be true.");
	}

	public static void assertTrue(final boolean value, final String message) throws IllegalArgumentException {
		if (!value) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertFalse(final boolean value) throws IllegalArgumentException {
		ValidationUtil.assertFalse(value, "Value must be false.");
	}

	public static void assertFalse(final boolean value, final String message) throws IllegalArgumentException {
		if (value) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertPublicConstructorNoParameters(final Class<?> clazz, final boolean invoke) throws IllegalArgumentException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {	// NOSONAR Refactor this method to throw at most one checked exception
		ValidationUtil.assertPublicConstructorNoParameters(clazz, invoke, "Constructor must be public with no parameters.");
	}

	public static void assertPublicConstructorNoParameters(final Class<?> clazz, final boolean invoke, final String message) throws IllegalArgumentException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {	// NOSONAR Refactor this method to throw at most one checked exception
		final Constructor<?> constructor = clazz.getDeclaredConstructor();	// throws exception if zero-parameter constructor is not found
		if (Modifier.isPrivate(constructor.getModifiers())) {
			throw new IllegalArgumentException(message);
		}
		if (invoke) {
			constructor.newInstance();	// ASSUMPTION: Never returns null, but could throw exception. We need to execute it to rule out an exception and for code coverage reporting.
		}
	}

	public static void assertPrivateConstructorNoParameters(final Class<?> clazz, final boolean invoke) throws IllegalArgumentException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {	// NOSONAR Refactor this method to throw at most one checked exception
		ValidationUtil.assertPrivateConstructorNoParameters(clazz, invoke, "Constructor must be private with no parameters.");
	}

	public static void assertPrivateConstructorNoParameters(final Class<?> clazz, final boolean invoke, final String message) throws IllegalArgumentException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {	// NOSONAR Refactor this method to throw at most one checked exception
		final Constructor<?> constructor = clazz.getDeclaredConstructor();	// throws exception if zero-parameter constructor is not found
		if (!Modifier.isPrivate(constructor.getModifiers())) {
			throw new IllegalArgumentException(message);
		}
		if (invoke) {
			constructor.setAccessible(true);
			constructor.newInstance();	// ASSUMPTION: Never returns null, but could throw exception. We need to execute it to rule out an exception and for code coverage reporting.
		}
	}

	public static void assertEquals(final Object[] array1, final Object[] array2) throws IllegalArgumentException {
		ValidationUtil.assertEquals(array1, array2, "Array contents must be equal.");
	}

	public static void assertEquals(final Object[] array1, final Object[] array2, final String message) throws IllegalArgumentException {
		if (!Arrays.equals(array1, array2)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertEquals(final byte[] byteArray1, final byte[] byteArray2) throws IllegalArgumentException {
		ValidationUtil.assertEquals(byteArray1, byteArray2, "Byte array contents must be equal.");
	}

	public static void assertEquals(final byte[] byteArray1, final byte[] byteArray2, final String message) throws IllegalArgumentException {
		if (!Arrays.equals(byteArray1, byteArray2)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNonNullElements(final Object[] array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(array, "Array must not contain null elements.");
	}

	public static void assertNonNullElements(final Object[] array, final String message) throws IllegalArgumentException {
		if (null != array) {
			for (final Object element : array) {
				ValidationUtil.assertNonNullObject(element, message);
			}
		}
	}

	public static void assertNonNullElements(final Collection<?> array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(array, "Collection must not contain null elements.");
	}

	public static void assertNonNullElements(final Collection<?> array, final String message) throws IllegalArgumentException {
		if (null != array) {
			for (final Object element : array) {
				ValidationUtil.assertNonNullObject(element, message);
			}
		}
	}

	public static void assertNonNullArrayAndElements(final Object[] array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullArrayAndElements(array, "Object array must be non-null and not contain any null elements.");
	}

	public static void assertNonNullArrayAndElements(final Object[] array, final String message) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(array, message);
		ValidationUtil.assertNonNullElements(array, message);
	}

	public static void assertNonNullCollectionAndElements(final Collection<?> array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullCollectionAndElements(array, "Collection must be non-null and not contain any null elements.");
	}

	public static void assertNonNullCollectionAndElements(final Collection<?> array, final String message) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(array, message);
		ValidationUtil.assertNonNullElements(array, message);
	}

	public static void assertNonEmpty(final Object[] array) throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty(array, "Array must not be empty.");
	}

	public static void assertNonEmpty(final Object[] array, final String message) throws IllegalArgumentException {
		if (null != array) {
			ValidationUtil.assertGreaterThan(array.length, 0, message);
		}
	}

	public static void assertNonEmpty(final Collection<?> array) throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty(array, "Collection must not be empty.");
	}

	public static void assertNonEmpty(final Collection<?> array, final String message) throws IllegalArgumentException {
		if (null != array) {
			ValidationUtil.assertGreaterThan(array.size(), 0, message);
		}
	}

	public static void assertNonNullAndNonEmpty(final Object[] array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(array, "Array must not be null and not contain any null elements.");
	}

	public static void assertNonNullAndNonEmpty(final Object[] array, final String message) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(array, message);
		ValidationUtil.assertGreaterThan(array.length, 0, message);
	}

	public static void assertNonNullAndNonEmpty(final Collection<?> array) throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(array, "Collection must not be null and not contain any null elements.");
	}

	public static void assertNonNullAndNonEmpty(final Collection<?> array, final String message) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(array, message);
		ValidationUtil.assertGreaterThan(array.size(), 0, message);
	}

	public static void assertNonNull(final Object object) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(object, "Object must not be null.");
	}

	public static void assertNonNullAndNotEmpty(final String string) throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNotEmpty(string, "String must not be null and not empty.");
	}

	public static void assertNonNullAndNotEmpty(final String string, final String message) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(string, message);
		ValidationUtil.assertGreaterThan(string.length(), 0, message);
	}

	public static void assertNonNullObject(final Object object, final String message) throws IllegalArgumentException {
		if (null == object) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertAllNullOrAllNotNullObjects(final Object object1, final Object object2) throws IllegalArgumentException {
		assertAllNullOrAllNotNullObjects(object1, object2, "Both objects must be null or non-null.");
	}

	public static void assertAllNullOrAllNotNullObjects(final Object array1, final Object array2, final String message) throws IllegalArgumentException {
		if (((null == array1) && (null != array2)) || ((null != array1) && (null == array2))) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertGreaterThanOrEqual(final int value1, final int value2) throws IllegalArgumentException {
		ValidationUtil.assertGreaterThanOrEqual(value1, value2, "Value " + value1 + " must be greater than or equal to " + value2 + ".");	// NOSONAR Define a constant instead of duplicating this literal
	}

	public static void assertGreaterThanOrEqual(final int value1, final int value2, final String message) throws IllegalArgumentException {
		if (value1 < value2) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertGreaterThanOrEqual(final long value1, final long value2) throws IllegalArgumentException {
		ValidationUtil.assertGreaterThanOrEqual(value1, value2, "Value " + value1 + " must be greater than or equal to " + value2 + ".");
	}

	public static void assertGreaterThanOrEqual(final long value1, final long value2, final String message) throws IllegalArgumentException {
		if (value1 < value2) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertGreaterThan(final long value1, final long value2) throws IllegalArgumentException {
		ValidationUtil.assertGreaterThan(value1, value2, "Value " + value1 + " must be greater than " + value2 + ".");
	}

	public static void assertGreaterThan(final long value1, final long value2, final String message) throws IllegalArgumentException {
		if (value1 <= value2) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertGreaterThan(final int value1, final int value2) throws IllegalArgumentException {
		ValidationUtil.assertGreaterThan(value1, value2, "Value " + value1 + " must be greater than " + value2 + ".");
	}

	public static void assertGreaterThan(final int value1, final int value2, final String message) throws IllegalArgumentException {
		if (value1 <= value2) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertSameLength(final Object[] array1, final Object[] array2) throws IllegalArgumentException {
		ValidationUtil.assertNonNullObject(array1, "Array 1 is mandatory");
		ValidationUtil.assertNonNullObject(array2, "Array 2 is mandatory");
		if (array1.length != array2.length) {
			throw new IllegalArgumentException("Lengths " + array1.length + " and " + array2.length + " must be the same.");
		}
	}
}