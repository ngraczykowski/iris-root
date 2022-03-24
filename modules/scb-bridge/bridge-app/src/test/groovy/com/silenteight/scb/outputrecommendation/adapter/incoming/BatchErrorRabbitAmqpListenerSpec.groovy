package com.silenteight.scb.outputrecommendation.adapter.incoming

import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade
import com.silenteight.proto.registration.api.v1.MessageBatchError

import spock.lang.Specification
import spock.lang.Subject

class BatchErrorRabbitAmqpListenerSpec extends Specification {

  def facade = Mock(OutputRecommendationFacade)
  def mapper = Mock(BatchMapper)

  @Subject
  def underTest = new BatchErrorRabbitAmqpListener(mapper, facade)

  def "should call OutputRecommendationFacade with Batch Error message"() {
    given:
    def message = MessageBatchError.newBuilder()
        .setBatchId(UUID.randomUUID().toString())
        .setBatchMetadata("Dummy metadata")
        .setErrorDescription("Dummy error description")
        .build()

    when:
    underTest.subscribe(message)

    then:
    1 * facade.prepareErrorBatchRecommendations(_)
  }
}
