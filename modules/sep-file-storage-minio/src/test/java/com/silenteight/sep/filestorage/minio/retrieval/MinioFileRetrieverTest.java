package com.silenteight.sep.filestorage.minio.retrieval;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.container.MinioContainer.MinioContainerInitializer;

import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.*;
import static java.nio.charset.StandardCharsets.UTF_8;
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
  void shouldGetSavedTxtFile() {
    createBucket();
    saveFile(MOCK_MULTIPART_FILE_TXT);

    //when + then
    FileDto file = underTest.getFile(BUCKET_NAME, FULL_FILE_NAME);
    String fileContent = IOUtils.toString(file.getContent(), UTF_8);
    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio(file.getName());
  }

  @SneakyThrows
  private void createBucket() {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  private void saveFile(MockMultipartFile file) {
    final PutObjectArgs putObjectArgs = PutObjectArgs.builder()
        .bucket(BUCKET_NAME)
        .object(file.getOriginalFilename())
        .stream(file.getInputStream(), file.getSize(), PART_SIZE)
        .build();

    minioClient.putObject(putObjectArgs);
  }

  @SneakyThrows
  public void cleanMinio(String fileName) {
    minioClient.removeObject(
        RemoveObjectArgs.builder().object(fileName).bucket(BUCKET_NAME).build());
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
  }

  @Test
  @SneakyThrows
  void shouldGetSavedPdfFile() {
    createBucket();
    saveFile(MOCK_MULTIPART_FILE_PDF);

    //when + then
    FileDto file = underTest.getFile(BUCKET_NAME, FULL_FILE_NAME_2);
    String fileContent = IOUtils.toString(file.getContent(), UTF_8);
    assertThat(fileContent).isEqualTo("<<pdf data>>");
    cleanMinio(file.getName());
  }
}
