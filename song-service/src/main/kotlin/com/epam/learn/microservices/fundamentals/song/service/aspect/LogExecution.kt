package com.epam.learn.microservices.fundamentals.song.service.aspect

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecution()
