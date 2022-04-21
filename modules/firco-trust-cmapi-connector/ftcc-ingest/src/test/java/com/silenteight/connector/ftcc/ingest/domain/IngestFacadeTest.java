package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.AlertMessage;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.state.AlertStateEvaluator;
import com.silenteight.connector.ftcc.request.store.RequestStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.connector.ftcc.ingest.domain.IngestFacade.LEARNING_PRIORITY;
import static com.silenteight.connector.ftcc.ingest.domain.IngestFacade.SOLVING_PRIORITY;
import static com.silenteight.connector.ftcc.ingest.domain.IngestFixtures.*;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.NEW;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.SOLVED_FALSE_POSITIVE;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.SOLVED_TRUE_POSITIVE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestFacadeTest {

  private static final String BATCH_NAME_PREFIX = "batches/";
  private static final String MESSAGE_NAME_PREFIX = "messages/";

  @InjectMocks
  private IngestFacade underTest;

  @Mock
  private RequestStorage requestStorage;
  @Mock
  private RegistrationApiClient registrationApiClient;
  @Mock
  private AlertStateEvaluator alertStateEvaluator;
  @Mock
  private DataPrepMessageGateway dataPrepMessageGateway;

  @Test
  void shouldIngest() {
    // given
    RequestDto requestDto = makeRequestDto();
    when(requestStorage.store(requestDto, BATCH_ID)).thenReturn(REQUEST_STORE);
    when(alertStateEvaluator.evaluate(BATCH_ID, MESSAGE_ID_1)).thenReturn(SOLVED_FALSE_POSITIVE);
    when(alertStateEvaluator.evaluate(BATCH_ID, MESSAGE_ID_2)).thenReturn(SOLVED_TRUE_POSITIVE);
    when(alertStateEvaluator.evaluate(BATCH_ID, MESSAGE_ID_3)).thenReturn(NEW);

    // when
    underTest.ingest(requestDto, BATCH_ID);

    // then
    Batch batch = Batch.builder()
        .batchId(BATCH_NAME_PREFIX + BATCH_ID)
        .alertsCount(requestDto.getMessagesCount())
        .build();
    verify(registrationApiClient).registerBatch(batch);
    var alertMessageCaptor = forClass(AlertMessage.class);
    verify(dataPrepMessageGateway, times(3)).send(alertMessageCaptor.capture());
    List<AlertMessage> alertMessages = alertMessageCaptor.getAllValues();
    assertThat(alertMessages).hasSize(3);
    AlertMessage alertMessage1 = alertMessages.get(0);
    assertThat(alertMessage1.getBatchName()).isEqualTo(BATCH_NAME_PREFIX + BATCH_ID);
    assertThat(alertMessage1.getMessageName()).isEqualTo(MESSAGE_NAME_PREFIX + MESSAGE_ID_1);
    assertThat(alertMessage1.getState()).isEqualTo(SOLVED_FALSE_POSITIVE);
    assertThat(alertMessage1.getPriority()).isEqualTo(LEARNING_PRIORITY);
    AlertMessage alertMessage2 = alertMessages.get(1);
    assertThat(alertMessage2.getBatchName()).isEqualTo(BATCH_NAME_PREFIX + BATCH_ID);
    assertThat(alertMessage2.getMessageName()).isEqualTo(MESSAGE_NAME_PREFIX + MESSAGE_ID_2);
    assertThat(alertMessage2.getState()).isEqualTo(SOLVED_TRUE_POSITIVE);
    assertThat(alertMessage2.getPriority()).isEqualTo(LEARNING_PRIORITY);
    AlertMessage alertMessage3 = alertMessages.get(2);
    assertThat(alertMessage3.getBatchName()).isEqualTo(BATCH_NAME_PREFIX + BATCH_ID);
    assertThat(alertMessage3.getMessageName()).isEqualTo(MESSAGE_NAME_PREFIX + MESSAGE_ID_3);
    assertThat(alertMessage3.getState()).isEqualTo(NEW);
    assertThat(alertMessage3.getPriority()).isEqualTo(SOLVING_PRIORITY);
  }
}
