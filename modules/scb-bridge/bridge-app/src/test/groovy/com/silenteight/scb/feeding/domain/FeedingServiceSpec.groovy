package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.agentinput.AgentInput
import com.silenteight.scb.feeding.domain.category.CategoryValue
import com.silenteight.scb.feeding.fixtures.Fixtures

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
    def agentInputs = [
        Mock(AgentInput),
        Mock(AgentInput)
    ]
    def categoryValues = [
        Mock(CategoryValue),
        Mock(CategoryValue)
    ]

    def featureService = new FeedingService(agentInputs, categoryValues)
    def alert = Fixtures.LEARNING_ALERT
    def match = Fixtures.MATCH

    when:
    featureService.createAgentInputIns(alert, match)

    then:
    agentInputs.each {1 * it.createAgentInput(alert, match)}
  }

  def "should create category values"() {
    given:
    def agentInputs = [
        Mock(AgentInput),
        Mock(AgentInput)
    ]
    def categoryValues = [
        Mock(CategoryValue),
        Mock(CategoryValue)
    ]

    def featureService = new FeedingService(agentInputs, categoryValues)
    def alert = Fixtures.LEARNING_ALERT
    def match = Fixtures.MATCH

    when:
    featureService.createCategoryValuesIns(alert, match)

    then:
    categoryValues.each {1 * it.createCategoryValuesIn(alert, match)}
  }
}
