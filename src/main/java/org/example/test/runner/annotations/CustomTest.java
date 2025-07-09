package org.example.test.runner.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomTest {
    Class<? extends Throwable> expected() default None.class;

    class None extends Throwable {
        private None() {
        }
    }
}