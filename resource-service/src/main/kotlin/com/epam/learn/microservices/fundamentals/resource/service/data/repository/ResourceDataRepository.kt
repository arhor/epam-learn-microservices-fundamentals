package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import java.io.InputStream

interface ResourceDataRepository {

    fun upload(filename: String, data: ByteArray): ResourceMeta

    fun download(filename: String): InputStream

    fun delete(filenames: Iterable<String>): List<String>
}
