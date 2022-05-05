package com.silenteight.sep.filestorage.minio.remove;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.S3MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.sep.filestorage.minio.retrieve.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.BUCKET_NAME;
import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.FULL_FILE_NAME;
import static com.silenteight.sep.filestorage.testcommons.MinioTestCommons.MOCK_MULTIPART_FILE_TXT;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = S3MinioFileStorageConfiguration.class)
class S3MinioFileRemoverTest {

  @Autowired
  S3Client s3Client;

  @Autowired
  FileRemover underTest;

  @Autowired
  FileRetriever minioFileRetrieval;

  @Test
  @SneakyThrows
  void shouldRemoveSavedFile() {
    //given
    createBucket(BUCKET_NAME);
    saveFile();

    //when
    underTest.removeFile(BUCKET_NAME, FULL_FILE_NAME);

    //then
    assertThatThrownBy(() -> minioFileRetrieval.getFile(FULL_FILE_NAME, BUCKET_NAME))
        .isInstanceOf(FileNotFoundException.class);

    cleanMinio();
  }

  @SneakyThrows
  private void createBucket(String bucketName) {
    s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
  }

  @SneakyThrows
  private void saveFile() {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key(MOCK_MULTIPART_FILE_TXT.getOriginalFilename())
        .build();

    RequestBody requestBody = RequestBody.fromInputStream(MOCK_MULTIPART_FILE_TXT.getInputStream(),
        MOCK_MULTIPART_FILE_TXT.getSize());

    s3Client.putObject(putObjectRequest, requestBody);
  }

  @SneakyThrows
  public void cleanMinio() {
    s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
  }
}
