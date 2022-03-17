package com.silenteight.scb.ingest.domain

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match
import com.silenteight.scb.ingest.domain.model.AlertErrorDescription
import com.silenteight.scb.ingest.fixtures.Fixtures

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.scb.ingest.domain.model.AlertStatus.FAILURE
import static com.silenteight.scb.ingest.domain.model.AlertStatus.SUCCESS

class AlertWithMatchesExtractorSpec extends Specification {

  @Subject
  def underTest = new AlertWitchMatchesExtractor()

  @Unroll
  def 'should extract alert with status #status'() {
    given:
    def alertId = ObjectId.builder()
        .sourceId('alertId')
        .build()

    def alert = Alert.builder()
        .id(alertId)
        .matches(matches as List<Match>)
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
    SUCCESS | null                             | List.of(Fixtures.MATCH_ALERT)
    FAILURE | AlertErrorDescription.NO_MATCHES | Collections.emptyList()
  }
}
