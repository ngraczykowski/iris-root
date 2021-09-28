package com.silenteight.serp.governance.changerequest.attachment.delete;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.common.exception.WrongFilesResourceFormatException;
import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DeleteAttachmentsUseCaseTest {

  private static final UUID CHANGE_REQUEST_ID = fromString("f9db1df7-11ea-42b7-ac8d-61447b0aa75b");
  private static final String WRONG_FILE_RESOURCE_NAME = "8ee90a99-5d11-41fb-aca0-a569ba3a04c9";

  @Mock
  FileService fileService;

  @Mock
  ChangeRequestAttachmentsService changeRequestAttachmentsService;

  @Mock
  FileReferenceService fileReferenceService;

  @InjectMocks
  DeleteAttachmentsUseCase underTest;

  @Test
  void shouldThrowExceptionWhenWrongFileResourceFormat() {
    assertThrows(
        WrongFilesResourceFormatException.class,
        () -> underTest.deleteAttachments(CHANGE_REQUEST_ID, WRONG_FILE_RESOURCE_NAME));
  }
}
