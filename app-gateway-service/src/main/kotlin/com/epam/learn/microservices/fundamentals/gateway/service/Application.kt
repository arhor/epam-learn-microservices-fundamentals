package com.epam.learn.microservices.fundamentals.gateway.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession

@EnableWebFluxSecurity
@EnableRedisWebSession
@SpringBootApplication
class Application {

    @Bean
    fun webSessionServerOAuth2AuthorizedClientRepository(): ServerOAuth2AuthorizedClientRepository {
        return WebSessionServerOAuth2AuthorizedClientRepository()
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        serverOAuth2AuthorizedClientRepository: ServerOAuth2AuthorizedClientRepository,
    ) = http {
        authorizeExchange {
            authorize(anyExchange, permitAll)
            authorize("/api/storages/**", authenticated)
        }
        formLogin {}
        oauth2Login {
            authorizedClientRepository = serverOAuth2AuthorizedClientRepository
        }
        csrf { disable() }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
