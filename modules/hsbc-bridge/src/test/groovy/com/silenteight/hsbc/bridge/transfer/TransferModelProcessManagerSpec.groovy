package com.silenteight.hsbc.bridge.transfer

import com.silenteight.hsbc.bridge.jenkins.Fixtures

import spock.lang.Specification

class TransferModelProcessManagerSpec extends Specification {

  def fixtures = new Fixtures()
  def modelClient = Mock(ModelClient)
  def transferServiceClient = Mock(TransferServiceClient)
  def underTest = new TransferModelProcessManager(modelClient, transferServiceClient)

  def 'should execute process of transferring model to Governance'() {
    given:
    def modelInfo = fixtures.modelInfo
    def model = fixtures.model

    when:
    underTest.execute(modelInfo)

    then:
    1 * modelClient.getModel(modelInfo) >> model
    1 * transferServiceClient.transfer(model)
  }
}
