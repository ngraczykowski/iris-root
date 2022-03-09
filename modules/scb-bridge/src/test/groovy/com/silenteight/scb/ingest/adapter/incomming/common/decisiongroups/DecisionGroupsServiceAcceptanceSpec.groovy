package com.silenteight.scb.ingest.adapter.incomming.common.decisiongroups

import com.silenteight.proto.serp.v1.integration.DecisionGroups
import com.silenteight.sep.base.testing.messaging.MessageSenderSpy

import spock.lang.Specification

class DecisionGroupsServiceAcceptanceSpec extends Specification {

  MessageSenderSpy messageSender = new MessageSenderSpy()
  DecisionGroupsConfiguration configuration = new DecisionGroupsConfiguration({messageSender})
  Set<String> sourceDecisionGroups = []

  DecisionGroupsReader reader = {sourceDecisionGroups}
  DecisionGroupsService service = configuration.decisionGroupsService(reader)

  def "publish decision groups on application start"() {
    given:
    def expectedDecisionGroups = sourceDecisionGroups = [
        "group 1",
        "group 2"
    ] as Set

    when:
    service.applicationStarted()

    then:
    sentDecisionGroups == expectedDecisionGroups
  }

  private Set<String> getSentDecisionGroups() {
    assert messageSender.sentMessage instanceof DecisionGroups
    ((DecisionGroups) messageSender.sentMessage).decisionGroupList as Set
  }
}
