package com.silenteight.sep.filestorage.minio.retrieval;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;

import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioFileRetriever implements FileRetriever {

  private final MinioClient minioClient;

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
    return minioClient.getObject(
        GetObjectArgs.builder()
            .bucket(bucketName)
            .object(fileName)
            .build());
  }
}
