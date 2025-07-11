package org.example.logging;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Aspect
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private final LoggingProperties properties;

    public LoggingAspect(LoggingProperties properties) {
        this.properties = properties;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Service)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!shouldLog(joinPoint)) {
            return joinPoint.proceed();
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        boolean isService = joinPoint.getTarget().getClass().isAnnotationPresent(Service.class);
        String componentType = isService ? "SERVICE" : "CONTROLLER";

        // Логирование аргументов
        if (properties.isLogMethodArgs()) {
            log.info("[{}] {}.{}() - args: {}",
                    componentType, className, methodName, joinPoint.getArgs());
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        // Логирование результата
        if (properties.isLogMethodResult()) {
            String logMessage = String.format("[%s] %s.%s() - result: %s",
                    componentType, className, methodName, result);

            if (properties.isLogExecutionTime()) {
                logMessage += String.format(" (executed in %d ms)", executionTime);
            }

            log.info(logMessage);
        }

        return result;
    }

    private boolean shouldLog(ProceedingJoinPoint joinPoint) {
        if (!properties.isEnabled()) {
            return false;
        }

        boolean isService = joinPoint.getTarget().getClass().isAnnotationPresent(Service.class);
        return (isService && properties.isServiceEnabled()) ||
                (!isService && properties.isRestControllerEnabled());
    }
}