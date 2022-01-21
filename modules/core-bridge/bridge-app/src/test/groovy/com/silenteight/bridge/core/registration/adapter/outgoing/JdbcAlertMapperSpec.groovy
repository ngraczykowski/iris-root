package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.model.Match.Status

import spock.lang.Specification
import spock.lang.Subject

class JdbcAlertMapperSpec extends Specification {

  @Subject
  def underTest = new JdbcAlertMapper()

  def 'should map to alert entity'() {
    given:
    def matchesIn = [
        Match.builder()
            .name('matchName')
            .status(Status.REGISTERED)
            .matchId('matchId')
            .build()
    ]

    def alertIn = Alert.builder()
        .name('alertName')
        .status(Alert.Status.REGISTERED)
        .alertId('alertId')
        .batchId('batchId')
        .metadata('metadata')
        .matches(matchesIn)
        .errorDescription('errorDescription')
        .build()

    when:
    def result = underTest.toAlertEntity(alertIn)

    then:
    with(result) {
      name() == 'alertName'
      status() == AlertEntity.Status.REGISTERED
      alertId() == 'alertId'
      batchId() == 'batchId'
      metadata() == 'metadata'
      errorDescription() == 'errorDescription'
      with(result.matches().first()) {
        name() == 'matchName'
        matchId() == 'matchId'
        status() == MatchEntity.Status.REGISTERED
      }
    }
  }

  def 'should map to alert id'() {
    given:
    def alertIdProjection = new AlertIdNameProjection('alertId', 'batchId', 'alertName')

    when:
    def result = underTest.toAlertId(alertIdProjection)

    then:
    result.alertId() == 'alertId'
  }

  def 'should map to alert name'() {
    given:
    def alertIdProjection = new AlertIdNameProjection('alertId', 'batchId', 'alertName')

    when:
    def result = underTest.toAlertName(alertIdProjection)

    then:
    result.alertName() == 'alertName'
  }

  def 'should map to alert'() {
    given:
    def matchEntities = [
        MatchEntity.builder()
            .name('matchName')
            .status(MatchEntity.Status.REGISTERED)
            .matchId('matchId')
            .build()
    ] as Set

    def alertEntity = AlertEntity.builder()
        .name('alertName')
        .status(AlertEntity.Status.REGISTERED)
        .alertId('alertId')
        .batchId('batchId')
        .metadata('metadata')
        .matches(matchEntities)
        .errorDescription('errorDescription')
        .build()

    when:
    def result = underTest.toAlert(alertEntity)

    then:
    with(result) {
      name() == 'alertName'
      status() == Alert.Status.REGISTERED
      alertId() == 'alertId'
      batchId() == 'batchId'
      metadata() == 'metadata'
      errorDescription() == 'errorDescription'
      with(result.matches().first()) {
        name() == 'matchName'
        matchId() == 'matchId'
        status() == Status.REGISTERED
      }
    }
  }

  def 'should map to alerts statistics'() {
    given:
    def alertStatusCountProjections = [
        new AlertStatusStatisticsProjection('RECOMMENDED', 1),
        new AlertStatusStatisticsProjection('ERROR', 2)
    ]

    when:
    def result = underTest.toAlertsStatistics(alertStatusCountProjections)

    then:
    result.getAlertCountByStatus(Alert.Status.RECOMMENDED) == 1
    result.getAlertCountByStatus(Alert.Status.ERROR) == 2
  }
}
