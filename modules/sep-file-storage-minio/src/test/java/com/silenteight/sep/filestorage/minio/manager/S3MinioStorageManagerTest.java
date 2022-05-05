package com.silenteight.sep.filestorage.minio.manager;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.S3MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.container.MinioContainer.MinioContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;

import java.util.List;

import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.BUCKET_NAME;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = S3MinioFileStorageConfiguration.class)
class S3MinioStorageManagerTest {

  @Autowired
  S3Client s3Client;

  @Autowired
  StorageManager underTest;

  @Test
  void shouldCreateBucket() {
    //when
    underTest.create(BUCKET_NAME);

    //then
    List<Bucket> listBucketsResponse = s3Client.listBuckets().buckets();
    assertThat(listBucketsResponse).hasSize(1);
    Bucket bucket = listBucketsResponse.get(0);
    assertThat(bucket.name()).isEqualTo(BUCKET_NAME);
    cleanMinio();
  }

  @Test
  @SneakyThrows
  void shouldDeleteBucket() {
    //given
    createBucket(BUCKET_NAME);

    //when
    underTest.remove(BUCKET_NAME);

    //then
    List<Bucket> buckets = s3Client.listBuckets().buckets();
    assertThat(buckets).isEmpty();
  }

  @Test
  void shouldCheckIfStorageDoesNotExists() {
    //given + when
    boolean exist = underTest.exist(BUCKET_NAME);

    //then
    assertThat(exist).isFalse();
  }

  @Test
  void shouldCheckIfStorageExists() {
    //given
    createBucket(BUCKET_NAME);

    //when
    boolean exist = underTest.exist(BUCKET_NAME);

    //then
    assertThat(exist).isTrue();
    cleanMinio();
  }

  @SneakyThrows
  public void cleanMinio() {
    s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  public void createBucket(String bucketName) {
    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
  }
}
