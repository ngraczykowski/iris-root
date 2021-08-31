package com.silenteight.sep.filestorage.minio.manager;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.StorageManager;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioStorageManager implements StorageManager {

  private final MinioClient minioClient;

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
      return minioClient.bucketExists(BucketExistsArgs.builder().bucket(storageName).build());
    } catch (Exception e) {
      throw new BucketManageException(
          format("Check for if bucket with name %s failed", storageName));
    }
  }

  void createBucket(String bucketName) {
    try {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    } catch (Exception e) {
      throw new BucketManageException(
          format("Create bucket with name %s failed.", bucketName));
    }
  }

  void removeBucket(String bucketName) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    } catch (Exception e) {
      throw new BucketManageException(format("Remove bucket with name %s failed", bucketName));
    }
  }
}
