package com.epam.learn.microservices.fundamentals.resource.service.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration(proxyBeanMethods = false)
class AmazonS3Config {

    @ConstructorBinding
    @ConfigurationProperties("configuration.aws.s3")
    data class Props(
        val region: String,
        val url: String,
        val bucketName: String,
        val accessKey: String,
        val secretKey: String,
    )

    @Bean
    fun amazonS3(props: Props): AmazonS3 {
        val credentials = AWSStaticCredentialsProvider(
            BasicAWSCredentials(
                props.accessKey,
                props.secretKey
            )
        )
        val endpointConfiguration = AwsClientBuilder.EndpointConfiguration(
            props.url,
            props.region
        )
        val amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(credentials)
            .withEndpointConfiguration(endpointConfiguration)
            .build()

        if (!amazonS3.doesBucketExistV2(props.bucketName)) {
            amazonS3.createBucket(props.bucketName)
        }
        return amazonS3
    }
}
