package com.silenteight.sep.filestorage.minio.manager;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.StorageManager;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;

import static java.lang.String.format;

@RequiredArgsConstructor
public class S3MinioStorageManager implements StorageManager {

  private final S3Client s3Client;

  @Override
  public void create(String storageName) {
    createBucket(storageName);
  }

  @Override
  public void remove(String storageName) {
    removeBucket(storageName);
  }

  @Override
  public boolean exist(String storageName) {
    try {
      return s3Client.listBuckets()
          .buckets()
          .stream()
          .anyMatch(bucket -> bucketHasName(storageName, bucket));

    } catch (Exception e) {
      throw new BucketManageException(
          format("Check for if bucket with name exists %s failed", storageName));
    }
  }

  private boolean bucketHasName(String storageName, Bucket bucket) {
    return bucket.name().equals(storageName);
  }

  void createBucket(String bucketName) {
    try {
      s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    } catch (Exception e) {
      throw new BucketManageException(
          format("Create bucket with name %s failed.", bucketName));
    }
  }

  void removeBucket(String bucketName) {
    try {
      s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
    } catch (Exception e) {
      throw new BucketManageException(format("Remove bucket with name %s failed", bucketName));
    }
  }
}
