package com.silenteight.scb.outputrecommendation.adapter.incoming

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate

class BatchCompletedRabbitAmqpListenerSpec extends Specification {

  def outputRecommendationFacade = Mock(OutputRecommendationFacade)
  def mapper = Mock(BatchMapper)

  @Subject
  def underTest = new BatchCompletedRabbitAmqpListener(mapper, outputRecommendationFacade)

  def "should call output recommendation facade with mapped command"() {
    given:
    def batchId = generate()
    def message = MessageBatchCompleted.newBuilder()
        .setBatchId(batchId)
        .setAnalysisName("some-analysis-id")
        .setBatchMetadata("some-metadata")
        .build()

    when:
    underTest.subscribe(message)

    then:
    1 * outputRecommendationFacade.prepareCompletedBatchRecommendations(_)
  }
}
