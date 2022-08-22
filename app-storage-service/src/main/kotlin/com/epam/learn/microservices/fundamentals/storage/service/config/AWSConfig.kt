package com.epam.learn.microservices.fundamentals.storage.service.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.epam.learn.microservices.fundamentals.storage.service.config.props.AWSProps
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AWSConfig(private val awsProps: AWSProps) {

    @Bean
    fun amazonS3(credentials: AWSCredentialsProvider, endpointConfiguration: EndpointConfiguration): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(credentials)
            .withEndpointConfiguration(endpointConfiguration)
            .withPathStyleAccessEnabled(true)
            .build()
    }

    @Bean
    fun awsCredentialsProvider(): AWSCredentialsProvider {
        return AWSStaticCredentialsProvider(
            BasicAWSCredentials(
                awsProps.accessKey,
                awsProps.secretKey,
            )
        )
    }

    @Bean
    fun endpointConfiguration(): EndpointConfiguration {
        return EndpointConfiguration(
            awsProps.url,
            awsProps.region,
        )
    }
}
