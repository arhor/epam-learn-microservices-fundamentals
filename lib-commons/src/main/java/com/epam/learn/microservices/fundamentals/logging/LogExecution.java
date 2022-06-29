package com.epam.learn.microservices.fundamentals.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogExecution {

    Level level() default Level.DEBUG;

    enum Level {
        DEBUG,
        INFO
    }
}
