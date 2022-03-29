package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo

import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.model.BatchSource

import spock.lang.Specification

class BatchInfoServiceSpec extends Specification {

  private repository = Mock(BatchInfoRepository)
  private underTest = new BatchInfoService(repository)

  def 'should store batch info with internalBatchId'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()

    when:
    underTest.store(internalBatchId, BatchSource.GNS_RT)

    then:
    1 * repository.save(_ as BatchInfo) >> {
      def entity = it[0] as BatchInfo

      assert entity.internalBatchId == internalBatchId
      assert entity.batchSource == BatchSource.GNS_RT
      assert entity.createdAt != null
    }
  }
}
