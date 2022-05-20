package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import spock.lang.Specification

class NewWorldCheckModelListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def transferManager = Mock(WorldCheckModelManager)
  def underTest = new NewWorldCheckModelListener(
      fixtures.bridgeApiProperties.getAddress(), transferManager)

  def 'should handle new NAME_ALIASES model from WorldCheck'() {
    given:
    def modelPersisted = fixtures.nameModelPersisted
    def modelInfo = fixtures.modelInfoWorldCheckNameAliases

    when:
    underTest.onModelChange(modelPersisted)

    then:
    1 * transferManager.transferModelToJenkins(modelInfo)
  }

  def 'should handle new IS_PEP_PROCEDURAL model from WorldCheck'() {
    given:
    def modelPersisted = fixtures.proceduralModelPersisted
    def modelInfo = fixtures.modelInfoWorldCheckIsPepProcedural

    when:
    underTest.onModelChange(modelPersisted)

    then:
    1 * transferManager.transferModelToJenkins(modelInfo)
  }

  def 'should handle new IS_PEP_HISTORICAL model from WorldCheck'() {
    given:
    def modelPersisted = fixtures.historicalModelPersisted
    def modelInfo = fixtures.modelInfoWorldCheckIsPepHistorical

    when:
    underTest.onModelChange(modelPersisted)

    then:
    1 * transferManager.transferModelToJenkins(modelInfo)
  }
}
