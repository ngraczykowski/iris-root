package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification

class AlertInfoFinderSpec extends Specification {

  static Collection<Long> ALERT_IDS = List.of(1L)

  def alertRepository = Mock(AlertRepository)
  def payloadConverter = Mock(AlertPayloadConverter)
  def underTest = new AlertInfoFinder(alertRepository, payloadConverter)

  def "Should find and return collection of Alerts"() {
    given:
    alertRepository.findByIdIn(ALERT_IDS) >> ALERT_ENTITIES
    payloadConverter.convertPayloadToMap(*_) >> new HashMap<String, String>()

    when:
    def find = underTest.find(ALERT_IDS)

    then:
    def alert = find.first()
    alert.name == "alertName1"
    alert.metadata.get("id") == "1234"
    alert.metadata.get("status") == "STORED"
    alert.metadata.get("errorMessage") == ""
    alert.metadata.get("extendedAttribute5") == "SAN"

    def match = alert.matches.first()
    match.name == "matchName1"
    match.metadata.get("id") == "123"
  }

  def "Should add payload values to map"() {
    given:
    def payloadMap = new HashMap<>(
        "customerId": "someId",
        "someKey": "someValue"
    )
    alertRepository.findByIdIn(ALERT_IDS) >> ALERT_ENTITIES
    payloadConverter.convertPayloadToMap(*_) >> payloadMap

    when:
    def find = underTest.find(ALERT_IDS)

    then:
    def result = find.first().getMetadata()
    result.get("customerId") == "someId"
    result.get("someKey") == "someValue"
  }

  static List<AlertEntity> ALERT_ENTITIES = [
      new AlertEntity(
          id: 10,
          externalId: "1234",
          name: "alertName1",
          discriminator: "someDiscriminator",
          bulkId: "1",
          status: AlertStatus.STORED,
          payload: new AlertDataPayloadEntity(),
          matches: [
              new AlertMatchEntity(
                  id: 1,
                  externalId: "123",
                  name: "matchName1"
              ),
              new AlertMatchEntity(
                  id: 2,
                  externalId: "124",
                  name: "matchName2"
              )
          ],
          metadata: [
              new AlertMetadata(
                  key: "extendedAttribute5",
                  value: "SAN"
              ),
              new AlertMetadata(
                  key: "trackingId",
                  value: "ddcc1234"
              )
          ]
      )
  ]
}
