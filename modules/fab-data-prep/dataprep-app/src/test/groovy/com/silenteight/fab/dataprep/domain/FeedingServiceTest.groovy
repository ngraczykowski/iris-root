package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FabFeature
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient

import spock.lang.Specification

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
        .registeredAlert(
            RegisteredAlert.builder()
                .batchName(Fixtures.BATCH_NAME)
                .messageName(Fixtures.MESSAGE_NAME)
                .alertName(Fixtures.ALERT_NAME)
                .matches(
                    [Match.builder()
                         .hitName(Fixtures.HIT_NAME)
                         .matchName(Fixtures.MATCH_NAME)
                         .build()
                    ])
                .build())
        .build()

    when:
    featureService.createFeatureInputs(command)

    then:
    features.each {1 * it.buildFeature(_) }
    1 * agentInputServiceClient.createBatchCreateAgentInputs(_) >> []
    1 * categoryService.createCategoryInputs(command)
  }
}
