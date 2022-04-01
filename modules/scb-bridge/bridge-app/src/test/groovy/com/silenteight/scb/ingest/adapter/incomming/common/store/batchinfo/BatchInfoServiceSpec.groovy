package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo

import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.model.BatchSource

import spock.lang.Specification

import static com.silenteight.scb.ingest.domain.model.BatchStatus.COMPLETED
import static com.silenteight.scb.ingest.domain.model.BatchStatus.ERROR

class BatchInfoServiceSpec extends Specification {

  private repository = Mock(BatchInfoRepository)
  private underTest = new BatchInfoService(repository)

  def 'should store batch info with internalBatchId'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()

    when:
    underTest.store(internalBatchId, BatchSource.GNS_RT, 1)

    then:
    1 * repository.save(_ as BatchInfo) >> {
      def entity = it[0] as BatchInfo

      assert entity.internalBatchId == internalBatchId
      assert entity.batchSource == BatchSource.GNS_RT
      assert entity.alertCount == 1
      assert entity.createdAt != null
    }
  }

  def 'should update status of batch info to error for internalBatchId'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()

    when:
    underTest.changeStatus(internalBatchId, ERROR)

    then:
    1 * repository.update(internalBatchId, ERROR)
  }

  def 'should update status of batch info to completed for internalBatchId'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()

    when:
    underTest.changeStatus(internalBatchId, COMPLETED)

    then:
    1 * repository.update(internalBatchId, COMPLETED)
  }
}
