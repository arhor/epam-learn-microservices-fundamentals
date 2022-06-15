package com.epam.learn.microservices.fundamentals.song.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class SongServiceRunner

fun main(args: Array<String>) {
    runApplication<SongServiceRunner>(*args)
}
