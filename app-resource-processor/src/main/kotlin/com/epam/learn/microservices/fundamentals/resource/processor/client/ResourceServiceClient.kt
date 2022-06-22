package com.epam.learn.microservices.fundamentals.resource.processor.client

import java.io.InputStream

interface ResourceServiceClient {

    fun fetchUnprocessedResourceIds(): List<Long>

    fun fetchResourceBinaryData(id: Long): InputStream

    fun updateResourceStatus(id: Long, status: String)
}
