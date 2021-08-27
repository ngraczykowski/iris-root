package com.silenteight.sep.filestorage.minio.save;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.BUCKET_NAME;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.FULL_FILE_NAME;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.MOCK_MULTIPART_FILE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = MinioFileStorageConfiguration.class)
class MinioFileUploaderTest {

  public static final StoreFileRequestDto FILE_REQUEST_DTO = createStorageFileRequestDto();

  @Autowired
  private MinioClient minioClient;

  @Autowired
  FileUploader underTest;

  @Test
  @SneakyThrows
  void shouldStoreFile() {
    createBucket(BUCKET_NAME);

    //when
    underTest.storeFile(FILE_REQUEST_DTO, BUCKET_NAME);

    //then
    InputStream fileAsStream = minioClient.getObject(
        GetObjectArgs.builder().bucket(BUCKET_NAME).object(FULL_FILE_NAME).build());
    String fileContent = IOUtils.toString(fileAsStream, Charset.defaultCharset());

    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio();
  }

  @SneakyThrows
  private static StoreFileRequestDto createStorageFileRequestDto() {
    return StoreFileRequestDto.builder()
        .fileName(FULL_FILE_NAME)
        .fileContent(MOCK_MULTIPART_FILE.getInputStream())
        .fileSize(MOCK_MULTIPART_FILE.getSize())
        .build();
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