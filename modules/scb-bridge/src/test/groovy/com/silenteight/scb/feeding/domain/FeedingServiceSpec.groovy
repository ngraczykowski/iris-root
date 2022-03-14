package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.agentinput.AgentInput
import com.silenteight.scb.feeding.fixtures.Fixtures

import spock.lang.Specification

class FeedingServiceSpec extends Specification {

  def "should fail when no features were initialized"() {
    when:
    new FeedingService([])

    then:
    thrown(IllegalStateException)
  }

  def "should call all features"() {
    given:
    def features = [
        Mock(AgentInput),
        Mock(AgentInput)
    ]

    def featureService = new FeedingService(features)
    def alert = Fixtures.ALERT
    def match = Fixtures.MATCH

    when:
    featureService.createAgentInputIns(alert, match)

    then:
    features.each {1 * it.createAgentInput(alert, match)}
  }
}
