package com.silenteight.sep.filestorage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.exception.FileNotFoundException;
import com.silenteight.sep.filestorage.exception.FileNotSavedException;

import io.minio.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor
public class MinioFileService {

  @NonNull
  private final MinioClient minioClient;

  private final String bucketName = "test-bucket";

  private static final int PART_SIZE = 10485760;

  public void storeFile(MultipartFile file) {
    try {
      PutObjectArgs fileToSave = prepareObjectBasedOnFileToSave(file);
      minioClient.putObject(fileToSave);
    } catch (Exception e) {
      throw new FileNotSavedException(
          format("File %s has not been saved", file.getOriginalFilename()), e);
    }
  }

  public void deleteFile(MultipartFile file) throws Exception {
    minioClient.removeObject(prepareObjectBasedOnFileToRemove(file.getOriginalFilename()));
  }

  public boolean bucketExist(String bucketName) throws Exception {
    return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
  }

  public void createBucket(String bucketName) throws Exception {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
  }

  public void removeBucket(String bucketName) throws Exception {
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
  }

  public InputStream getFile(String fileName, String bucketName) {

    try {
      return minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .build());

    } catch (Exception e) {
      throw new FileNotFoundException(format("File %s has not been found", fileName), e);
    }
  }

  private PutObjectArgs prepareObjectBasedOnFileToSave(MultipartFile file) throws Exception {
    return PutObjectArgs.builder()
        .bucket(bucketName)
        .object(file.getOriginalFilename())
        .stream(file.getInputStream(), file.getSize(), PART_SIZE)
        .build();
  }

  private RemoveObjectArgs prepareObjectBasedOnFileToRemove(String fileName) {
    return RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object(fileName)
        .build();
  }
}
