package com.silenteight.scb.ingest.adapter.incomming.common.store

import com.silenteight.scb.BaseDataJpaSpec
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.common.SyncTestInitializer
import com.silenteight.scb.ingest.adapter.incomming.common.store.RawAlert.AlertType

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = TestConfiguration,
    initializers = SyncTestInitializer)
@Slf4j
class RawAlertRepositorySpecIT extends BaseDataJpaSpec {

  @Autowired
  private RawAlertRepository rawAlertRepository;

  def 'should persist entities'() {
    given:
    def entity1 = createEntity("system_id_1", "batch_id_1")
    def entity2 = createEntity("system_id_2", "batch_id_2")

    when:
    persistEntities(entity1, entity2)

    then:
    assertStoredSuccessfully(entity1, entity2)
  }

  def 'should not allow to persist entities with same pair of (systemId, batchId)'() {
    given:
    def entity1 = createEntity("system_id_1", "batch_id_1")
    def entity2 = createEntity("system_id_1", "batch_id_1")

    when:
    persistEntities(entity1, entity2)

    then:
    def e = thrown(DataIntegrityViolationException)
    e.message.contains("ConstraintViolationException")
  }

  private void assertStoredSuccessfully(RawAlert[] expectedEntities) {
    assert rawAlertRepository.findAll().size() == expectedEntities.size()

    expectedEntities.each {
      assertEquals(rawAlertRepository.findById(it.getId()).orElseThrow(), it)
    }
  }

  private static void assertEquals(RawAlert entity, RawAlert expected) {
    assert entity.getCreatedAt() != null
    assert entity.getId() == expected.getId()
    assert entity.getSystemId() == expected.getSystemId()
    assert entity.getBatchId() == expected.getBatchId()
    assert entity.getInternalBatchId() == expected.getInternalBatchId()
    assert entity.getAlertType() == expected.getAlertType()
    assert entity.getPayload() == expected.getPayload()
  }

  private void persistEntities(RawAlert[] entities) {
    rawAlertRepository.saveAll(entities as List)

    entityManager.clear() // so entities are not cached
  }

  private static RawAlert createEntity(String systemId, String batchId) {
    return new RawAlert(
        AlertId.builder()
            .batchId(batchId)
            .systemId(systemId)
            .build(),
        UUID.randomUUID().toString(),
        AlertType.LEARNING,
        "payload".getBytes())
  }

  @EntityScan(basePackages = "com.silenteight.scb")
  @EnableJpaRepositories(basePackages = "com.silenteight.scb")
  @Configuration
  static class TestConfiguration {

  }

}
