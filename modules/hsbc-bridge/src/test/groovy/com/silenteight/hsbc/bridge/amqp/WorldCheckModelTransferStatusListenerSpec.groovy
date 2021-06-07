package com.silenteight.hsbc.bridge.amqp

import com.silenteight.hsbc.bridge.jenkins.Fixtures
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager

import spock.lang.Specification

class WorldCheckModelTransferStatusListenerSpec extends Specification {

  def fixtures = new Fixtures()
  def transferManager = Mock(WorldCheckModelManager)
  def underTest = new WorldCheckModelTransferStatusListener(transferManager)

  def 'should handle that model was loaded in WorldCheck'() {
    given:
    def modelStatusUpdated = fixtures.modelStatusUpdated
    def modelStatusUpdatedDto = fixtures.modelStatusUpdatedDto

    when:
    underTest.handleWorldCheckStatus(modelStatusUpdated)

    then:
    1 * transferManager.transferWorldCheckModelStatus(modelStatusUpdatedDto)
  }
}
