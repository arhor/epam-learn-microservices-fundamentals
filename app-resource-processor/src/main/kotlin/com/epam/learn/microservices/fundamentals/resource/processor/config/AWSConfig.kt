package com.epam.learn.microservices.fundamentals.resource.processor.config

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.epam.learn.microservices.fundamentals.resource.processor.config.props.AWSProps
import com.epam.learn.microservices.fundamentals.resource.processor.config.props.SQSProps
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.ConnectionFactory

@Configuration(proxyBeanMethods = false)
class AWSConfig(private val awsProps: AWSProps) {

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

    @Bean
    fun sqsConnectionFactory(amazonSQS: AmazonSQS): ConnectionFactory {
        return SQSConnectionFactory(ProviderConfiguration(), amazonSQS)
    }
}
