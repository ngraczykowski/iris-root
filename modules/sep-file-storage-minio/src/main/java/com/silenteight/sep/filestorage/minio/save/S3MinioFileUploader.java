package com.silenteight.sep.filestorage.minio.save;


import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static java.lang.String.format;

@RequiredArgsConstructor
public class S3MinioFileUploader implements FileUploader {

  private final S3Client s3Client;

  @Override
  public void storeFile(StoreFileRequestDto file) {
    storeFileInMinio(file);
  }

  private void storeFileInMinio(StoreFileRequestDto request) {
    try {
      s3Client.putObject(buildPutObjectRequest(request), buildRequestBody(request));
    } catch (Exception e) {
      throw new FileNotSavedException(
          format("File %s has not been saved", request.getFileName()), e);
    }
  }

  private RequestBody buildRequestBody(StoreFileRequestDto request) {
    return RequestBody.fromInputStream(request.getFileContent(), request.getFileSize());
  }

  private PutObjectRequest buildPutObjectRequest(StoreFileRequestDto request) {
    return PutObjectRequest.builder()
        .bucket(request.getStorageName())
        .key(request.getFileName())
        .build();
  }
}
