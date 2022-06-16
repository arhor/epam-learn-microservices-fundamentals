package com.epam.learn.microservices.fundamentals.resource.service.data.repository.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.epam.learn.microservices.fundamentals.resource.service.config.S3Props
import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import org.springframework.stereotype.Repository
import java.io.ByteArrayInputStream
import java.io.InputStream

@Repository
class ResourceDataRepositoryImpl(
    private val s3Client: AmazonS3,
    private val s3Props: S3Props,
) : ResourceDataRepository {

    override fun upload(filename: String, data: ByteArray): ResourceMeta {
        val dataStream = ByteArrayInputStream(data)
        val metadata = ObjectMetadata().apply { contentLength = data.size.toLong() }

        val bucket = s3Props.bucket
        val result = s3Client.putObject(bucket, filename, dataStream, metadata)

        return ResourceMeta(
            filename = filename,
            uploaded = result != null,
            length = result?.metadata?.contentLength ?: 0
        )
    }

    override fun download(filename: String): InputStream {
        val bucketName = s3Props.bucket
        val s3Object = s3Client.getObject(bucketName, filename)

        return s3Object.objectContent
    }

    override fun delete(filenames: Iterable<String>): List<String> {
        val bucketName = s3Props.bucket
        val filesToDelete = filenames.toList().toTypedArray()
        val request = DeleteObjectsRequest(bucketName).withKeys(*filesToDelete)

        val result = s3Client.deleteObjects(request)

        return result.deletedObjects.map { it.key }
    }
}
