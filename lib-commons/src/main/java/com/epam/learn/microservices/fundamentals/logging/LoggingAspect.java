package com.epam.learn.microservices.fundamentals.logging;

import com.epam.learn.microservices.fundamentals.logging.LogExecution.Level;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
class LoggingAspect {

    @Around(value = "executionLogging(log)", argNames = "joinPoint,log")
    public Object logMethodExecution(final ProceedingJoinPoint joinPoint, final LogExecution log) throws Throwable {

        var logger = componentLogger(joinPoint);

        if (isLevelEnabled(logger, log.level())) {
            var signature = joinPoint.getSignature();
            var signatureName = "%s.%s()".formatted(
                signature.getDeclaringType().getSimpleName(),
                signature.getName()
            );

            log(logger, log.level(), "Method: {}, Args: {}", signatureName, joinPoint.getArgs());
            var result = joinPoint.proceed();
            log(logger, log.level(), "Method: {}, Result: {}", signatureName, result);

            return result;
        } else {
            return joinPoint.proceed();
        }
    }

    @AfterThrowing(pointcut = "executionLogging(log)", throwing = "exception", argNames = "joinPoint,exception,log")
    public void logException(final JoinPoint joinPoint, final Throwable exception, LogExecution log) {
        componentLogger(joinPoint).error("An error occurred", exception);
    }

    @Pointcut(value = "within(@log *)", argNames = "log")
    private void annotatedClass(LogExecution log) { /* no-op */ }

    @Pointcut(value = "@annotation(log)", argNames = "log")
    private void annotatedMethod(LogExecution log) { /* no-op */ }

    @Pointcut(value = "execution(public * *(..))")
    private void publicMethod() { /* no-op */ }

    @Pointcut(value = "(annotatedClass(log) || annotatedMethod(log)) && publicMethod()", argNames = "log")
    private void executionLogging(LogExecution log) { /* no-op */ }

    private Logger componentLogger(final JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
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

