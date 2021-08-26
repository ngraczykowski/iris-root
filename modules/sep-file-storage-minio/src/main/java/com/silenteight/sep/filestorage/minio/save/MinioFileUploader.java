package com.silenteight.sep.filestorage.minio.save;


import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioFileUploader implements FileUploader {

  private static final int PART_SIZE = 10485760;

  private final MinioClient minioClient;

  @Override
  public void storeFile(StoreFileRequestDto file, String storageName) {
    storeFileInMinio(file, storageName);
  }

  private void storeFileInMinio(StoreFileRequestDto file, String bucketName) {
    try {
      PutObjectArgs fileToSave = prepareObjectBasedOnFileToSave(file, bucketName);
      minioClient.putObject(fileToSave);
    } catch (Exception e) {
      throw new FileNotSavedException(
          format("File %s has not been saved", file.getFileName()), e);
    }
  }

  private PutObjectArgs prepareObjectBasedOnFileToSave(
      StoreFileRequestDto file, String bucketName) {
    return PutObjectArgs.builder()
        .bucket(bucketName)
        .object(file.getFileName())
        .stream(file.getFileContent(), file.getFileSize(), PART_SIZE)
        .build();
  }
}
