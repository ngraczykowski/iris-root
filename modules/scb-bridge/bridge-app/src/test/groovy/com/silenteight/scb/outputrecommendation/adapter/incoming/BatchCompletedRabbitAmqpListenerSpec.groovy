package com.silenteight.scb.outputrecommendation.adapter.incoming

import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted

import spock.lang.Specification
import spock.lang.Subject

class BatchCompletedRabbitAmqpListenerSpec extends Specification {

  def outputRecommendationFacade = Mock(OutputRecommendationFacade)
  def mapper = Mock(BatchMapper)

  @Subject
  def underTest = new BatchCompletedRabbitAmqpListener(mapper, outputRecommendationFacade)

  def "should call output recommendation facade with mapped command"() {
    given:
    def message = MessageBatchCompleted.newBuilder()
        .setBatchId("some-batch-id")
        .setAnalysisName("some-analysis-id")
        .setBatchMetadata("some-metadata")
        .build()

    when:
    underTest.subscribe(message)

    then:
    1 * outputRecommendationFacade.prepareCompletedBatchRecommendations(_)
  }
}
