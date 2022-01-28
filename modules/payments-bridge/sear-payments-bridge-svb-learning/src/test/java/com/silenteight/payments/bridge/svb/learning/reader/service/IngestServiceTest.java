package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertLabelUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;
import com.silenteight.payments.bridge.svb.migration.DecisionMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.reader.service.ReaderServiceFixture.createBatchAlertRequest;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  @Mock
  private RegisterAlertUseCase registerAlertUseCase;
  @Mock
  private AddAlertLabelUseCase addAlertLabelUseCase;
  @Mock
  private DataSourceIngestService dataSourceIngestService;
  @Mock
  private FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  @Mock
  private CreateAlertRetentionPort createAlertRetentionPort;
  @Mock
  private IndexLearningAlertPort indexLearningAlertPort;
  @Mock
  private DecisionMapper decisionMapper;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  private IngestService ingestService;

  @BeforeEach
  void setUp() {
    ingestService =
        new IngestService(registerAlertUseCase, addAlertLabelUseCase,
            dataSourceIngestService, findRegisteredAlertUseCase,
            createAlertRetentionPort, decisionMapper,
            indexLearningAlertPort, eventPublisher);
  }

  @Test
  void shouldIndexRegisteredAlerts() {
    when(findRegisteredAlertUseCase.find(any())).thenReturn(List.of(
        new RegisteredAlert(
            "systemId", "alerts/1", List.of(
            RegisteredMatch
                .builder()
                .matchName("alerts/1/matches/1")
                .matchId("matchId")
                .build()))));
    ingestService.ingest(createBatchAlertRequest());
    verify(indexLearningAlertPort).indexForLearning(anyList());
  }
}
