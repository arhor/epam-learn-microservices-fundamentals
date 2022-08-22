package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import java.io.InputStream

interface ResourceDataRepository {

    fun upload(bucket: String, filename: String, data: InputStream, size: Long)

    fun download(bucket: String, filename: String): Pair<InputStream, Long>

    fun delete(filenamesByBucket: Map<String, List<String>>): Map<String, List<String>>

    fun move(sourceBucket: String, sourceFilename: String, targetBucket: String, targetFilename: String)
}
