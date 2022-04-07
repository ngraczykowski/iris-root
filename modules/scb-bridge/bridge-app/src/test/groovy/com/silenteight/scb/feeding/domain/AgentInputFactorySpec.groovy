package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.category.CategoryValue
import com.silenteight.scb.feeding.domain.agentinput.AgentInputFactory
import com.silenteight.scb.feeding.domain.agentinput.feature.FeatureFactory
import com.silenteight.scb.feeding.fixtures.Fixtures
import com.silenteight.universaldatasource.api.library.Feature

import spock.lang.Specification

class AgentInputFactorySpec extends Specification {

  def "should fail when no features were initialized"() {
    when:
    new AgentInputFactory([], [])

    then:
    thrown(IllegalStateException)
  }

  def "should create agent inputs"() {
    given:
      def alert = Fixtures.LEARNING_ALERT
      def match = Fixtures.MATCH
      def featureInputFactory = Mock(FeatureFactory)
      def agentInputs = [featureInputFactory, featureInputFactory]
      def categoryValues = [
          Mock(CategoryValue),
          Mock(CategoryValue)
      ]
      def feedingService = new AgentInputFactory(agentInputs, categoryValues)

    when:
      feedingService.createAgentInputIns(alert, match)

    then:
      agentInputs.each {1 * it.create(alert, match) >> Mock(Feature)}
  }

  def "should create category values"() {
    given:
    def agentInputs = [
        Mock(FeatureFactory),
        Mock(FeatureFactory)
    ]
    def categoryValues = [
        Mock(CategoryValue),
        Mock(CategoryValue)
    ]

    def feedingService = new AgentInputFactory(agentInputs, categoryValues)
    def alert = Fixtures.LEARNING_ALERT
    def match = Fixtures.MATCH

    when:
    feedingService.createCategoryValuesIns(alert, match)

    then:
    categoryValues.each {1 * it.createCategoryValuesIn(alert, match)}
  }
}
