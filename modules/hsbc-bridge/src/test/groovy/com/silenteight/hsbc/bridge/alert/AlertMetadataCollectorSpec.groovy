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
        new AlertMetadata(key: 'extendedAttribute5', value: 'extendedAttribute5')
    ] as Set<AlertMetadata>
  }
}
