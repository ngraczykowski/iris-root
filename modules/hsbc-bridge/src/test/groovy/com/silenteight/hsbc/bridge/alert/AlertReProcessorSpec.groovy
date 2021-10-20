package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification

import java.util.stream.Stream
import javax.persistence.EntityManager

class AlertReProcessorSpec extends Specification {

  private static List<String> ALERTS = List.of('alert/1')
  private static String BULK_ID = "reRecommend-some-uuid"
  def alertRepository = Mock(AlertRepository)
  def entityManager = Mock(EntityManager)
  def underTest = new AlertReProcessor(alertRepository, entityManager)

  def "Should start re-processing alerts"() {
    when:
    underTest.reProcessAlerts(BULK_ID, ALERTS)

    then:
    1 * alertRepository.findByNameIn(ALERTS) >> Stream.of(getAlertEntity())
    1 * alertRepository.save(_ as AlertEntity)
  }

  private static AlertEntity getAlertEntity() {
    return new AlertEntity(
        id: 1,
        name: 'alert/1',
        payload: new AlertDataPayloadEntity(
            id: 1,
            payload: null
        )
    )
  }
}
