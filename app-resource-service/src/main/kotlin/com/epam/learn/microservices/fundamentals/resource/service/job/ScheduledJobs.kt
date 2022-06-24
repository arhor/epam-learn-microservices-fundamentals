package com.epam.learn.microservices.fundamentals.resource.service.job

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles
import java.util.concurrent.TimeUnit

@Component
class ScheduledJobs(private val service: ResourceService) {

//    @Scheduled(initialDelay = 10, fixedRate = 300, timeUnit = TimeUnit.SECONDS)
    fun extractAndSaveMetadata() {
        log.info("Resetting outdated pending resources")
        val resourcesUpdated = service.resetOutdatedPendingResourcesStatus()
        log.info("Resources returned to initial status: {}", resourcesUpdated)
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
