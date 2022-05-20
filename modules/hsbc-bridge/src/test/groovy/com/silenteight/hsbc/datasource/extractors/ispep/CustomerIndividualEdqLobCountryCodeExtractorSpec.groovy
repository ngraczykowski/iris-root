package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual

import spock.lang.Specification

class CustomerIndividualEdqLobCountryCodeExtractorSpec extends Specification {

  def 'returns correct value for customerIndividuals even if edqLobCountry that should be the same has different value'() {
    given:
    def firstLobCountryCode = Mock(CustomerIndividual) {
      getEdqLobCountryCode() >> 'SG'
    }

    def secondLobCountryCode = Mock(CustomerIndividual) {
      getEdqLobCountryCode() >> 'PL'
    }

    when:
    def underTest = new CustomerIndividualEdqLobCountryCodeExtractor([firstLobCountryCode, secondLobCountryCode])
    def actual = underTest.extract()

    then:
    actual == 'SG'
  }
}
