package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import spock.lang.Specification

class WorldCheckModelTransferStatusListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def transferManager = Mock(WorldCheckModelManager)
  def underTest = new WorldCheckModelTransferStatusListener(transferManager)

  def 'should handle that NAME_ALIASES model was loaded in WorldCheck'() {
    given:
    def modelStatusUpdated = fixtures.nameModelStatusUpdated
    def modelStatusUpdatedDto = fixtures.nameModelStatusUpdatedDto

    when:
    underTest.handleWorldCheckStatus(modelStatusUpdated)

    then:
    1 * transferManager.transferWorldCheckModelStatus(modelStatusUpdatedDto)
  }

  def 'should handle that IS_PEP_PROCEDURAL model was loaded in WorldCheck'() {
    given:
    def modelStatusUpdated = fixtures.proceduralModelStatusUpdated
    def modelStatusUpdatedDto = fixtures.proceduralModelStatusUpdatedDto

    when:
    underTest.handleWorldCheckStatus(modelStatusUpdated)

    then:
    1 * transferManager.transferWorldCheckModelStatus(modelStatusUpdatedDto)
  }

  def 'should handle that IS_PEP_HISTORICAL model was loaded in WorldCheck'() {
    given:
    def modelStatusUpdated = fixtures.historicalModelStatusUpdated
    def modelStatusUpdatedDto = fixtures.historicalModelStatusUpdatedDto

    when:
    underTest.handleWorldCheckStatus(modelStatusUpdated)

    then:
    1 * transferManager.transferWorldCheckModelStatus(modelStatusUpdatedDto)
  }
}
