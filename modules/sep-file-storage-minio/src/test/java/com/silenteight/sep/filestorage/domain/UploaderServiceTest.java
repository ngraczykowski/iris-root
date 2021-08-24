package com.silenteight.sep.filestorage.domain;


import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.sep.filestorage.exception.FileNotFoundException;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = UploaderServiceTestConfiguration.class)
class UploaderServiceTest {

  @Autowired
  private MinioClient minioClient;

  @Autowired
  MinioFileService underTest;

  private static final String BUCKET_NAME = "test-bucket";
  private static final String FILE_NAME = "test-file.txt";
  private static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile("test-file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE,
          "Hello World".getBytes());
  private static final int PART_SIZE = 10485760;

  @Test
  @SneakyThrows
  void shouldCreateBucket() {
    //when
    underTest.createBucket(BUCKET_NAME);

    //then + given
    boolean bucketExist = underTest.bucketExist(BUCKET_NAME);

    assertThat(bucketExist).isTrue();

    cleanMinio();
  }

  @Test
  @SneakyThrows
  void shouldDeleteBucket() {
    //when
    underTest.createBucket(BUCKET_NAME);

    //then
    underTest.removeBucket(BUCKET_NAME);

    //given
    boolean bucketExist = underTest.bucketExist(BUCKET_NAME);
    assertThat(bucketExist).isFalse();
  }

  @Test
  @SneakyThrows
  void shouldStoreFile() {
    createBucket();

    //when
    underTest.storeFile(MOCK_MULTIPART_FILE);

    InputStream file = underTest.getFile(FILE_NAME, BUCKET_NAME);

    String fileContent = IOUtils.toString(file, Charset.defaultCharset());

    assertThat(fileContent).isEqualTo("Hello World");
    cleanMinio();
  }

  @Test
  @SneakyThrows
  void shouldRemoveSavedFile() {
    createBucket();

    //given
    saveFile();

    //when
    underTest.deleteFile(MOCK_MULTIPART_FILE);

    //then
    assertThatThrownBy(() -> underTest.getFile(FILE_NAME, BUCKET_NAME))
        .isInstanceOf(FileNotFoundException.class);
  }

  @SneakyThrows
  private void saveFile() {
    final PutObjectArgs putObjectArgs = PutObjectArgs.builder()
        .bucket(BUCKET_NAME)
        .object(MOCK_MULTIPART_FILE.getOriginalFilename())
        .stream(MOCK_MULTIPART_FILE.getInputStream(), MOCK_MULTIPART_FILE.getSize(), PART_SIZE)
        .build();

    minioClient.putObject(putObjectArgs);
  }

  @SneakyThrows
  private void cleanMinio() {
    minioClient.removeObject(
        RemoveObjectArgs.builder().object(FILE_NAME).bucket(BUCKET_NAME).build());
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  private void createBucket() {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
  }
}