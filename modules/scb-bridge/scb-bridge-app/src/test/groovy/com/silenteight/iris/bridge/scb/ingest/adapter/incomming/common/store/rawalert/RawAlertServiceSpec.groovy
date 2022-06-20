/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert

import com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlert.AlertType
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator

import spock.lang.Specification

class RawAlertServiceSpec extends Specification {

  private repository = Mock(RawAlertRepository)
  private underTest = new RawAlertService(repository)

  def 'should store raw alerts with internalBatchId'() {
    given:
    def alert = Fixtures.RECOMMENDATION_ALERT
    def internalBatchId = InternalBatchIdGenerator.generate()

    when:
    underTest.store(internalBatchId, List.of(alert))

    then:
    1 * repository.saveAll(_ as List<RawAlert>) >> {
      def argument = it[0] as List<RawAlert>
      def entity = argument.first()

      assert entity.systemId == alert.details().systemId
      assert entity.batchId == alert.details().batchId
      assert entity.internalBatchId == internalBatchId
      assert entity.alertType == AlertType.SOLVING
      assert entity.payload != null
    }
  }
}
