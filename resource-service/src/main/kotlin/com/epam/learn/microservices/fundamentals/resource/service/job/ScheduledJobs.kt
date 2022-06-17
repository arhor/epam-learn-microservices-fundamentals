package com.epam.learn.microservices.fundamentals.resource.service.job

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ScheduledJobs(private val service: ResourceService) {

    @Scheduled(initialDelay = 10, fixedRate = 300, timeUnit = TimeUnit.SECONDS)
    fun extractAndSaveMetadata() {
        service.resetOutdatedPendingResourcesStatus()
    }
}
