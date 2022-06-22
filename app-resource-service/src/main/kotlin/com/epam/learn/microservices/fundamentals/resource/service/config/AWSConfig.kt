package com.epam.learn.microservices.fundamentals.resource.service.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.epam.learn.microservices.fundamentals.resource.service.config.props.AWSProps
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
            .build()
    }

    @Bean
    fun amazonSQS(credentials: AWSCredentialsProvider, endpointConfiguration: EndpointConfiguration): AmazonSQS {
        return AmazonSQSClientBuilder
            .standard()
            .withCredentials(credentials)
            .withEndpointConfiguration(endpointConfiguration)
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
