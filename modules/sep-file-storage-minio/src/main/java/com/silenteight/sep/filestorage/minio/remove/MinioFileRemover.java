package com.silenteight.sep.filestorage.minio.remove;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileRemover;

import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioFileRemover implements FileRemover {

  private final MinioClient minioClient;

  @Override
  public void removeFile(String fileName, String storageName) {
    try {
      deleteFile(fileName, storageName);
    } catch (Exception e) {
      throw new FileRemoveFailedException(
          format("Remove file %s from bucket %s failed", fileName, storageName));
    }
  }

  private void deleteFile(String fileName, String bucketName) throws Exception {
    minioClient.removeObject(prepareObjectBasedOnFileToRemove(fileName, bucketName));
  }

  private RemoveObjectArgs prepareObjectBasedOnFileToRemove(String fileName, String bucketName) {
    return RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object(fileName)
        .build();
  }
}
