package com.silenteight.serp.governance.changerequest.attachment.add;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.common.exception.WrongFilesResourceFormatException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AddAttachmentsUseCaseTest {

  @Mock
  ChangeRequestAttachmentsService changeRequestAttachmentsService;

  @InjectMocks
  AddAttachmentsUseCase underTest;

  private static final String ATTACHMENT_ID_1 = "56f70ff7-184a-4dd5-b391-637b945f154a";
  private static final String ATTACHMENT_ID_2 = "1e869faf-289c-4728-b9af-837c105e3aa7";
  private static final UUID CHANGE_REQUEST = fromString("8ba5f072-0faa-4678-8a16-6e6f62511b80");

  @Test
  void shouldThrowExceptionWhenWrongFilesResourcesFormat() {
    assertThrows(
        WrongFilesResourceFormatException.class,
        () -> underTest.addAttachments(CHANGE_REQUEST, of(ATTACHMENT_ID_1, ATTACHMENT_ID_2)));
  }
}
