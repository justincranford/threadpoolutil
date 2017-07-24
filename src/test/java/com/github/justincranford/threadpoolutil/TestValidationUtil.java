package com.github.justincranford.threadpoolutil;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

@SuppressWarnings("static-method")
public class TestValidationUtil {
	@Test
	public void testAssertTrueSuccess() throws IllegalArgumentException  {
		ValidationUtil.assertTrue(true);
		ValidationUtil.assertTrue(true, "message");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertTrueFailure1() throws IllegalArgumentException  {
		ValidationUtil.assertTrue(false);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertTrueFailure2() throws IllegalArgumentException  {
		ValidationUtil.assertTrue(false, "message");
	}

	@Test
	public void testAssertFalseSuccess() throws IllegalArgumentException  {
		ValidationUtil.assertFalse(false);
		ValidationUtil.assertFalse(false, "message");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertFalseFailure1() throws IllegalArgumentException  {
		ValidationUtil.assertFalse(true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertFalseFailure2() throws IllegalArgumentException  {
		ValidationUtil.assertFalse(true, "message");
	}

	@Test
	public void testPrivateConstructorEmptyParameters() throws Exception {
		ValidationUtil.assertPrivateConstructorNoParameters(ValidationUtil.class, false);
		ValidationUtil.assertPrivateConstructorNoParameters(ValidationUtil.class, true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testPrivateConstructorWithoutParametersNotPublic() throws Exception {
		ValidationUtil.assertPublicConstructorNoParameters(TestClassWithPrivateConstructorWithoutParameters.class, false);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testPublicConstructorWithoutParametersNotPrivate() throws Exception {
		ValidationUtil.assertPrivateConstructorNoParameters(TestClassWithPublicConstructorWithoutParameters.class, false);
	}

	@Test(expected=NoSuchMethodException.class)
	public void testPublicConstructorWithParameters() throws Exception {
		ValidationUtil.assertPublicConstructorNoParameters(TestClassWithPublicConstructorWithParameters.class, true);
	}

	@Test
	public void testPublicConstructorWithoutParameters() throws Exception {
		ValidationUtil.assertPublicConstructorNoParameters(TestClassWithPublicConstructorWithoutParameters.class, false);
		ValidationUtil.assertPublicConstructorNoParameters(TestClassWithPublicConstructorWithoutParameters.class, true);
	}

	@Test(expected=NoSuchMethodException.class)
	public void testPrivateConstructorWithParameters() throws Exception {
		ValidationUtil.assertPrivateConstructorNoParameters(TestClassWithPrivateConstructorWithParameters.class, true);
	}

	@Test
	public void testPrivateConstructorWithoutParameters() throws Exception {
		ValidationUtil.assertPrivateConstructorNoParameters(TestClassWithPrivateConstructorWithoutParameters.class, false);
		ValidationUtil.assertPrivateConstructorNoParameters(TestClassWithPrivateConstructorWithoutParameters.class, true);
	}

	@Test
	public void testAssertEqualsObjectArraySuccess() throws IllegalArgumentException {
		ValidationUtil.assertEquals((Object[])null, (Object[])null);
		ValidationUtil.assertEquals(new Object[]{}, new Object[]{});
		ValidationUtil.assertEquals(new Object[]{Integer.valueOf(100)}, new Object[]{Integer.valueOf(100)});
		final Object[] same = new Object[]{Integer.valueOf(100)};
		ValidationUtil.assertEquals(same, same);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsObjectArrayFailsLeftNull() throws IllegalArgumentException {
		ValidationUtil.assertEquals((Object[])null, new Object[]{});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsObjectArrayFailsRightNull() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new Object[]{},(Object[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsObjectArrayFailsLeftEmpty() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new Object[]{}, new Object[]{Integer.valueOf(100)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsObjectArrayFailsRightEmpty() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new Object[]{Integer.valueOf(100)},new Object[]{});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsObjectArrayFailsDifferentElements() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new Object[]{Integer.valueOf(100)}, new Object[]{Integer.valueOf(101)});
	}

	@Test
	public void testAssertEqualsByteArraySuccess() throws IllegalArgumentException {
		ValidationUtil.assertEquals((byte[])null, (byte[])null);
		ValidationUtil.assertEquals(new byte[]{}, new byte[]{});
		ValidationUtil.assertEquals(new byte[]{(byte)0xAB}, new byte[]{(byte)0xAB});
		final byte[] same = new byte[]{(byte)0xAB};
		ValidationUtil.assertEquals(same, same);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsByteArrayFailsLeftNull() throws IllegalArgumentException {
		ValidationUtil.assertEquals((byte[])null, new byte[]{});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsByteArrayFailsRightNull() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new byte[]{},(byte[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsByteArrayFailsLeftEmpty() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new byte[]{}, new byte[]{(byte)0xAB});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsByteArrayFailsRightEmpty() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new byte[]{(byte)0xAB},new byte[]{});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertEqualsByteArrayFailsDifferentElements() throws IllegalArgumentException {
		ValidationUtil.assertEquals(new byte[]{(byte)0xAB}, new byte[]{(byte)0xCD});
	}

	@Test
	public void testAssertObjectArrayNonNullElementsWithNullArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements((Object[])null);
	}

	@Test
	public void testAssertObjectArrayNonNullElementsWithEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(new Object[]{});
	}

	@Test
	public void testAssertObjectArrayNonNullElementsWithNonEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(new Object[]{Integer.valueOf(100)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertObjectArrayNonNullElementsWithNullElement() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(new Object[]{Integer.valueOf(100), null});
	}

	@Test
	public void testAssertCollectionNonNullElementsWithNullArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements((ArrayList<Object>)null);
	}

	@Test
	public void testAssertCollectionNonNullElementsWithEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(new ArrayList<Object>(0));
	}

	@Test
	public void testAssertCollectionNonNullElementsWithNonEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(Arrays.asList(new Object()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertCollectionNonNullElementsWithNullElement() throws IllegalArgumentException {
		ValidationUtil.assertNonNullElements(Arrays.asList(new Object(), null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullArrayAndElementsWithNullArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullArrayAndElements((Object[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullArrayAndElementsWithNullElement() throws IllegalArgumentException {
		ValidationUtil.assertNonNullArrayAndElements(new Object[]{Integer.valueOf(0), null, Integer.valueOf(0)}, "");
	}

	@Test
	public void testAssertNonNullArrayAndElementsWithEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullArrayAndElements(new Object[]{});
	}

	@Test
	public void testAssertNonNullArrayAndElementsWithOkArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullArrayAndElements(new Object[]{Integer.valueOf(0)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullCollectionAndElementsWithNullCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullCollectionAndElements((ArrayList<Object>)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullCollectionAndElementsWithNullElement() throws IllegalArgumentException {
		ValidationUtil.assertNonNullCollectionAndElements(Arrays.asList(new Object(), null));
	}

	@Test
	public void testAssertNonNullCollectionAndElementsWithEmptyCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullCollectionAndElements(new ArrayList<Object>(0));
	}

	@Test
	public void testAssertNonNullCollectionAndElementsWithOkCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullCollectionAndElements(Arrays.asList(new Object(), new Object()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertArrayNonEmptyWithEmptyList() throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty(new Object[]{});
	}

	@Test
	public void testAssertArrayNonEmptyWithNonEmptyList() throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty((Object[])null);
		ValidationUtil.assertNonEmpty(new Object[]{Integer.valueOf(100)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertCollectionNonEmptyWithEmptyList() throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty(new ArrayList<Object>(0));
	}

	@Test
	public void testAssertCollectionNonEmptyWithNonEmptyList() throws IllegalArgumentException {
		ValidationUtil.assertNonEmpty((ArrayList<Object>)null);
		ValidationUtil.assertNonEmpty(Arrays.asList(new Object()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertArrayNonNullAndNonEmptyWithNullArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty((Object[])null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertArrayNonNullAndNonEmptyWithEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(new Object[]{});
	}

	@Test
	public void testAssertArrayNonNullAndNonEmptyWithNonEmptyArray() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(new Object[]{Integer.valueOf(100)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertCollectionNonNullAndNonEmptyWithNullCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty((ArrayList<Object>)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertCollectionNonNullAndNonEmptyWithEmptyCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(new ArrayList<Object>(0));
	}

	@Test
	public void testAssertCollectionNonNullAndNonEmptyWithNonEmptyCollection() throws IllegalArgumentException {
		ValidationUtil.assertNonNullAndNonEmpty(Arrays.asList(new Object()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullObjectWithNullObject() throws IllegalArgumentException {
		ValidationUtil.assertNonNull(null);
	}

	@Test
	public void testAssertNonNullObjectWithNonNullObject() throws IllegalArgumentException {
		ValidationUtil.assertNonNull(Integer.valueOf(0));
	}

	@Test
	public void testAssertNonNullObjectWithNonNullObjectSuccess() throws IllegalArgumentException {
		ValidationUtil.assertAllNullOrAllNotNullObjects(null, null);
		ValidationUtil.assertAllNullOrAllNotNullObjects(Integer.valueOf(0), Integer.valueOf(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullObjectWithNonNullObjectFailureLeftNull() throws IllegalArgumentException {
		ValidationUtil.assertAllNullOrAllNotNullObjects(null, Integer.valueOf(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertNonNullObjectWithNonNullObjectFailureRightNull() throws IllegalArgumentException {
		ValidationUtil.assertAllNullOrAllNotNullObjects(Integer.valueOf(0), null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertGreaterThanWithLessThanValues() throws IllegalArgumentException {
		ValidationUtil.assertGreaterThan(0, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertGreaterThanWithEqualValues() throws IllegalArgumentException {
		ValidationUtil.assertGreaterThan(0, 0);
	}

	@Test
	public void testAssertGreaterThanWithGreaterThanValues() throws IllegalArgumentException {
		ValidationUtil.assertGreaterThan(1, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertSameLengthWithNullArray1() throws IllegalArgumentException {
		ValidationUtil.assertSameLength(null, new Object[]{Integer.valueOf(0)});
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertSameLengthWithNullArray2() throws IllegalArgumentException {
		ValidationUtil.assertSameLength(new Object[]{Integer.valueOf(0)}, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAssertSameLengthWithDifferentLengths() throws IllegalArgumentException {
		ValidationUtil.assertSameLength(new Object[]{Integer.valueOf(0),Integer.valueOf(0)}, new Object[]{Integer.valueOf(0)});
	}

	private static class TestClassWithPublicConstructorWithParameters {
		@SuppressWarnings("unused")
		public TestClassWithPublicConstructorWithParameters(final Object object) {
			// do nothing
		}
	}

	private static class TestClassWithPublicConstructorWithoutParameters {
		@SuppressWarnings("unused")
		public TestClassWithPublicConstructorWithoutParameters() {
			// do nothing
		}
	}

	private static class TestClassWithPrivateConstructorWithParameters {
		private TestClassWithPrivateConstructorWithParameters(final Object object) {
			// do nothing
		}
	}

	private static class TestClassWithPrivateConstructorWithoutParameters {
		private TestClassWithPrivateConstructorWithoutParameters() {
			// do nothing
		}
	}
}