package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.AlertGetter.AlertInformation
import com.silenteight.hsbc.bridge.json.external.model.AlertData

import spock.lang.Specification

class AlertMapperSpec extends Specification {

  def payloadConverter = Mock(AlertPayloadConverter)
  def underTest = new AlertMapper(payloadConverter)

  def "Should find and return collection of Alerts"() {
    given:
    payloadConverter.convertAlertDataToMap(*_) >> new HashMap<String, String>()

    when:
    def find = underTest.toReportAlerts(ALERT_INFORMATION)

    then:
    def alert = find.first()
    alert.discriminator == "1234_someDiscriminator"
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
    payloadConverter.convertAlertDataToMap(*_) >> payloadMap

    when:
    def find = underTest.toReportAlerts(ALERT_INFORMATION)

    then:
    def result = find.first().getMetadata()
    result.get("customerId") == "someId"
    result.get("someKey") == "someValue"
  }

  static List<AlertInformation> ALERT_INFORMATION = [
      new AlertInformation(
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
                new AlertMetadata("extendedAttribute5", "SAN"),
                new AlertMetadata("trackingId", "ddcc1234")
            ]
        ),
          new AlertData()
      )
  ]
}
