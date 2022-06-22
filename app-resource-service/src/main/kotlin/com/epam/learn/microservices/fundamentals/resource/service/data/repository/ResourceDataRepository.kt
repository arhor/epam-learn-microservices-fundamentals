package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import java.io.InputStream

interface ResourceDataRepository {

    fun upload(filename: String, data: InputStream, size: Long)

    fun download(filename: String): Pair<InputStream, Long>

    fun delete(filenames: Iterable<String>): List<String>
}
