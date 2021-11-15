package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.FindRegisteredAlertPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.payments.bridge.svb.learning.reader.service.ReaderServiceFixture.createBatchAlertRequest;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  @Mock
  private RegisterAlertUseCase registerAlertUseCase;
  @Mock
  private DataSourceIngestService dataSourceIngestService;
  @Mock
  private FindRegisteredAlertPort findRegisteredAlertPort;
  @Mock
  private CreateAlertRetentionPort createAlertRetentionPort;
  @Mock
  private IndexLearningAlertPort indexLearningAlertPort;
  @Mock
  private DecisionMapper decisionMapper;

  private IngestService ingestService;

  @BeforeEach
  void setUp() {
    ingestService =
        new IngestService(registerAlertUseCase, dataSourceIngestService, findRegisteredAlertPort,
            createAlertRetentionPort, decisionMapper, indexLearningAlertPort);
  }

  @Test
  void shouldIndexRegisteredAlerts() {
    when(findRegisteredAlertPort.find(any())).thenReturn(List.of(
        new RegisteredAlert(UUID.randomUUID(), "systemId",
            "messageId", "alerts/1", List.of("alerts/1/matches/1"))));
    ingestService.ingest(createBatchAlertRequest());
    verify(indexLearningAlertPort).indexForLearning(anyList());
  }
}
