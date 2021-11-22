package com.silenteight.serp.governance.model.archive;

import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestCommand;
import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestUseCase;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.APPROVED_CHANGE_REQUEST;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.PENDING_CHANGE_REQUEST;
import static com.silenteight.serp.governance.model.archive.ModelsArchivedFixtures.MODELS_ARCHIVED_MESSAGE;
import static com.silenteight.serp.governance.model.archive.ModelsArchivedFixtures.MODEL_NAMES;
import static com.silenteight.serp.governance.model.archive.ModelsArchivedUseCase.CANCELLED_BY;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelsArchivedUseCaseTest {

  @InjectMocks
  private ModelsArchivedUseCase underTest;

  @Mock
  private ListChangeRequestsQuery listChangeRequestsQuery;
  @Mock
  private CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @Test
  void handleModelsArchivedMessage() {
    // given
    when(listChangeRequestsQuery.listByModelNames(MODEL_NAMES))
        .thenReturn(List.of(PENDING_CHANGE_REQUEST, APPROVED_CHANGE_REQUEST));

    // when
    underTest.handle(MODELS_ARCHIVED_MESSAGE);

    // then
    ArgumentCaptor<CancelChangeRequestCommand> argumentCaptor = ArgumentCaptor
        .forClass(CancelChangeRequestCommand.class);

    verify(cancelChangeRequestUseCase).activate(argumentCaptor.capture());

    List<CancelChangeRequestCommand> commands = argumentCaptor.getAllValues();
    assertThat(commands.size()).isEqualTo(1);
    CancelChangeRequestCommand command = commands.get(0);
    assertThat(command.getId()).isEqualTo(PENDING_CHANGE_REQUEST.getId());
    assertThat(command.getCancellerUsername()).isEqualTo(CANCELLED_BY);
  }
}
