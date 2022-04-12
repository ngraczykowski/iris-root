package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo

import com.silenteight.scb.BaseDataJpaSpec
import com.silenteight.scb.ingest.adapter.incomming.common.SyncTestInitializer
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration

import static com.silenteight.scb.ingest.domain.model.BatchSource.GNS_RT
import static com.silenteight.scb.ingest.domain.model.BatchStatus.COMPLETED
import static com.silenteight.scb.ingest.domain.model.BatchStatus.QUEUED

@ContextConfiguration(
    classes = TestConfiguration,
    initializers = SyncTestInitializer)
@Slf4j
class BatchInfoRepositorySpecIT extends BaseDataJpaSpec {

  @Autowired
  private BatchInfoRepository batchInfoRepository;

  def 'should persist entity'() {
    given:
    def batchInfo = createEntity()

    when:
    persistEntity(batchInfo)

    then:
    assertStoredSuccessfully(batchInfo)
  }

  def 'should update entity'() {
    given:
    def batchInfo = createEntity()
    persistEntity(batchInfo)

    when:
    batchInfoRepository.update(batchInfo.getInternalBatchId(), COMPLETED)

    then:
    assertStoredSuccessfully(createCompletedBatchInfo(batchInfo))
  }

  private void assertStoredSuccessfully(BatchInfo expectedBatchInfo) {
    assert batchInfoRepository.findAll().size() == 1
    assertEquals(
        batchInfoRepository.findById(expectedBatchInfo.getId()).orElseThrow(), expectedBatchInfo)
  }

  private static void assertEquals(BatchInfo savedBatchInfo, BatchInfo expectedBatchInfo) {
    assert savedBatchInfo.getCreatedAt() != null
    assert savedBatchInfo.getId() == expectedBatchInfo.getId()
    assert savedBatchInfo.getInternalBatchId() == expectedBatchInfo.getInternalBatchId()
    assert savedBatchInfo.getBatchSource() == expectedBatchInfo.getBatchSource()
    assert savedBatchInfo.getBatchStatus() == expectedBatchInfo.getBatchStatus()
    assert savedBatchInfo.getModifiedAt() != null
  }

  private void persistEntity(BatchInfo batchInfoEntity) {
    batchInfoRepository.save(batchInfoEntity)

    entityManager.clear() // so entities are not cached
  }

  private static BatchInfo createEntity() {
    return BatchInfo.builder()
        .internalBatchId(InternalBatchIdGenerator.generate())
        .batchSource(GNS_RT)
        .batchStatus(QUEUED)
        .alertCount(2)
        .build();
  }

  private static BatchInfo createCompletedBatchInfo(BatchInfo persistedBatchInfo) {
    return persistedBatchInfo.toBuilder()
        .batchStatus(COMPLETED)
        .build();
  }

  @EntityScan(basePackages = "com.silenteight.scb")
  @EnableJpaRepositories(basePackages = "com.silenteight.scb")
  @Configuration
  static class TestConfiguration {
  }
}
