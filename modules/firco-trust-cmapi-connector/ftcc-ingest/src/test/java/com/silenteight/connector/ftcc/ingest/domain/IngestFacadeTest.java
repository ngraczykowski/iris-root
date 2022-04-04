package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.state.AlertStateEvaluator;
import com.silenteight.connector.ftcc.request.store.RequestStorage;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.connector.ftcc.ingest.domain.IngestFixtures.*;
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
  private BatchIdGenerator batchIdGenerator;
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
    when(batchIdGenerator.generate()).thenReturn(BATCH_ID);
    when(requestStorage.store(requestDto, BATCH_ID)).thenReturn(REQUEST_STORE);
    when(alertStateEvaluator.evaluate(BATCH_ID, MESSAGE_ID_1)).thenReturn(SOLVED_FALSE_POSITIVE);
    when(alertStateEvaluator.evaluate(BATCH_ID, MESSAGE_ID_2)).thenReturn(SOLVED_TRUE_POSITIVE);

    // when
    underTest.ingest(requestDto);

    // then
    Batch batch = Batch.builder()
        .batchId(BATCH_NAME_PREFIX + BATCH_ID)
        .alertsCount(requestDto.getMessagesCount())
        .build();
    verify(registrationApiClient).registerBatch(batch);
    var alertMessageStoredCaptor = forClass(AlertMessageStored.class);
    verify(dataPrepMessageGateway, times(2)).send(alertMessageStoredCaptor.capture());
    List<AlertMessageStored> allAlertMessageStored = alertMessageStoredCaptor.getAllValues();
    assertThat(allAlertMessageStored.size()).isEqualTo(2);
    AlertMessageStored alertMessageStored1 = allAlertMessageStored.get(0);
    assertThat(alertMessageStored1.getBatchName()).isEqualTo(BATCH_NAME_PREFIX + BATCH_ID);
    assertThat(alertMessageStored1.getMessageName()).isEqualTo(MESSAGE_NAME_PREFIX + MESSAGE_ID_1);
    assertThat(alertMessageStored1.getState()).isEqualTo(SOLVED_FALSE_POSITIVE);
    AlertMessageStored alertMessageStored2 = allAlertMessageStored.get(1);
    assertThat(alertMessageStored2.getBatchName()).isEqualTo(BATCH_NAME_PREFIX + BATCH_ID);
    assertThat(alertMessageStored2.getMessageName()).isEqualTo(MESSAGE_NAME_PREFIX + MESSAGE_ID_2);
    assertThat(alertMessageStored2.getState()).isEqualTo(SOLVED_TRUE_POSITIVE);
  }
}
