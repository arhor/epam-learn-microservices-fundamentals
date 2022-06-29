package com.epam.learn.microservices.fundamentals.resource.processor.client

import java.io.InputStream

interface ResourceServiceClient {

    fun fetchResourceBinaryData(id: Long): InputStream
}
