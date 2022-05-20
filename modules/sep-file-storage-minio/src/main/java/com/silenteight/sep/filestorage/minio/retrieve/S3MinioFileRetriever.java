package com.silenteight.sep.filestorage.minio.retrieve;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor
public class S3MinioFileRetriever implements FileRetriever {

  private final S3Client s3Client;

  @Override
  public FileDto getFile(String storageName, String fileName) {
    return getFileDto(storageName, fileName);
  }

  private FileDto getFileDto(String bucketName, String fileName) {
    try {
      InputStream fileAsInputStream = getFileAsInputStream(bucketName, fileName);
      return convertToFileDto(fileAsInputStream, fileName);
    } catch (Exception e) {
      throw new FileNotFoundException(format("File %s has not been found", fileName));
    }
  }

  private FileDto convertToFileDto(InputStream inputStream, String fileName) {
    return FileDto.builder()
        .name(fileName)
        .content(inputStream)
        .build();
  }

  private InputStream getFileAsInputStream(String bucketName, String fileName) throws Exception {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();
    return s3Client.getObject(getObjectRequest);
  }
}
