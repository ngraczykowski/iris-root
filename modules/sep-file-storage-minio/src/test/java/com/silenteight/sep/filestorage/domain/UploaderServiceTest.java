package com.silenteight.sep.filestorage.domain;


import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.sep.filestorage.minio.MinioFileService;
import com.silenteight.sep.filestorage.minio.retrieval.FileNotFoundException;

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

  private static final String BUCKET_NAME = "test-bucket-name";
  private static final String FILE_NAME = "test_file";
  private static final int PART_SIZE = 10485760;

  private static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile(FILE_NAME, FILE_NAME, MediaType.TEXT_PLAIN_VALUE,
          "Test Content".getBytes());

  private static final StoreFileRequestDto FILE_REQUEST_DTO = createStorageFileRequestDto();

  @Autowired
  private MinioClient minioClient;

  @Autowired
  MinioFileService underTest;

  @Test
  void shouldCreateBucket() {
    //when
    underTest.createBucket(BUCKET_NAME);

    //then + given
    boolean bucketExist = bucketExist(BUCKET_NAME);

    assertThat(bucketExist).isTrue();

    cleanMinio();
  }

  @Test
  void shouldDeleteBucket() {
    //when
    createBucket(BUCKET_NAME);

    //then
    underTest.removeBucket(BUCKET_NAME);

    //given
    boolean bucketExist = underTest.bucketExist(BUCKET_NAME);
    assertThat(bucketExist).isFalse();
  }

  @Test
  @SneakyThrows
  void shouldStoreFile() {
    createBucket(BUCKET_NAME);

    //when
    underTest.storeFile(FILE_REQUEST_DTO, BUCKET_NAME);

    //then
    InputStream fileAsStream = minioClient.getObject(
        GetObjectArgs.builder().bucket(BUCKET_NAME).object(FILE_NAME).build());
    String fileContent = IOUtils.toString(fileAsStream, Charset.defaultCharset());

    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio();
  }

  @Test
  @SneakyThrows
  void shouldRemoveSavedFile() {
    createBucket(BUCKET_NAME);

    //given
    saveFile();

    //when
    underTest.removeFile(FILE_NAME, BUCKET_NAME);

    //then
    assertThatThrownBy(() -> underTest.retrieveFile(FILE_NAME, BUCKET_NAME))
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
  private void createBucket(String bucketName) {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
  }

  @SneakyThrows
  private boolean bucketExist(String bucketName) {
    return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
  }

  @SneakyThrows
  private static StoreFileRequestDto createStorageFileRequestDto() {
    return StoreFileRequestDto.builder()
        .fileName(FILE_NAME)
        .fileContent(MOCK_MULTIPART_FILE.getInputStream())
        .fileSize(MOCK_MULTIPART_FILE.getSize())
        .build();
  }
}