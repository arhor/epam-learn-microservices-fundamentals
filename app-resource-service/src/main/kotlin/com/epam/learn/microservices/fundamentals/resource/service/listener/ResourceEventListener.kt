package com.epam.learn.microservices.fundamentals.resource.service.listener

import com.epam.learn.microservices.fundamentals.event.ResourceEvent
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageServiceClient
import com.epam.learn.microservices.fundamentals.resource.service.client.StorageType.PERMANENT
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@LogExecution
class ResourceEventListener(
    private val resourceRepository: ResourceRepository,
    private val resourceDataRepository: ResourceDataRepository,
    private val storageServiceClient: StorageServiceClient,
) {

    @JmsListener(destination = "resource-handled-events")
    fun processResourceHandledEvent(event: ResourceEvent.Handled) {
        resourceRepository.findByIdOrNull(event.id)?.let {
            val stagingStorage = storageServiceClient.getStorageId(it.storageId)
            val permanentStorage = storageServiceClient.getStorages(type = PERMANENT, single = true).single()

            val id: Long by permanentStorage
            val name: String by permanentStorage

            resourceRepository.save(
                it.copy(storageId = id)
            )

            resourceDataRepository.move(
                sourceBucket = stagingStorage.name,
                sourceFilename = it.filename,
                targetBucket = name,
                targetFilename = it.filename
            )
        }
    }
}
