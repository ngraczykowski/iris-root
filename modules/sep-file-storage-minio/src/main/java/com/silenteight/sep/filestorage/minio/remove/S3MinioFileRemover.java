package com.silenteight.sep.filestorage.minio.remove;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileRemover;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import static java.lang.String.format;

@RequiredArgsConstructor
public class S3MinioFileRemover implements FileRemover {

  private final S3Client s3Client;

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
    s3Client.deleteObject(buildDeleteObjectRequest(bucketName, fileName));
  }

  private DeleteObjectRequest buildDeleteObjectRequest(String bucketName, String fileName) {
    return DeleteObjectRequest
        .builder()
        .bucket(bucketName)
        .key(fileName)
        .build();
  }
}
