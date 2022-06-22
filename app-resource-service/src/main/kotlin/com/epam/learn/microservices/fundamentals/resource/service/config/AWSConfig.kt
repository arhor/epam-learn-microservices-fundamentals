package com.epam.learn.microservices.fundamentals.resource.service.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class AWSConfig {

    @ConstructorBinding
    @ConfigurationProperties("configuration.aws")
    data class Props(
        val url: String,
        val region: String,
        val accessKey: String,
        val secretKey: String,
    )

    @ConstructorBinding
    @ConfigurationProperties("configuration.aws.s3")
    data class S3Props(
        val bucket: String,
    )

    @Bean
    fun amazonS3(
        s3Props: S3Props,
        credentialsProvider: AWSCredentialsProvider,
        endpointConfiguration: EndpointConfiguration,
    ): AmazonS3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(credentialsProvider)
        .withEndpointConfiguration(endpointConfiguration)
        .build()
        .also { it.createBucketIfNotExists(s3Props.bucket) }

    @Bean
    fun amazonSQS(
        credentialsProvider: AWSCredentialsProvider,
        endpointConfiguration: EndpointConfiguration,
    ): AmazonSQS = AmazonSQSClientBuilder
        .standard()
        .withCredentials(credentialsProvider)
        .withEndpointConfiguration(endpointConfiguration)
        .build()

    @Bean
    fun awsCredentialsProvider(props: Props): AWSCredentialsProvider {
        return AWSStaticCredentialsProvider(
            BasicAWSCredentials(
                props.accessKey,
                props.secretKey,
            )
        )
    }

    @Bean
    fun endpointConfiguration(props: Props): EndpointConfiguration {
        return EndpointConfiguration(
            props.url,
            props.region,
        )
    }

    private fun AmazonS3.createBucketIfNotExists(bucket: String) {
        val shouldCreateBucket = !doesBucketExistV2(bucket)

        if (shouldCreateBucket) {
            createBucket(bucket)
        }
    }
}
