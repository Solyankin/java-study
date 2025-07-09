package org.example.testrunner;

import org.example.test.runner.annotations.CustomAfter;
import org.example.test.runner.annotations.CustomBefore;
import org.example.test.runner.annotations.CustomTest;

import java.util.ArrayList;
import java.util.List;

public class CustomRunnerTest {

    private List<String> list;

    @CustomBefore
    public void setUp() {
        list = new ArrayList<>();
        list.add("test");
    }

    @CustomTest
    public void testListSize() {
        if (list.size() != 1) {
            throw new AssertionError("List size should be 1");
        }
    }

    @CustomTest(expected = IndexOutOfBoundsException.class)
    public void testEmptyList() {
        list.clear();
        list.get(0); // Должно бросить исключение
    }

    @CustomAfter
    public void tearDown() {
        list = null;
    }
}
