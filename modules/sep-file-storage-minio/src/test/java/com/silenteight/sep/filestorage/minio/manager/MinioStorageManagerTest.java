package com.silenteight.sep.filestorage.minio.manager;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.BUCKET_NAME;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.FULL_FILE_NAME;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = MinioFileStorageConfiguration.class)
class MinioStorageManagerTest {

  @Autowired
  MinioClient minioClient;

  @Autowired
  StorageManager underTest;

  @Test
  void shouldCreateBucket() {
    //when
    underTest.create(BUCKET_NAME);

    //then + given
    boolean bucketExist = bucketExist(BUCKET_NAME);

    assertThat(bucketExist).isTrue();

    cleanMinio();
  }

  @Test
  @SneakyThrows
  void shouldDeleteBucket() {
    //when
    createBucket(BUCKET_NAME);

    //then
    underTest.remove(BUCKET_NAME);

    //given
    boolean bucketExist =
        minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
    assertThat(bucketExist).isFalse();
  }

  @Test
  void shouldCheckIfStorageExists() {
  }

  @SneakyThrows
  private boolean bucketExist(String bucketName) {
    return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
  }

  @SneakyThrows
  public void cleanMinio() {
    minioClient.removeObject(
        RemoveObjectArgs.builder().object(FULL_FILE_NAME).bucket(BUCKET_NAME).build());
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  public void createBucket(String bucketName) {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
  }
}