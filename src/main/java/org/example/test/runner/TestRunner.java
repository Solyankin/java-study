package org.example.test.runner;

import org.example.test.runner.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class TestRunner {
    public static void runTests(Class<?> testClass) {
        try {
            validateSuiteAnnotations(testClass);
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            runBeforeSuite(testClass);
            List<Method> testMethods = getSortedTestMethods(testClass);

            for (Method testMethod : testMethods) {
                runSingleTest(testInstance, testMethod);
            }

            runAfterSuite(testClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateSuiteAnnotations(Class<?> testClass) {
        long beforeSuiteCount = Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(BeforeSuite.class))
                .count();

        if (beforeSuiteCount > 1) {
            throw new RuntimeException("Only one @BeforeSuite method allowed");
        }

        long afterSuiteCount = Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AfterSuite.class))
                .count();

        if (afterSuiteCount > 1) {
            throw new RuntimeException("Only one @AfterSuite method allowed");
        }
    }

    private static void runBeforeSuite(Class<?> testClass) throws Exception {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                checkStatic(method, "@BeforeSuite");
                method.invoke(null);
            }
        }
    }

    private static void runAfterSuite(Class<?> testClass) throws Exception {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterSuite.class)) {
                checkStatic(method, "@AfterSuite");
                method.invoke(null);
            }
        }
    }

    private static List<Method> getSortedTestMethods(Class<?> testClass) {
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        // Сортируем по приоритету (по убыванию)
        testMethods.sort((m1, m2) -> {
            int p1 = m1.getAnnotation(Test.class).priority();
            int p2 = m2.getAnnotation(Test.class).priority();
            return Integer.compare(p2, p1);
        });

        return testMethods;
    }

    private static void runSingleTest(Object testInstance, Method testMethod) throws Exception {
        System.out.println("\n--- Starting test: " + testMethod.getName() + " ---");

        runBeforeAfterMethods(testInstance, testMethod.getDeclaringClass(), BeforeTest.class);

        // Выполняем сам тест
        try {
            testMethod.invoke(testInstance);
            System.out.println("Test PASSED");
        } catch (InvocationTargetException e) {
            System.out.println("Test FAILED: " + e.getCause().getMessage());
        }

        // Выполняем AfterTest методы
        runBeforeAfterMethods(testInstance, testMethod.getDeclaringClass(), AfterTest.class);
    }

    private static void runBeforeAfterMethods(Object testInstance, Class<?> testClass,
                                              Class<? extends Annotation> annotation) throws Exception {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                method.invoke(testInstance);
            }
        }
    }

    private static void checkStatic(Method method, String annotationName) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new RuntimeException(annotationName + " method must be static: " + method.getName());
        }
    }
}