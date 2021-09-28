package com.silenteight.serp.governance.changerequest.attachment.download;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.minio.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.serp.governance.file.common.exception.WrongFilesResourceFormatException;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;
import com.silenteight.serp.governance.file.storage.FileWrapper;
import com.silenteight.serp.governance.file.upload.UploadFileUseCase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Transactional
@ContextConfiguration(classes = {
    DownloadAttachmentsTestConfiguration.class, },
    initializers = {
        MinioContainerInitializer.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class DownloadAttachmentsUseCaseTest extends BaseDataJpaTest {

  private static final String NEW_FILE_NAME = "98323c8c-5253-4a54-bc84-b42c4709e151";
  private static final String FILE_NAME_PREFIX = "files/";
  private static final String FILE_NAME = "test.doc";

  @Autowired
  UploadFileUseCase uploadFileUseCase;

  @Autowired
  StorageManager storageManager;

  @Autowired
  DownloadAttachmentsUseCase underTest;

  @Test
  @SneakyThrows
  void shouldDownloadAttachment() {
    //given
    storageManager.create("attachments");

    byte[] testFileAsBytes = getTestFileAsBytes("src/test/resources/test-files/test.doc");
    MockMultipartFile fileToSave =
        new MockMultipartFile("test-file", FILE_NAME, TEXT_PLAIN_VALUE, testFileAsBytes);

    //when
    FileReferenceDto savedFile = uploadFileUseCase.activate(fileToSave, NEW_FILE_NAME);

    //then
    FileWrapper fileWrapper =
        underTest.activate(FILE_NAME_PREFIX + savedFile.getFileId().toString());
    assertThat(fileWrapper.getFileName()).isEqualTo(FILE_NAME);
    assertThat(fileWrapper.getMimeType()).isEqualTo("application/x-tika-msoffice");
    assertThat(fileWrapper.getContent()).isEqualTo(testFileAsBytes);
  }

  @Test
  void shouldThrowExceptionWhenWrongFileResourceNameProvided() {
    assertThrows(
        WrongFilesResourceFormatException.class, () -> underTest.activate(NEW_FILE_NAME));
  }

  @SneakyThrows
  static byte[] getTestFileAsBytes(String pathForFile) {
    Path filePath = get(pathForFile);
    return readAllBytes(filePath);
  }
}
