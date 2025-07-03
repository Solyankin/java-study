package org.example.test.runner;


import org.example.test.runner.annotations.CustomTest;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.util.Arrays;

public class CustomTestEngine implements TestEngine {
    private static final String ENGINE_ID = "my-test-engine";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(uniqueId, "My Test Engine");

        discoveryRequest.getSelectorsByType(ClassSelector.class).forEach(selector -> {
            Class<?> testClass = selector.getJavaClass();
            if (isTestClass(testClass)) {
                engineDescriptor.addChild(new ClassTestDescriptor(
                        engineDescriptor.getUniqueId().append("class", testClass.getName()),
                        testClass
                ));
            }
        });

        return engineDescriptor;
    }

    @Override
    public void execute(ExecutionRequest request) {
        TestDescriptor rootDescriptor = request.getRootTestDescriptor();
        EngineExecutionListener listener = request.getEngineExecutionListener();

        listener.executionStarted(rootDescriptor);

        try {
            executeTests(rootDescriptor, listener);
            listener.executionFinished(rootDescriptor, TestExecutionResult.successful());
        } catch (Exception e) {
            listener.executionFinished(rootDescriptor, TestExecutionResult.failed(e));
        }
    }

    private void executeTests(TestDescriptor descriptor, EngineExecutionListener listener) {
        if (descriptor instanceof ClassTestDescriptor) {
            ClassTestDescriptor classDescriptor = (ClassTestDescriptor) descriptor;
            listener.executionStarted(classDescriptor);

            try {
                Object testInstance = classDescriptor.getTestClass().getDeclaredConstructor().newInstance();

                for (TestDescriptor child : classDescriptor.getChildren()) {
                    if (child instanceof MethodTestDescriptor) {
                        executeTestMethod((MethodTestDescriptor) child, testInstance, listener);
                    }
                }

                listener.executionFinished(classDescriptor, TestExecutionResult.successful());
            } catch (Exception e) {
                listener.executionFinished(classDescriptor, TestExecutionResult.failed(e));
            }
        }
    }

    private void executeTestMethod(MethodTestDescriptor descriptor,
                                   Object testInstance,
                                   EngineExecutionListener listener) {
        listener.executionStarted(descriptor);

        try {
            descriptor.getTestMethod().invoke(testInstance);
            listener.executionFinished(descriptor, TestExecutionResult.successful());
        } catch (Exception e) {
            listener.executionFinished(descriptor, TestExecutionResult.failed(e));
        }
    }

    private boolean isTestClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(m -> m.isAnnotationPresent(CustomTest.class));
    }
}