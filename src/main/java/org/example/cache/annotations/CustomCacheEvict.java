package org.example.cache.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomCacheEvict {

    @AliasFor("value")
    String cacheName() default "";

    @AliasFor("cacheName")
    String value() default "";

    String key() default "";

    boolean allEntries() default false;
}