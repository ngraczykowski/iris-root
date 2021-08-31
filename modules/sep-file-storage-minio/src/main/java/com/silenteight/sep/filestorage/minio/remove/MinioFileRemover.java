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
  public void removeFile(String storageName, String fileName) {
    try {
      deleteFile(storageName, fileName);
    } catch (Exception e) {
      throw new FileRemoveFailedException(
          format("Remove file %s from bucket %s failed", fileName, storageName));
    }
  }

  private void deleteFile(String bucketName, String fileName) throws Exception {
    minioClient.removeObject(prepareObjectBasedOnFileToRemove(bucketName, fileName));
  }

  private RemoveObjectArgs prepareObjectBasedOnFileToRemove(String bucketName, String fileName) {
    return RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object(fileName)
        .build();
  }
}
