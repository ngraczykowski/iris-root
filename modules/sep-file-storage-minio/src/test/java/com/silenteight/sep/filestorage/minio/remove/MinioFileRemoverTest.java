package com.silenteight.sep.filestorage.minio.remove;

import lombok.SneakyThrows;

import com.silenteight.sep.filestorage.MinioFileStorageConfiguration;
import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.domain.test.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.sep.filestorage.minio.retrieval.FileNotFoundException;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.sep.filestorage.testcontants.MinioTestCommons.*;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
})
@SpringBootTest(classes = MinioFileStorageConfiguration.class)
class MinioFileRemoverTest {

  @Autowired
  MinioClient minioClient;

  @Autowired
  FileRemover underTest;

  @Autowired
  FileRetriever minioFileRetrieval;

  @Test
  @SneakyThrows
  void shouldRemoveSavedFile() {
    createBucket(BUCKET_NAME);

    //given
    saveFile();

    //when
    underTest.removeFile(FULL_FILE_NAME, BUCKET_NAME);

    //then
    assertThatThrownBy(() -> minioFileRetrieval.getFile(FULL_FILE_NAME, BUCKET_NAME))
        .isInstanceOf(FileNotFoundException.class);

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
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_NAME).build());
  }
}