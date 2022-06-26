package com.epam.learn.microservices.fundamentals.logging

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecution(val level: Level = Level.DEBUG) {

    enum class Level {
        DEBUG,
        INFO
    }
}
