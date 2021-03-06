package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.AlertSender.AlertDataComposite
import com.silenteight.hsbc.bridge.alert.dto.AlertEntityDto
import com.silenteight.hsbc.bridge.alert.dto.AlertMatchEntityDto
import com.silenteight.hsbc.bridge.alert.dto.AlertMetadataDto
import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation

import spock.lang.Specification

class AlertMapperSpec extends Specification {

  def payloadConverter = Mock(AlertPayloadConverter)
  def analystDecisionMapper = Mock(AnalystDecisionMapper)
  def caseCommentsMapper = Mock(CaseCommentsMapper)
  def underTest = new AlertMapper(payloadConverter, analystDecisionMapper, caseCommentsMapper)

  def "Should find and return collection of Alerts"() {
    given:
    payloadConverter.convertAlertDataToMap(*_) >> Collections.emptyMap()
    caseCommentsMapper.getLastCaseCommentWithDate(*_) >> Collections.emptyMap()

    when:
    def find = underTest.toReportAlerts(ALERT_INFORMATION)

    then:
    def alert = find.first()
    alert.discriminator == "1234_someDiscriminator"
    alert.metadata.get("id") == "1234"
    alert.metadata.get("status") == "STORED"
    alert.metadata.get("errorMessage") == ""
    alert.metadata.get("extendedAttribute5") == "SAN"
    alert.metadata.get("analyst_decision") == ""

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
    caseCommentsMapper.getLastCaseCommentWithDate(*_) >> Collections.emptyMap()

    when:
    def find = underTest.toReportAlerts(ALERT_INFORMATION)

    then:
    def result = find.first().getMetadata()
    result.get("customerId") == "someId"
    result.get("someKey") == "someValue"
  }

  static List<AlertDataComposite> ALERT_INFORMATION = [
      new AlertDataComposite(
          AlertEntityDto.builder()
              .externalId("1234")
              .name("alertName1")
              .discriminator("someDiscriminator")
              .bulkId("1")
              .status(AlertStatus.STORED)
              .payload(new byte[0])
              .matches(
                  [AlertMatchEntityDto.builder().name("matchName1").externalId("123").build(),
                   AlertMatchEntityDto.builder().name("matchName2").externalId("124").build()
                  ])
              .metadata(
                  [
                      new AlertMetadataDto("extendedAttribute5", "SAN"),
                      new AlertMetadataDto("trackingId", "ddcc1234")
                  ])
              .build(),
          new AlertData(
              caseInformation: new CaseInformation(
                  currentState: 'someCurrentState'
              )
          )
      )
  ]
}
