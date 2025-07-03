package org.example.cache.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.cache.annotations.CustomCacheEvict;
import org.example.cache.annotations.CustomCachePut;
import org.example.cache.annotations.CustomCacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class CustomCacheAspect {

    @Autowired
    private CacheManager cacheManager;

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();


    @Around("@annotation(customCacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, CustomCacheable customCacheable) throws Throwable {
        String cacheName = resolveCacheName(customCacheable);
        String keyExpression = customCacheable.key();

        Object key = resolveKey(joinPoint, keyExpression);
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            return joinPoint.proceed();
        }

        Cache.ValueWrapper cachedValue = cache.get(key);
        if (cachedValue != null) {
            return cachedValue.get();
        }

        Object result = joinPoint.proceed();
        cache.put(key, result);
        return result;
    }

    @Around("@annotation(customCachePut)")
    public Object handleCachePut(ProceedingJoinPoint joinPoint, CustomCachePut customCachePut) throws Throwable {
        String cacheName = resolveCacheName(customCachePut);
        String keyExpression = customCachePut.key();

        Object key = resolveKey(joinPoint, keyExpression);
        Object result = joinPoint.proceed();

        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, result);
        }

        return result;
    }

    @Around("@annotation(customCacheEvict)")
    public Object handleCacheEvict(ProceedingJoinPoint joinPoint, CustomCacheEvict customCacheEvict) throws Throwable {
        String cacheName = resolveCacheName(customCacheEvict);
        String keyExpression = customCacheEvict.key();
        boolean allEntries = customCacheEvict.allEntries();

        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            if (allEntries) {
                cache.clear();
            } else {
                Object key = resolveKey(joinPoint, keyExpression);
                cache.evict(key);
            }
        }

        return joinPoint.proceed();
    }

    private String resolveCacheName(CustomCacheable annotation) {
        if (!annotation.value().isEmpty()) {
            return annotation.value();
        }
        return annotation.cacheName();
    }

    private String resolveCacheName(CustomCachePut annotation) {
        if (!annotation.value().isEmpty()) {
            return annotation.value();
        }
        return annotation.cacheName();
    }

    private String resolveCacheName(CustomCacheEvict annotation) {
        if (!annotation.value().isEmpty()) {
            return annotation.value();
        }
        return annotation.cacheName();
    }

    private Object resolveKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        // Если выражение ключа не задано, используем параметры метода
        if (keyExpression == null || keyExpression.trim().isEmpty()) {
            return generateDefaultKey(joinPoint.getArgs());
        }

        // Для SpEL выражения создаем контекст с параметрами метода
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Кэшируем имена параметров для производительности
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            throw new IllegalStateException("Cannot resolve parameter names for method: " + method);
        }

        // Создаем контекст оценки с минимальными возможностями для безопасности
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        // Добавляем параметры в контекст
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // Добавляем дополнительные полезные переменные
        context.setVariable("methodName", method.getName());
        context.setVariable("target", joinPoint.getTarget());

        try {
            return SPEL_PARSER.parseExpression(keyExpression).getValue(context);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    String.format("Failed to parse key expression '%s' for method %s",
                            keyExpression, method), ex);
        }
    }

    private Object generateDefaultKey(Object[] args) {
        if (args.length == 0) {
            return SimpleKey.EMPTY;
        }
        if (args.length == 1) {
            return args[0] != null ? args[0] : SimpleKey.EMPTY;
        }
        return new SimpleKey(args);
    }
}