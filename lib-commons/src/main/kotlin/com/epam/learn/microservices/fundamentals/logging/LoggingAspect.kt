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
    fun logMethodExecution(joinPoint: ProceedingJoinPoint, annotation: LogExecution): Any? {
        val logger = joinPoint.componentLogger

        return if (logger.isLevelEnabled(annotation.level)) {
            val signature = joinPoint.signature
            val signatureName = "${signature.declaringType.simpleName}.${signature.name}()"

            logger.log(annotation.level, "Method: {}, Args: {}", signatureName, joinPoint.args.joinToString())
            val result = joinPoint.proceed()
            logger.log(annotation.level, "Method: {}, Result: {}", signatureName, result)

            result
        } else {
            joinPoint.proceed()
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

    private fun Logger.isLevelEnabled(level: LogExecution.Level): Boolean {
        return when (level) {
            LogExecution.Level.DEBUG -> isDebugEnabled
            LogExecution.Level.INFO -> isInfoEnabled
        }
    }

    private fun Logger.log(level: LogExecution.Level, format: String, vararg arguments: Any) {
        when (level) {
            LogExecution.Level.DEBUG -> debug(format, *arguments)
            LogExecution.Level.INFO -> info(format, *arguments)
        }
    }
}

