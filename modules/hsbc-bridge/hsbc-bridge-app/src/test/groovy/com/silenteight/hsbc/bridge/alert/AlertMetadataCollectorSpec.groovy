package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation
import com.silenteight.hsbc.bridge.json.external.model.CustomerIndividual

import spock.lang.Specification

class AlertMetadataCollectorSpec extends Specification {

  def underTest = new AlertMetadataCollector()

  def 'should collect metadata from alertData'() {
    given:
    def alertData = new AlertData(
        caseInformation: new CaseInformation(
            id: 'someCaseId',
            extendedAttribute5: 'extendedAttribute5',
            flagKey: 'flagKey',
            keyLabel: 'keyLabel'
        )
    )
    alertData.getCustomerIndividuals().add(
        new CustomerIndividual(edqLobCountryCode: 'edqLobCountryCodeIndividual')
    )

    when:
    def result = underTest.collectFromAlertData(alertData)

    then:
    result == [
        new AlertMetadata(key: 'discriminator', value: 'keyLabel_flagKey'),
        new AlertMetadata(key: 'trackingId', value: 'flagKey'),
        new AlertMetadata(key: 's8_lobCountryCode', value: 'edqLobCountryCodeIndividual'),
        new AlertMetadata(key: 'extendedAttribute5', value: 'extendedAttribute5'),
        new AlertMetadata(key: 'clientId', value: 'someCaseId')
    ] as Set<AlertMetadata>
  }

  def 'should map countries from alertData'() {
    given:
    def alertData = new AlertData(
        caseInformation: new CaseInformation(
            id: 'someCaseId',
            extendedAttribute5: 'extendedAttribute5',
            flagKey: 'flagKey',
            keyLabel: 'keyLabel'
        )
    )
    alertData.getCustomerIndividuals().add(
        new CustomerIndividual(edqLobCountryCode: 'UK')
    )

    when:
    def result = underTest.collectFromAlertData(alertData)

    then:
    result == [
        new AlertMetadata(key: 'discriminator', value: 'keyLabel_flagKey'),
        new AlertMetadata(key: 'trackingId', value: 'flagKey'),
        new AlertMetadata(key: 's8_lobCountryCode', value: 'GB'),
        new AlertMetadata(key: 'extendedAttribute5', value: 'extendedAttribute5'),
        new AlertMetadata(key: 'clientId', value: 'someCaseId')
    ] as Set<AlertMetadata>
  }

  def 'should not map empty country from alertData'() {
    given:
    def alertData = new AlertData(
        caseInformation: new CaseInformation(
            id: 'someCaseId',
            extendedAttribute5: 'extendedAttribute5',
            flagKey: 'flagKey',
            keyLabel: 'keyLabel'
        )
    )
    alertData.getCustomerIndividuals().add(
        new CustomerIndividual(edqLobCountryCode: '')
    )

    when:
    def result = underTest.collectFromAlertData(alertData)

    then:
    result == [
        new AlertMetadata(key: 'discriminator', value: 'keyLabel_flagKey'),
        new AlertMetadata(key: 'trackingId', value: 'flagKey'),
        new AlertMetadata(key: 's8_lobCountryCode', value: ''),
        new AlertMetadata(key: 'extendedAttribute5', value: 'extendedAttribute5'),
        new AlertMetadata(key: 'clientId', value: 'someCaseId')
    ] as Set<AlertMetadata>
  }
}
