package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FabFeature
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient

import spock.lang.Specification

class FeedingServiceTest extends Specification {

  AgentInputServiceClient agentInputServiceClient = Mock()

  def "should fail when no features were initialized"() {
    when:
    new FeedingService([], agentInputServiceClient)

    then:
    thrown(IllegalStateException)
  }

  def "should call all features"() {
    given:
    def features = [
        Mock(FabFeature),
        Mock(FabFeature)
    ]

    def featureService = new FeedingService(features, agentInputServiceClient)

    def command = FeatureInputsCommand.builder()
        .batchId('123')
        .build()

    when:
    featureService.createFeatureInputs(command)

    then:
    features.each {1 * it.createFeatureInput(command)}
    features.size() * agentInputServiceClient.createBatchCreateAgentInputs(_) >> []
  }
}
