package com.silenteight.scb.outputrecommendation.adapter.incoming

import com.silenteight.proto.registration.api.v1.MessageBatchError
import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate

class BatchErrorRabbitAmqpListenerSpec extends Specification {

  def facade = Mock(OutputRecommendationFacade)
  def mapper = Mock(BatchMapper)

  @Subject
  def underTest = new BatchErrorRabbitAmqpListener(mapper, facade)

  def "should call OutputRecommendationFacade with Batch Error message"() {
    def batchId = generate()
    given:
    def message = MessageBatchError.newBuilder()
        .setBatchId(batchId)
        .setBatchMetadata("Dummy metadata")
        .setErrorDescription("Dummy error description")
        .build()

    when:
    underTest.subscribe(message)

    then:
    1 * facade.prepareErrorBatchRecommendations(_)
  }
}
