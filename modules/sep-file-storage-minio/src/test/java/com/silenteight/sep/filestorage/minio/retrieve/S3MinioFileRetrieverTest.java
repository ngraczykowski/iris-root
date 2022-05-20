package com.silenteight.sep.filestorage.minio.retrieve;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.S3MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.container.MinioContainer.MinioContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = S3MinioFileStorageConfiguration.class)
class S3MinioFileRetrieverTest {

  @Autowired
  S3Client s3Client;

  @Autowired
  FileRetriever underTest;

  @Test
  @SneakyThrows
  void shouldGetSavedTxtFile() {
    //given
    createBucket();
    saveFile(MOCK_MULTIPART_FILE_TXT);

    //when + then
    FileDto file = underTest.getFile(BUCKET_NAME, FULL_FILE_NAME);
    String fileContent = IOUtils.toString(file.getContent(), UTF_8);
    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio(file.getName());
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

  @SneakyThrows
  private void createBucket() {
    s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  private void saveFile(MockMultipartFile file) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key(file.getOriginalFilename())
        .build();

    RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());

    s3Client.putObject(putObjectRequest, requestBody);
  }

  @SneakyThrows
  public void cleanMinio(String fileName) {
    s3Client.deleteObject(
        DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(fileName).build());

    s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
  }
}
