package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FabFeature
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient

import spock.lang.Specification

import static com.silenteight.fab.dataprep.domain.Fixtures.REGISTERED_ALERT

class FeedingServiceTest extends Specification {

  AgentInputServiceClient agentInputServiceClient = Mock()

  CategoryService categoryService = Mock()

  def "should fail when no features were initialized"() {
    when:
    new FeedingService([], agentInputServiceClient, categoryService)

    then:
    thrown(IllegalStateException)
  }

  def "should call all features"() {
    given:
    def features = [
        Mock(FabFeature),
        Mock(FabFeature)
    ]

    def featureService = new FeedingService(features, agentInputServiceClient, categoryService)

    def command = FeatureInputsCommand.builder()
        .registeredAlert(REGISTERED_ALERT)
        .build()

    when:
    featureService.createFeatureInputs(command)

    then:
    features.each {1 * it.buildFeature(_) }
    1 * agentInputServiceClient.createBatchCreateAgentInputs(_) >> []
    1 * categoryService.createCategoryInputs(command)
  }
}
