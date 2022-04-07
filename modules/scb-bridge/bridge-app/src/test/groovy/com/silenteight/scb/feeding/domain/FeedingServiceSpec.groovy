package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.category.CategoryValue
import com.silenteight.scb.feeding.domain.featureinput.FeatureInputFactory
import com.silenteight.scb.feeding.fixtures.Fixtures
import com.silenteight.universaldatasource.api.library.Feature

import spock.lang.Specification

class FeedingServiceSpec extends Specification {

  def "should fail when no features were initialized"() {
    when:
    new FeedingService([], [])

    then:
    thrown(IllegalStateException)
  }

  def "should create agent inputs"() {
    given:
      def alert = Fixtures.LEARNING_ALERT
      def match = Fixtures.MATCH
      def featureInputFactory = Mock(FeatureInputFactory)
      def agentInputs = [featureInputFactory, featureInputFactory]
      def categoryValues = [
          Mock(CategoryValue),
          Mock(CategoryValue)
      ]
      def feedingService = new FeedingService(agentInputs, categoryValues)

    when:
      feedingService.createAgentInputIns(alert, match)

    then:
      agentInputs.each {1 * it.create(alert, match) >> Mock(Feature)}
  }

  def "should create category values"() {
    given:
    def agentInputs = [
        Mock(FeatureInputFactory),
        Mock(FeatureInputFactory)
    ]
    def categoryValues = [
        Mock(CategoryValue),
        Mock(CategoryValue)
    ]

    def feedingService = new FeedingService(agentInputs, categoryValues)
    def alert = Fixtures.LEARNING_ALERT
    def match = Fixtures.MATCH

    when:
    feedingService.createCategoryValuesIns(alert, match)

    then:
    categoryValues.each {1 * it.createCategoryValuesIn(alert, match)}
  }
}
