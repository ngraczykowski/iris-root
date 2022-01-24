package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.model.Match.Status

import org.assertj.core.api.Assertions
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

  def 'should map projections to alerts with matches'() {
    given:
    def projections = [
        Fixtures.FIRST_PROJECTION, Fixtures.SECOND_PROJECTION, Fixtures.THIRD_PROJECTION
    ]

    def expected = [Fixtures.FIRST_ALERT_WITH_MATCHES, Fixtures.SECOND_ALERT_WITH_MATCHES]

    when:
    def result = underTest.toAlertWithMatches(projections)

    then:
    Assertions.assertThat(result)
        .hasSameSizeAs(expected)
        .containsAll(expected)
  }

  class Fixtures {

    static def FIRST_ALERT_ID = 'id_1'
    static def FIRST_ALERT_NAME = 'name_1'
    static def FIRST_ALERT_STATUS = 'RECOMMENDED'
    static def FIRST_ALERT_METADATA = 'metadata_1'
    static def FIRST_ALERT_ERROR_DESCRIPTION = 'error_description_1'

    static def SECOND_ALERT_ID = 'id_2'
    static def SECOND_ALERT_NAME = 'name_2'
    static def SECOND_ALERT_STATUS = 'RECOMMENDED'
    static def SECOND_ALERT_METADATA = 'metadata_2'
    static def SECOND_ALERT_ERROR_DESCRIPTION = 'error_description_2'

    static def FIRST_MATCH_ID = 'match_1'
    static def FIRST_MATCH_NAME = 'match_name_1'

    static def SECOND_MATCH_ID = 'match_2'
    static def SECOND_MATCH_NAME = 'match_name_2'

    static def FIRST_PROJECTION = AlertWithMatchNamesProjection.builder()
        .alertId(FIRST_ALERT_ID)
        .alertName(FIRST_ALERT_NAME)
        .alertStatus(FIRST_ALERT_STATUS)
        .alertMetadata(FIRST_ALERT_METADATA)
        .alertErrorDescription(FIRST_ALERT_ERROR_DESCRIPTION)
        .matchId(FIRST_MATCH_ID)
        .matchName(FIRST_MATCH_NAME)
        .build()

    static def SECOND_PROJECTION = AlertWithMatchNamesProjection.builder()
        .alertId(FIRST_ALERT_ID)
        .alertName(FIRST_ALERT_NAME)
        .alertStatus(FIRST_ALERT_STATUS)
        .alertMetadata(FIRST_ALERT_METADATA)
        .alertErrorDescription(FIRST_ALERT_ERROR_DESCRIPTION)
        .matchId(SECOND_MATCH_ID)
        .matchName(SECOND_MATCH_NAME)
        .build()

    static def THIRD_PROJECTION = AlertWithMatchNamesProjection.builder()
        .alertId(SECOND_ALERT_ID)
        .alertName(SECOND_ALERT_NAME)
        .alertStatus(SECOND_ALERT_STATUS)
        .alertMetadata(SECOND_ALERT_METADATA)
        .alertErrorDescription(SECOND_ALERT_ERROR_DESCRIPTION)
        .matchId(FIRST_MATCH_ID)
        .matchName(FIRST_MATCH_NAME)
        .build()

    static def FIRST_ALERT_WITH_MATCHES = AlertWithMatches.builder()
        .id(FIRST_ALERT_ID)
        .name(FIRST_ALERT_NAME)
        .status(Alert.Status.RECOMMENDED)
        .metadata(FIRST_ALERT_METADATA)
        .errorDescription(FIRST_ALERT_ERROR_DESCRIPTION)
        .matches(
            [
                new AlertWithMatches.Match(FIRST_MATCH_ID, FIRST_MATCH_NAME),
                new AlertWithMatches.Match(SECOND_MATCH_ID, SECOND_MATCH_NAME),
            ])
        .build()

    static def SECOND_ALERT_WITH_MATCHES = AlertWithMatches.builder()
        .id(SECOND_ALERT_ID)
        .name(SECOND_ALERT_NAME)
        .status(Alert.Status.RECOMMENDED)
        .metadata(SECOND_ALERT_METADATA)
        .errorDescription(SECOND_ALERT_ERROR_DESCRIPTION)
        .matches(
            [
                new AlertWithMatches.Match(FIRST_MATCH_ID, FIRST_MATCH_NAME),
            ])
        .build()
  }
}
