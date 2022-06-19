package com.epam.learn.microservices.fundamentals.resource.service.data.repository.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion
import com.amazonaws.services.s3.model.ObjectMetadata
import com.epam.learn.microservices.fundamentals.resource.service.config.AWSConfig.S3Props
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import org.springframework.stereotype.Repository
import java.io.InputStream

@Repository
class ResourceDataRepositoryImpl(
    private val s3Client: AmazonS3,
    private val s3Props: S3Props,
) : ResourceDataRepository {

    override fun upload(filename: String, data: InputStream, size: Long) {
        s3Client.putObject(
            s3Props.bucket,
            filename,
            data,
            ObjectMetadata().apply {
                contentLength = size
            }
        )
    }

    override fun download(filename: String): Pair<InputStream, Long> {
        val result = s3Client.getObject(s3Props.bucket, filename)

        val data = result.objectContent
        val size = result.objectMetadata.contentLength

        return data to size
    }

    override fun delete(filenames: Iterable<String>): List<String> {
        val result = s3Client.deleteObjects(
            DeleteObjectsRequest(s3Props.bucket).apply {
                keys = filenames.map(::KeyVersion)
            }
        )

        return result.deletedObjects.map { it.key }
    }
}
