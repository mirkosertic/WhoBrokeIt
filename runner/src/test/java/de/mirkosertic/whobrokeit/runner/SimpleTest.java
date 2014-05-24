package de.mirkosertic.whobrokeit.runner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(WhoBrokeItRunner.class)
public class SimpleTest {

    ClassUnderTest classUnderTest;

    @Before
    public void initialize() {
        classUnderTest = new ClassUnderTest();
    }

    @Test
    public void testGreet() {
        assertEquals("hello", classUnderTest.greet());
    }
}
