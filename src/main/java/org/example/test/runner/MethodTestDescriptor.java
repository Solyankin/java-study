package org.example.test.runner;

import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.*;
import java.lang.reflect.*;

public class MethodTestDescriptor extends AbstractTestDescriptor {
    private final Method testMethod;

    public MethodTestDescriptor(UniqueId uniqueId, Method testMethod) {
        super(uniqueId, testMethod.getName(),
                MethodSource.from(testMethod.getDeclaringClass(), testMethod));
        this.testMethod = testMethod;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }
}