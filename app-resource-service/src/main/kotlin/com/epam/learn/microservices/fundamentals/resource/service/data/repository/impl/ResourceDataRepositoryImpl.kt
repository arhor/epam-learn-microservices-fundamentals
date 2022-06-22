package com.epam.learn.microservices.fundamentals.resource.service.data.repository.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion
import com.amazonaws.services.s3.model.ObjectMetadata
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.config.props.S3Props
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import org.springframework.stereotype.Repository
import java.io.InputStream
import javax.annotation.PostConstruct

@Repository
@LogExecution
class ResourceDataRepositoryImpl(
    private val s3Client: AmazonS3,
    private val s3Props: S3Props,
) : ResourceDataRepository {

    @PostConstruct
    fun init() {
        val shouldCreateBucket = !s3Client.doesBucketExistV2(s3Props.bucket)

        if (shouldCreateBucket) {
            s3Client.createBucket(s3Props.bucket)
        }
    }

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
