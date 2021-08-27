package com.silenteight.sep.filestorage.minio.retrieval;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioFileRetriever implements FileRetriever {

  private final MinioClient minioClient;

  @Override
  public FileDto getFile(String fileName, String storageName) {
    return getFileDto(fileName, storageName);
  }

  private FileDto getFileDto(String fileName, String bucketName) {
    try {
      InputStream fileAsInputStream = getFileAsInputStream(fileName, bucketName);
      return convertToFileDto(fileAsInputStream, fileName);
    } catch (Exception e) {
      throw new FileNotFoundException(format("File %s has not been found", fileName));
    }
  }

  private FileDto convertToFileDto(InputStream inputStream, String fileName) throws IOException {
    byte[] bytes = inputStream.readAllBytes();

    return FileDto.builder()
        .fileName(fileName)
        .fileContent(new ByteArrayInputStream(bytes))
        .fileSize(bytes.length)
        .build();
  }

  private InputStream getFileAsInputStream(String fileName, String bucketName) throws Exception {
    return minioClient.getObject(
        GetObjectArgs.builder()
            .bucket(bucketName)
            .object(fileName)
            .build());
  }
}
