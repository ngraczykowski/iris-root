/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.incoming

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.OutputRecommendationFacade
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.PrepareRecommendationResponseCommand

import spock.lang.Specification
import spock.lang.Subject

class BatchCompletedRabbitAmpqListenerSpec extends Specification {

  def outputRecommendationFacade = Mock(OutputRecommendationFacade)
  def mapper = Mock(BatchMapper)

  @Subject
  def underTest = new BatchCompletedRabbitAmqpListener(mapper, outputRecommendationFacade)

  def "should call output recommendation facade with mapped command"() {
    given:
    def message = MessageBatchCompleted.newBuilder()
        .setBatchId("some-batch-id")
        .setAnalysisName("some-analysis-name")
        .setBatchMetadata("some-metadata")
        .build()
    def expectedCommand = PrepareRecommendationResponseCommand.builder()
        .batchId(message.batchId)
        .analysisName(message.analysisName)
        .batchMetadata(message.batchMetadata)
        .build()
    1 * mapper.fromBatchCompletedMessage(message) >> expectedCommand

    when:
    underTest.subscribe(message)

    then:
    1 * outputRecommendationFacade.prepareCompletedBatchRecommendations(expectedCommand)
  }
}
