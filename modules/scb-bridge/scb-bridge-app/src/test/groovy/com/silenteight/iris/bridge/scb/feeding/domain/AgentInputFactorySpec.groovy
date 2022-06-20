/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain

import com.silenteight.universaldatasource.api.library.Feature

import spock.lang.Specification

class AgentInputFactorySpec extends Specification {

  def "should fail when no features were initialized"() {
    when:
    new com.silenteight.iris.bridge.scb.feeding.domain.agent.input.AgentInputFactory([], [])

    then:
    thrown(IllegalStateException)
  }

  def "should create agent inputs"() {
    given:
      def alert = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.LEARNING_ALERT
      def match = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.MATCH
      def featureInputFactory = Mock(com
          .silenteight.iris.bridge.scb.feeding.domain.agent.input.feature.FeatureFactory)
      def agentInputs = [featureInputFactory, featureInputFactory]
      def categoryValues = [
          Mock(com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue),
          Mock(com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue)
      ]
      def feedingService = new com.silenteight.iris.bridge.scb.feeding.domain.agent.input.AgentInputFactory(agentInputs, categoryValues)

    when:
      feedingService.createAgentInputIns(alert, match)

    then:
      agentInputs.each {1 * it.create(alert, match) >> Mock(Feature)}
  }

  def "should create category values"() {
    given:
    def agentInputs = [
        Mock(com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature.FeatureFactory),
        Mock(com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature.FeatureFactory)
    ]
    def categoryValues = [
        Mock(com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue),
        Mock(com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue)
    ]

    def feedingService = new com.silenteight.iris.bridge.scb.feeding.domain.agent.input.AgentInputFactory(agentInputs, categoryValues)
    def alert = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.LEARNING_ALERT
    def match = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.MATCH

    when:
    feedingService.createCategoryValuesIns(alert, match)

    then:
    categoryValues.each {1 * it.createCategoryValuesIn(alert, match)}
  }
}
