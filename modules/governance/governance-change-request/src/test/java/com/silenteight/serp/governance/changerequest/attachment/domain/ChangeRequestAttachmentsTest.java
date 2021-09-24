package com.silenteight.serp.governance.changerequest.attachment.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.minio.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;
import com.silenteight.serp.governance.changerequest.domain.exception.MaxAttachmentsPerChangeRequestException;
import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.serp.governance.changerequest.attachment.domain.FileReferenceConstants.*;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@ContextConfiguration(
    classes = ChangeRequestTestConfiguration.class,
    initializers = MinioContainerInitializer.class)
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class ChangeRequestAttachmentsTest extends BaseDataJpaTest {

  @Autowired
  ChangeRequestAttachmentsService underTest;

  @Autowired
  ChangeRequestAttachmentsQuery queryUnderTest;

  @Autowired
  ChangeRequestAttachmentRepository repository;

  @Autowired
  FileReferenceService fileReferenceService;

  @Autowired
  FileService fileService;

  @Autowired
  StorageManager storageManager;

  @Autowired
  ChangeRequestService changeRequestService;

  @Test
  void shouldAddAttachmentsIds() {
    //given
    saveChangeRequest(CHANGE_REQUEST_ID.toString());
    saveFileReference(FILE_REFERENCE_ID);
    saveFileReference(FILE_REFERENCE_ID_2);
    saveFileReference(FILE_REFERENCE_ID_3);

    //when
    underTest.addAttachments(
        CHANGE_REQUEST_ID,
        of(FILE_REFERENCE_ID, FILE_REFERENCE_ID_2, FILE_REFERENCE_ID_3));

    //then
    List<String> attachmentsList = repository.getAttachmentsList(CHANGE_REQUEST_ID);
    assertThat(attachmentsList.size()).isEqualTo(3);
    assertThat(attachmentsList)
        .containsExactly(FILE_REFERENCE_ID_3, FILE_REFERENCE_ID_2,
            FILE_REFERENCE_ID);
  }

  @Test
  void shouldDeleteAttachmentId() {
    persistDataForListing();

    underTest.deleteAttachments(CHANGE_REQUEST_ID, FILE_NAME_PREFIX + FILE_REFERENCE_ID);

    List<String> attachmentsList = repository.getAttachmentsList(CHANGE_REQUEST_ID);
    assertThat(attachmentsList).isNotEmpty();
    assertThat(attachmentsList.size()).isEqualTo(2);
    assertThat(attachmentsList).doesNotContain(FILE_REFERENCE_ID);
  }

  @Test
  void shouldReturnListOfAttachments() {
    //given
    persistDataForListing();

    //when
    List<String> attachmentsList = queryUnderTest.list(CHANGE_REQUEST_ID);

    //then
    assertThat(attachmentsList.size()).isEqualTo(3);
    assertThat(attachmentsList.get(0)).isEqualTo(FILE_NAME_PREFIX + FILE_REFERENCE_ID_3);
    assertThat(attachmentsList)
        .contains(
            FILE_NAME_PREFIX + FILE_REFERENCE_ID,
            FILE_NAME_PREFIX + FILE_REFERENCE_ID_2,
            FILE_NAME_PREFIX + FILE_REFERENCE_ID_3);
  }

  @Test
  void shouldThrowExceptionWhenMaxNumberOfAttachmentIsReached() {
    //given
    persistDataForTwentyAttachments();

    //when
    Exception exception = assertThrows(
        MaxAttachmentsPerChangeRequestException.class,
        () -> underTest.addAttachments(CHANGE_REQUEST_ID, of(FILE_REFERENCE_ID)));

    // then
    assertThat(exception.getMessage())
        .isEqualTo(
            "Change request with %s id has already max number of attachments.",
            CHANGE_REQUEST_ID);
  }


  private void saveChangeRequestAttachmentReference(
      String changeRequestId, String fileReferenceId) {

    final ChangeRequestAttachment changeRequestAttachment =
        new ChangeRequestAttachment();

    changeRequestAttachment.setChangeRequestId(
        fromString(changeRequestId));

    changeRequestAttachment.setFileName(fileReferenceId);

    repository.save(changeRequestAttachment);
  }

  private void saveChangeRequest(String changeRequestId) {
    changeRequestService.addChangeRequest(fromString(changeRequestId), "John Doe", "John Doe",
        "no comment");
  }

  private void saveFileReference(String fileReferenceId) {
    fileReferenceService.saveFileReference(fromString(fileReferenceId),
        "John Doe", "test.txt", 1234L, "application/pdf");
  }

  private void persistDataForTwentyAttachments() {
    saveChangeRequest(CHANGE_REQUEST_ID.toString());
    for (int i = 0; i < 20; i++) {
      String newFileId = randomUUID().toString();
      saveFileReference(newFileId);
      saveChangeRequestAttachmentReference(CHANGE_REQUEST_ID.toString(), newFileId);
    }
  }

  private void persistDataForListing() {
    saveChangeRequest(CHANGE_REQUEST_ID.toString());
    saveFileReference(FILE_REFERENCE_ID);
    saveFileReference(FILE_REFERENCE_ID_2);
    saveFileReference(FILE_REFERENCE_ID_3);
    saveChangeRequestAttachmentReference(
        CHANGE_REQUEST_ID.toString(),
        FILE_NAME_PREFIX + FILE_REFERENCE_ID);
    saveChangeRequestAttachmentReference(
        CHANGE_REQUEST_ID.toString(), FILE_NAME_PREFIX + FILE_REFERENCE_ID_2);
    saveChangeRequestAttachmentReference(
        CHANGE_REQUEST_ID.toString(), FILE_NAME_PREFIX + FILE_REFERENCE_ID_3);
  }
}
