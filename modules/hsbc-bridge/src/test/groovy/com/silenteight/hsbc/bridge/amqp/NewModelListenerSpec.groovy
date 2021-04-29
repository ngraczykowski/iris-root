package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.transfer.ProcessManager

import spock.lang.Specification

class NewModelListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def processManager = Mock(ProcessManager)
  def underTest = new NewModelListener(processManager)

  def 'should handle new model from Governance'() {
    given:
    def modelInfo = fixtures.modelInfo

    when:
    underTest.onModelChange(modelInfo)

    then:
    1 * processManager.execute(modelInfo)
  }
}
