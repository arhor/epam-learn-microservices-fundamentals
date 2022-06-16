package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import java.io.InputStream

interface ResourceDataRepository {

    fun upload(filename: String, data: InputStream, size: Long): ResourceMeta

    fun download(filename: String): Pair<InputStream, Long>

    fun delete(filenames: Iterable<String>): List<String>
}
