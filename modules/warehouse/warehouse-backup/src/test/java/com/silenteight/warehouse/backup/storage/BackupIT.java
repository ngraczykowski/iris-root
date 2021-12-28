package com.silenteight.warehouse.backup.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_DATA_JSON_FORMAT;
import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_REQUEST_V1;
import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_REQUEST_V2;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = BackupTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@CleanDatabase
class BackupIT {

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private BackupMessageRepository backupMessageRepository;

  @SneakyThrows
  @Test
  void shouldSaveBackupMessageWhenProductionDataIndexV1Requested() {
    //when
    productionIndexClientGateway.indexRequest(PRODUCTION_REQUEST_V1);
    await()
        .atMost(5, SECONDS)
        .until(() ->
            backupMessageRepository.findByRequestId(PRODUCTION_REQUEST_V1.getRequestId()) != null);
    //then
    BackupMessage backupMessage = backupMessageRepository
        .findByRequestId(PRODUCTION_REQUEST_V1.getRequestId());
    assertThat(backupMessage).isNotNull();
    assertThat(backupMessage.getRequestId()).isEqualTo(PRODUCTION_REQUEST_V1.getRequestId());
    assertThat(backupMessage.getAnalysisName()).isEqualTo(PRODUCTION_REQUEST_V1.getAnalysisName());
    assertThat(backupMessage.getDiagnostic()).isEqualTo(PRODUCTION_DATA_JSON_FORMAT);

    var savedProductionRequest =
        toProductionDataIndexRequestV1(backupMessage.getData());
    assertThat(savedProductionRequest).isEqualTo(PRODUCTION_REQUEST_V1);
  }

  @SneakyThrows
  @Test
  void shouldSaveBackupMessageWhenProductionDataIndexV2Requested() {
    //when
    productionIndexClientGateway.indexRequest(PRODUCTION_REQUEST_V2);
    await()
        .atMost(5, SECONDS)
        .until(() ->
            backupMessageRepository.findByRequestId(PRODUCTION_REQUEST_V2.getRequestId()) != null);
    //then
    BackupMessage backupMessage = backupMessageRepository
        .findByRequestId(PRODUCTION_REQUEST_V2.getRequestId());
    assertThat(backupMessage).isNotNull();
    assertThat(backupMessage.getRequestId()).isEqualTo(PRODUCTION_REQUEST_V2.getRequestId());
    assertThat(backupMessage.getAnalysisName()).isEqualTo(PRODUCTION_REQUEST_V2.getAnalysisName());
    assertThat(backupMessage.getDiagnostic()).isEqualTo(PRODUCTION_DATA_JSON_FORMAT);

    var savedProductionRequest =
        toProductionDataIndexRequestV2(backupMessage.getData());
    assertThat(savedProductionRequest).isEqualTo(PRODUCTION_REQUEST_V2);
  }

  @SneakyThrows
  private com.silenteight.data.api.v1.ProductionDataIndexRequest toProductionDataIndexRequestV1(
      byte[] data) {

    return com.silenteight.data.api.v1.ProductionDataIndexRequest.parseFrom(data);
  }

  @SneakyThrows
  private com.silenteight.data.api.v2.ProductionDataIndexRequest toProductionDataIndexRequestV2(
      byte[] data) {

    return com.silenteight.data.api.v2.ProductionDataIndexRequest.parseFrom(data);
  }
}
