package com.epam.learn.microservices.fundamentals.resource.service.service.event

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.QueueNameExistsException
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.epam.learn.microservices.fundamentals.resource.service.config.props.SQSProps
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.lang.invoke.MethodHandles
import javax.annotation.PostConstruct

@Service
class ResourceCreatedEventHandler(private val sqsClient: AmazonSQS, private val sqsProps: SQSProps) {

    private lateinit var queueUrl: String

    @PostConstruct
    fun init() {
        queueUrl = try {
            log.info("Trying to create SQS queue '${sqsProps.queue}'")
            sqsClient.createQueue(sqsProps.queue).queueUrl
        } catch (e: QueueNameExistsException) {
            log.info("SQS queue '${sqsProps.queue}' already exists")
            sqsClient.getQueueUrl(sqsProps.queue).queueUrl
        }
    }

    @Async
    @EventListener(ResourceCreatedEvent::class)
    fun publishEvent(event: ResourceCreatedEvent) {
        sqsClient.sendMessage(
            SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(event.id.toString())
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
