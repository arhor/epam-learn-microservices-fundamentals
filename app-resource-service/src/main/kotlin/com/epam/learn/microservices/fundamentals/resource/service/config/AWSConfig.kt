package com.epam.learn.microservices.fundamentals.resource.service.config

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.epam.learn.microservices.fundamentals.resource.service.config.props.AWSProps
import com.epam.learn.microservices.fundamentals.resource.service.config.props.SQSProps
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.invoke.MethodHandles

@Configuration(proxyBeanMethods = false)
class AWSConfig(private val awsProps: AWSProps, private val sqsProps: SQSProps) {

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

    @Bean
    fun sqsConnectionFactory(amazonSQS: AmazonSQS): SQSConnectionFactory {
        return SQSConnectionFactory(ProviderConfiguration(), amazonSQS).apply {
            createConnection().apply {
                try {
                    with(wrappedAmazonSQSClient) {
                        if (!queueExists(sqsProps.queue)) {
                            log.info("Trying to create SQS queue '${sqsProps.queue}'")
                            createQueue(sqsProps.queue)
                            log.info("SQS queue '${sqsProps.queue}' created")
                        }
                    }
                } finally {
                    close()
                }
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
