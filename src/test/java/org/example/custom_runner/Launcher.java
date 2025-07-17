package org.example.custom_runner;

import org.example.runner.TestRunner;

public class Launcher {
    public static void main(String[] args) {
        TestRunner.runTests(CustomTestClass.class);
    }
}
