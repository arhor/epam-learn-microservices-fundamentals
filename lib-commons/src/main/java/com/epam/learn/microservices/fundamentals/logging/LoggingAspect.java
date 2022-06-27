package com.epam.learn.microservices.fundamentals.logging;

import com.epam.learn.microservices.fundamentals.logging.LogExecution.Level;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {

    @Around(value = "executionLogging()")
    public Object logMethodExecution(final ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = findAnnotation(joinPoint);

        if (annotation != null) {
            var logger = componentLogger(joinPoint);
            var level = annotation.level();

            if (isLevelEnabled(logger, level)) {
                var signature = joinPoint.getSignature();
                var signatureName = "%s.%s()".formatted(
                    signature.getDeclaringType().getSimpleName(),
                    signature.getName()
                );

                log(logger, level, "Method: {}, Args: {}", signatureName, joinPoint.getArgs());
                var result = joinPoint.proceed();
                log(logger, level, "Method: {}, Result: {}", signatureName, result);

                return result;
            }
        }
        return joinPoint.proceed();
    }

    @AfterThrowing(pointcut = "executionLogging()", throwing = "exception")
    public void logException(final JoinPoint joinPoint, final Throwable exception) {
        componentLogger(joinPoint).error("An error occurred", exception);
    }

    @Pointcut("within(@com.epam.learn.microservices.fundamentals.logging.LogExecution *)")
    private void annotatedClass() { /* no-op */ }

    @Pointcut("@annotation(com.epam.learn.microservices.fundamentals.logging.LogExecution)")
    private void annotatedMethod() { /* no-op */ }

    @Pointcut("execution(public * *(..))")
    private void publicMethod() { /* no-op */ }

    @Pointcut("(annotatedClass() || annotatedMethod()) && publicMethod()")
    private void executionLogging() { /* no-op */ }

    private Logger componentLogger(final JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    private LogExecution findAnnotation(final JoinPoint joinPoint) {
        if (joinPoint.getSignature() instanceof MethodSignature signature) {
            var annotationClass = LogExecution.class;
            var method = signature.getMethod();
            var annotation = method.getAnnotation(annotationClass);

            if (annotation == null) {
                var declaringClass = method.getDeclaringClass();

                annotation = declaringClass.getAnnotation(annotationClass);
            }
            return annotation;
        }
        return null;
    }

    private boolean isLevelEnabled(final Logger logger, final Level level) {
        return switch (level) {
            case DEBUG -> logger.isDebugEnabled();
            case INFO -> logger.isInfoEnabled();
        };
    }

    private void log(final Logger logger, final Level level, final String msg, final Object... ars) {
        switch (level) {
            case DEBUG -> logger.debug(msg, ars);
            case INFO -> logger.info(msg, ars);
        }
    }
}

