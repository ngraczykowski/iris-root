package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class BatchRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private BatchRepository batchRepository;

  def "Should save and get batch from database"() {
    given:
    def batchId = UUID.randomUUID().toString()

    when:
    batchRepository.create(Batch.newOne(batchId, UUID.randomUUID().toString(), 123, "batchMetadata"))

    then:
    def batch = batchRepository.findById(batchId)
    batch.isPresent()
    batch.get().id() == batchId
  }
}
