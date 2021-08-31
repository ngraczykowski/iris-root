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
  public void storeFile(StoreFileRequestDto file) {
    storeFileInMinio(file);
  }

  private void storeFileInMinio(StoreFileRequestDto request) {
    try {
      PutObjectArgs fileToSave = prepareObjectBasedOnFileToSave(request);
      minioClient.putObject(fileToSave);
    } catch (Exception e) {
      throw new FileNotSavedException(
          format("File %s has not been saved", request.getFileName()), e);
    }
  }

  private PutObjectArgs prepareObjectBasedOnFileToSave(
      StoreFileRequestDto request) {

    return PutObjectArgs.builder()
        .bucket(request.getStorageName())
        .object(request.getFileName())
        .stream(request.getFileContent(), request.getFileSize(), PART_SIZE)
        .build();
  }
}
