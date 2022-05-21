package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager

import spock.lang.Specification

class NewGovernanceModelListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def transferManager = Mock(GovernanceModelManager)
  def underTest = new NewGovernanceModelListener(
      fixtures.bridgeApiProperties.getAddress(), transferManager)

  def 'should handle new model from Governance'() {
    given:
    def modelPromotedForProduction = fixtures.modelPromotedForProduction
    def modelInfo = fixtures.modelInfo

    when:
    underTest.onModelChange(modelPromotedForProduction)

    then:
    1 * transferManager.transferModelToJenkins(modelInfo)
  }
}
