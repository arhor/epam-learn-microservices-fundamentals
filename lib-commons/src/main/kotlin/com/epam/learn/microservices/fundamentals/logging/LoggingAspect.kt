package com.epam.learn.microservices.fundamentals.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Aspect
class LoggingAspect {

    @Around("executionLogging()")
    fun logMethodExecution(joinPoint: ProceedingJoinPoint): Any? {
        val log = joinPoint.componentLogger
        if (log.isDebugEnabled) {
            val signature = joinPoint.signature
            val signatureName = "${signature.declaringType.simpleName}.${signature.name}()"
            log.debug(
                "Method: {}, Args: {}",
                signatureName,
                joinPoint.args.joinToString()
            )
            val result = joinPoint.proceed()
            log.debug(
                "Method: {}, Result: {}",
                signatureName,
                result
            )
            return result
        } else {
            return joinPoint.proceed()
        }
    }

    @AfterThrowing(pointcut = "executionLogging()", throwing = "exception")
    fun logException(joinPoint: JoinPoint, exception: Throwable) {
        joinPoint.componentLogger.error("An error occurred", exception)
    }

    @Pointcut("within(@com.epam.learn.microservices.fundamentals.logging.LogExecution *)")
    private fun annotatedClass() {
        /* no-op */
    }

    @Pointcut("@annotation(com.epam.learn.microservices.fundamentals.logging.LogExecution)")
    private fun annotatedMethod() {
        /* no-op */
    }

    @Pointcut("execution(public * *(..))")
    private fun publicMethodExecution() {
        /* no-op */
    }

    @Pointcut("(annotatedClass() || annotatedMethod()) && publicMethodExecution()")
    private fun executionLogging() {
        /* no-op */
    }

    private val JoinPoint.componentLogger: Logger
        get() = LoggerFactory.getLogger(signature.declaringTypeName)
}

