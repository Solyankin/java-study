package org.example.custom_runner;

import org.example.runner.annotations.*;

public class CustomTestClass {

    @BeforeSuite
    public static void globalSetup() {
        System.out.println("Global setup (BeforeSuite)");
    }

    @AfterSuite
    public static void globalTeardown() {
        System.out.println("Global teardown (AfterSuite)");
    }

    @BeforeTest
    public void testSetup() {
        System.out.println("Test setup (BeforeTest)");
    }

    @AfterTest
    public void testTeardown() {
        System.out.println("Test teardown (AfterTest)");
    }

    @Test(priority = 1)
    public void highPriorityTest() {
        System.out.println("Running test with priority 1");
    }

    @Test(priority = 5)
    public void mediumPriorityTest() {
        System.out.println("Running test with priority 5");
    }

    @Test(priority = 10)
    public void lowPriorityTest() {
        System.out.println("Running test with priority 10");
    }

    @Test // priority = 5 (default)
    public void defaultPriorityTest() {
        System.out.println("Running test with default priority");
    }
}