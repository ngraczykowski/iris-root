package com.silenteight.sep.filestorage.minio.save;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.S3MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileUploader;
import com.silenteight.sep.filestorage.api.dto.StoreFileRequestDto;
import com.silenteight.sep.filestorage.container.MinioContainer.MinioContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.BUCKET_NAME;
import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.FULL_FILE_NAME;
import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.MOCK_MULTIPART_FILE_TXT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = S3MinioFileStorageConfiguration.class)
class S3MinioFileUploaderTest {

  public static final StoreFileRequestDto FILE_REQUEST_DTO = createStorageFileRequestDto();

  @Autowired
  private S3Client s3Client;

  @Autowired
  FileUploader underTest;

  @Test
  @SneakyThrows
  void shouldStoreFile() {
    createBucket(BUCKET_NAME);

    //when
    underTest.storeFile(FILE_REQUEST_DTO);

    //then
    InputStream fileAsStream = s3Client.getObject(
        GetObjectRequest.builder().key(FULL_FILE_NAME).bucket(BUCKET_NAME).build());

    String fileContent = IOUtils.toString(fileAsStream, Charset.defaultCharset());

    assertThat(fileContent).isEqualTo("Test Content");
    cleanMinio();
  }

  @SneakyThrows
  private static StoreFileRequestDto createStorageFileRequestDto() {
    return StoreFileRequestDto.builder()
        .storageName(BUCKET_NAME)
        .fileName(FULL_FILE_NAME)
        .fileContent(MOCK_MULTIPART_FILE_TXT.getInputStream())
        .fileSize(MOCK_MULTIPART_FILE_TXT.getSize())
        .build();
  }

  @SneakyThrows
  public void cleanMinio() {
    s3Client.deleteObject(
        DeleteObjectRequest.builder().key(FULL_FILE_NAME).bucket(BUCKET_NAME).build());
    s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  private void createBucket(String bucketName) {
    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
  }
}
