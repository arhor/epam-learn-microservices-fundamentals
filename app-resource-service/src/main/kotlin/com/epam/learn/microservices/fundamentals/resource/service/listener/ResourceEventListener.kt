package com.epam.learn.microservices.fundamentals.resource.service.listener

import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageResponseDTO
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageServiceClient
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageType.PERMANENT
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles

@Component
@LogExecution
class ResourceEventListener(
    private val resourceRepository: ResourceRepository,
    private val resourceDataRepository: ResourceDataRepository,
    private val storageServiceClient: StorageServiceClient,
) {

    @Autowired
    private lateinit var circuitBreakerFactory: CircuitBreakerFactory<*, *>

    @JmsListener(destination = "resource-handled-events")
    fun processResourceHandledEvent(event: ResourceEvent.Handled) {
        resourceRepository.findByIdOrNull(event.id)?.let { resource ->

            val stagingStorageName =
                resource.storageId.takeIf { it != 1L }
                    ?.let(storageServiceClient::getStorageId)
                    ?.let(StorageResponseDTO::name)
                    ?: "default-storage-staging"

            val permanentStorage = circuitBreakerFactory.create("storage-service").run({
                storageServiceClient.getStorages(
                    type = PERMANENT,
                    single = true,
                ).single()
            }) {
                log.error("Using fallback permanent bucket", it)
                mapOf(
                    "id" to 2L,
                    "name" to "default-storage-permanent"
                )
            }

            val id: Long by permanentStorage
            val name: String by permanentStorage

            resourceRepository.save(
                resource.copy(storageId = id)
            )

            resourceDataRepository.move(
                sourceBucket = stagingStorageName,
                sourceFilename = resource.filename,
                targetBucket = name,
                targetFilename = resource.filename
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
