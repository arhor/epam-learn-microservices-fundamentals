package com.epam.learn.microservices.fundamentals.resource.service.data.repository.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion
import com.amazonaws.services.s3.model.ObjectMetadata
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceDataRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.lang.invoke.MethodHandles

@Repository
@LogExecution
class ResourceDataRepositoryImpl(private val s3Client: AmazonS3) : ResourceDataRepository {

    override fun upload(bucket: String, filename: String, data: InputStream, size: Long) {
        log.info("Uploading data - bucket: $bucket, filename: $filename, size: $size")

        s3Client.putObject(
            bucket,
            filename,
            data,
            ObjectMetadata().apply {
                contentLength = size
            }
        )
    }

    override fun download(bucket: String, filename: String): Pair<InputStream, Long> {
        val result = s3Client.getObject(bucket, filename)

        val data = result.objectContent
        val size = result.objectMetadata.contentLength

        return data to size
    }

    override fun delete(filenamesByBucket: Map<String, List<String>>): Map<String, List<String>> {
        val deletedFilesByBucket = HashMap<String, List<String>>()

        for ((bucket, filenames) in filenamesByBucket) {
            val result = s3Client.deleteObjects(
                DeleteObjectsRequest(bucket).apply {
                    keys = filenames.map(::KeyVersion)
                }
            )
            if (result.deletedObjects.isNotEmpty()) {
                deletedFilesByBucket[bucket] = result.deletedObjects.map { it.key }
            }
        }
        return deletedFilesByBucket
    }

    override fun move(sourceBucket: String, sourceFilename: String, targetBucket: String, targetFilename: String) {
        s3Client.copyObject(sourceBucket, sourceFilename, targetBucket, targetFilename)
        s3Client.deleteObject(sourceBucket, sourceFilename)
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
