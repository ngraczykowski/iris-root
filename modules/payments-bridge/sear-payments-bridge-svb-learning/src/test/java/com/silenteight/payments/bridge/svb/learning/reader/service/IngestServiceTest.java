package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.common.model.RegisteredAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CheckAlertRegisteredPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAnalystSolution;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.reader.service.ReaderServiceFixture.createBatchAlertRequest;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  @Mock
  private RegisterAlertUseCase registerAlertUseCase;
  @Mock
  private DataSourceIngestService dataSourceIngestService;
  @Mock
  private IndexAlertUseCase indexAlertUseCase;
  @Mock
  private CheckAlertRegisteredPort checkAlertRegisteredPort;
  @Mock
  private IndexedAlertBuilderFactory alertBuilderFactory;
  @Mock
  private LearningWarehouseMapper warehouseMapper;
  @Mock
  private CreateAlertRetentionPort createAlertRetentionPort;
  private IngestService ingestService;

  @BeforeEach
  void setUp() {
    ingestService =
        new IngestService(registerAlertUseCase, dataSourceIngestService, indexAlertUseCase,
            checkAlertRegisteredPort, new IndexedAlertBuilderFactory(new ObjectMapper()),
            warehouseMapper, createAlertRetentionPort);
  }

  @Test
  void shouldIndexRegisteredAlerts() {
    when(checkAlertRegisteredPort.findAlertRegistered(any())).thenReturn(List.of(
        new RegisteredAlert("systemId", "messageId", "alerts/1", List.of("alerts/1/matches/1"))));
    when(warehouseMapper.makeAnalystDecision(any())).thenReturn(
        WarehouseAnalystSolution.builder().build());
    ingestService.ingest(createBatchAlertRequest());
    verify(indexAlertUseCase).index(anyList(), any());
  }
}
