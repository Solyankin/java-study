package org.example.test.runner;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

public class ClassTestDescriptor extends AbstractTestDescriptor {
    private final Class<?> testClass;

    public ClassTestDescriptor(UniqueId uniqueId, Class<?> testClass) {
        super(uniqueId, testClass.getSimpleName(), ClassSource.from(testClass));
        this.testClass = testClass;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }
}