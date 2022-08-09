package com.epam.learn.microservices.fundamentals.storage.service.config

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

@Configuration(proxyBeanMethods = false)
class LocalizationConfig {

    @Bean
    fun messages(webProperties: ObjectProvider<WebProperties>): MessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:messages")
            setDefaultEncoding("UTF-8")
            setDefaultLocale(webProperties.ifAvailable?.locale ?: Locale.ENGLISH)
        }
    }
}
