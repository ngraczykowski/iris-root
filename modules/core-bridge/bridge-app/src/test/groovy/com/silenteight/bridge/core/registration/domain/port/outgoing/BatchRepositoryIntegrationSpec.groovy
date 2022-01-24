package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class BatchRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private BatchRepository batchRepository

  def "should save and get batch from database"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = UUID.randomUUID().toString()
    def policyName = UUID.randomUUID().toString()
    def batch = Batch.builder()
        .id(batchId)
        .analysisName(analysisName)
        .policyName(policyName)
        .alertsCount(123)
        .batchMetadata("batchMetadata")
        .status(BatchStatus.STORED)
        .build()

    when:
    batchRepository.create(batch)

    then:
    def result = batchRepository.findById(batchId)
    with(result) {
      isPresent()
      with(result.get()) {
        it == batch
      }
    }
  }
}
