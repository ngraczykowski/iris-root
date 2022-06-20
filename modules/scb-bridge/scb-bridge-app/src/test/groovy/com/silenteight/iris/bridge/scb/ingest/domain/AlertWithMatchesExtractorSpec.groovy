/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.iris.bridge.scb.ingest.domain.model.AlertStatus.FAILURE
import static com.silenteight.iris.bridge.scb.ingest.domain.model.AlertStatus.SUCCESS

class AlertWithMatchesExtractorSpec extends Specification {

  @Subject
  def underTest = new AlertWitchMatchesExtractor()

  @Unroll
  def 'should extract alert with status #status'() {
    given:
    def alertId = com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
        .sourceId('alertId')
        .build()

    def alert = com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
        .id(alertId)
        .matches(matches as List<com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match>)
        .details(com
            .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder()
            .watchlistId('watchlistId')
            .build())
        .build()

    when:
    def result = underTest.extract(alert)

    then:
    result.getStatus() == status
    if (result.status == FAILURE) {
      assert result.alertId == 'alertId'
      assert result.alertErrorDescription == errorDescription
      println result
    } else {
      assert result.alertId == 'alertId'
      assert result.alertErrorDescription == null
      println result
    }

    where:
    status  | errorDescription                 | matches
    SUCCESS | null                                                                                 | List.of(com
        .silenteight
        .iris
        .bridge
        .scb
        .ingest
        .fixtures
        .Fixtures.MATCH_ALERT)
    FAILURE | com.silenteight.iris.bridge.scb.ingest.domain.model.AlertErrorDescription.NO_MATCHES | Collections.emptyList()
  }
}
