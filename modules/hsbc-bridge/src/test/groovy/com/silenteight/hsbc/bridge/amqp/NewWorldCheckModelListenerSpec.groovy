package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import spock.lang.Specification

class NewWorldCheckModelListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def transferManager = Mock(WorldCheckModelManager)
  def underTest = new NewWorldCheckModelListener(transferManager)

  def 'should handle new model from WorldCheck'() {
    given:
    def modelPersisted = fixtures.modelPersisted
    def modelInfo = fixtures.modelInfoWorldCheck

    when:
    underTest.onModelChange(modelPersisted)

    then:
    1 * transferManager.transferModelToJenkins(modelInfo)
  }
}
