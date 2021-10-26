package com.silenteight.warehouse.backup.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_DATA_INDEX_REQUEST_1;
import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_DATA_JSON_FORMAT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = BackupTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class,
    OpendistroElasticContainerInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class BackupIT {

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private BackupMessageRepository backupMessageRepository;

  @SneakyThrows
  @Test
  void shouldSaveBackupMessageWhenProductionDataIndexRequested() {
    //given
    ProductionDataIndexRequest productionRequest = PRODUCTION_DATA_INDEX_REQUEST_1;
    //when
    productionIndexClientGateway.indexRequest(PRODUCTION_DATA_INDEX_REQUEST_1);
    await()
        .atMost(5, SECONDS)
        .until(() ->
            backupMessageRepository.findByRequestId(productionRequest.getRequestId()) != null);
    //then
    BackupMessage backupMessage = backupMessageRepository
        .findByRequestId(productionRequest.getRequestId());
    assertThat(backupMessage).isNotNull();
    assertThat(backupMessage.getRequestId()).isEqualTo(productionRequest.getRequestId());
    assertThat(backupMessage.getAnalysisName()).isEqualTo(productionRequest.getAnalysisName());
    assertThat(backupMessage.getDiagnostic()).isEqualTo(PRODUCTION_DATA_JSON_FORMAT);

    ProductionDataIndexRequest savedProductionRequest =
        toProductionDataIndexRequest(backupMessage.getData());
    assertThat(savedProductionRequest).isEqualTo(productionRequest);
  }

  @SneakyThrows
  private ProductionDataIndexRequest toProductionDataIndexRequest(byte[] data) {
    return ProductionDataIndexRequest.parseFrom(data);
  }
}
