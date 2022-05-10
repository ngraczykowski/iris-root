package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessingRepositoryIT.TestConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.common.SyncTestInitializer;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State.ERROR;
import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State.IN_PROGRESS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(
    classes = TestConfiguration.class,
    initializers = SyncTestInitializer.class)
@Disabled
class AlertUnderProcessingRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private AlertUnderProcessingRepository alertUnderProcessingRepository;

  @Test
  void shouldStoreEntities() {
    // given
    AlertUnderProcessing entity1 = createEntity("system_id_1", "batch_id_1");
    AlertUnderProcessing entity2 = createEntity("system_id_2", "batch_id_2");

    // when
    persistEntities(entity1, entity2);

    // then
    assertStoredSuccessfully(entity1, entity2);
  }

  private void assertStoredSuccessfully(AlertUnderProcessing... expectedEntities) {
    Collection<AlertUnderProcessing> entities = alertUnderProcessingRepository.findAll();
    Assertions.assertThat(entities).hasSize(expectedEntities.length);
    for (AlertUnderProcessing expected : expectedEntities) {
      Assertions.assertThat(entities).anySatisfy(e -> assertEquals(e, expected));
    }
  }

  private static void assertEquals(AlertUnderProcessing entity, AlertUnderProcessing expected) {
    assertThat(entity.getSystemId()).isEqualTo(expected.getSystemId());
    assertThat(entity.getBatchId()).isEqualTo(expected.getBatchId());
    assertThat(entity.getState()).isEqualTo(IN_PROGRESS);
  }

  private void persistEntities(AlertUnderProcessing... entities) {
    alertUnderProcessingRepository.saveAll(asList(entities));

    entityManager.flush();
    entityManager.clear();
  }

  private static AlertUnderProcessing createEntity(String systemId, String batchId) {
    return new AlertUnderProcessing(systemId, batchId, ScbAlertIdContext.newBuilder().build());
  }

  @Test
  void shouldDeleteExpiredEntities() {
    // given
    AlertUnderProcessing entity1 = createEntity("system_id_1", "batch_id_1");
    persistEntities(entity1);

    //when
    alertUnderProcessingRepository.deleteByCreatedAtBeforeAndStateIn(
        OffsetDateTime.now().minusDays(1),
        allStates());

    //then
    assertThat(alertUnderProcessingRepository.findAll()).hasSize(1);

    //when
    alertUnderProcessingRepository.deleteByCreatedAtBeforeAndStateIn(OffsetDateTime.now(),
        allStates());

    //then
    assertThat(alertUnderProcessingRepository.findAll()).isEmpty();
  }

  @Test
  void shouldFetchOnlyExistingEntities() {
    // given
    AlertUnderProcessing entity1 = createEntity("system_id_1", "batch_id_1");
    AlertUnderProcessing entity2 = createEntity("system_id_2", "batch_id_2");

    persistEntities(entity1);

    // when
    List<AlertId> allBySystemIdIn =
        (List<AlertId>) alertUnderProcessingRepository.findAllBySystemIdIn(
            asList(entity1.getSystemId(), entity2.getSystemId()));

    // then
    Assertions.assertThat(allBySystemIdIn)
        .hasSize(1)
        .first()
        .satisfies(a -> assertThat(a.getSystemId()).isEqualTo(entity1.getSystemId()))
        .satisfies(a -> assertThat(a.getBatchId()).isEqualTo(entity1.getBatchId()));
  }

  @Test
  void shouldDeleteAlertBySystemIdAndBatchId() {
    //given
    AlertUnderProcessing entity1 = createEntity("systemId_1", "batchId_1");
    AlertUnderProcessing entity2 = createEntity("systemId_2", "batchId_1");
    persistEntities(entity1, entity2);

    //when
    alertUnderProcessingRepository.deleteBySystemIdAndBatchId("systemId_1", "batchId_1");

    //then
    assertThat(alertUnderProcessingRepository.findAll()).hasSize(1);

    //when
    alertUnderProcessingRepository.deleteBySystemIdAndBatchId("systemId_2", "batchId_1");

    //then
    assertThat(alertUnderProcessingRepository.findAll()).isEmpty();
  }

  @Test
  void shouldUpdateState() {
    //given
    AlertUnderProcessing entity = createEntity("systemId_1", "batchId_1");
    persistEntities(entity);

    //when
    alertUnderProcessingRepository.update(entity.getSystemId(), entity.getBatchId(), ERROR);

    //then
    Collection<AlertUnderProcessing> results = alertUnderProcessingRepository.findAll();
    Assertions.assertThat(results).hasSize(1);
    for (AlertUnderProcessing entry : results) {
      assertThat(entry.getState()).isEqualTo(ERROR);
    }
  }

  @Test
  void shouldUpdateStateAndError() {
    //given
    AlertUnderProcessing entity = createEntity("systemId_1", "batchId_1");
    persistEntities(entity);

    //when
    alertUnderProcessingRepository.update(entity.getSystemId(), entity.getBatchId(), ERROR, "text");

    //then
    Collection<AlertUnderProcessing> results = alertUnderProcessingRepository.findAll();
    Assertions.assertThat(results).hasSize(1);
    for (AlertUnderProcessing entry : results) {
      assertThat(entry.getState()).isEqualTo(ERROR);
      assertThat(entry.getError()).isEqualTo("text");
    }
  }

  private static EnumSet<State> allStates() {
    return EnumSet.allOf(State.class);
  }

  @EntityScan(basePackages = "com.silenteight.scb")
  @EnableJpaRepositories(basePackages = "com.silenteight.scb")
  @Configuration
  static class TestConfiguration {

  }
}
