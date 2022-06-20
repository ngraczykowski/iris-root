/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain

import com.silenteight.iris.bridge.scb.ingest.domain.model.DataRetentionCommand
import com.silenteight.iris.bridge.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert

import spock.lang.Specification
import spock.lang.Subject

class DataRetentionServiceSpec extends Specification {

  def rawAlertService = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService)

  @Subject
  def underTest = new DataRetentionService(rawAlertService)

  def "should remove expired raw alerts by internal batch id"() {
    given:
    def command = new DataRetentionCommand(
        [
            new DataRetentionAlert('systemId1', 'internalBatchId1'),
            new DataRetentionAlert('systemId2', 'internalBatchId2'),
            new DataRetentionAlert('systemId3', 'internalBatchId2')
        ])

    when:
    underTest.performDataRetention(command)

    then:
    1 * rawAlertService.removeExpiredAlerts('internalBatchId1', ['systemId1'] as Set)
    1 * rawAlertService.removeExpiredAlerts('internalBatchId2', ['systemId2', 'systemId3'] as Set)
  }
}
