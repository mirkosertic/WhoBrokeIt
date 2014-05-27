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
}
