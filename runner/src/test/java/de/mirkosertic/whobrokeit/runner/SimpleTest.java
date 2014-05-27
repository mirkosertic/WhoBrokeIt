package de.mirkosertic.whobrokeit.runner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTest {

    @Before
    public void initialize() {
    }

    @Test
    public void testGreet() {
        UnderTestClass theClassUnderTest = new UnderTestClass();
        assertEquals("hello", theClassUnderTest.greet());
    }

    @Test
    public void failingTest() {
        throw new IllegalArgumentException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpectedException1() {
        throw new IllegalArgumentException();
    }

    @Test(expected = RuntimeException.class)
    public void testExpectedException2() {
        throw new IllegalArgumentException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailingUnexpectedException() {
        throw new RuntimeException();
    }
}
