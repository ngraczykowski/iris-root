package com.silenteight.sep.filestorage.minio.retrieval;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.BUCKET_NAME;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.FULL_FILE_NAME;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.MOCK_MULTIPART_FILE;
import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.PART_SIZE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = MinioFileStorageConfiguration.class)
class MinioFileRetrieverTest {

  @Autowired
  MinioClient minioClient;

  @Autowired
  FileRetriever underTest;

  @Test
  @SneakyThrows
  void shouldGetSavedFile() {
    createBucket(BUCKET_NAME);
    saveFile();

    //when + then
    FileDto file = underTest.getFile(FULL_FILE_NAME, BUCKET_NAME);
    String fileContent = IOUtils.toString(file.getFileContent(), StandardCharsets.UTF_8);
    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio();
  }

  @SneakyThrows
  private void createBucket(String bucketName) {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
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
  public void cleanMinio() {
    minioClient.removeObject(
        RemoveObjectArgs.builder().object(FULL_FILE_NAME).bucket(BUCKET_NAME).build());
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
  }
}