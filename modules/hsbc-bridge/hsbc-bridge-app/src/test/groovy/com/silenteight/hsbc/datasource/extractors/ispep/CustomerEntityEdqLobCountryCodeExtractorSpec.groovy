package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity

import spock.lang.Specification

class CustomerEntityEdqLobCountryCodeExtractorSpec extends Specification {

  def 'returns correct value for customerEntities even if edqLobCountry that should be the same has different value'() {
    given:
    def firstLobCountryCode = Mock(CustomerEntity) {
      getEdqLobCountryCode() >> 'EG'
    }

    def secondLobCountryCode = Mock(CustomerEntity) {
      getEdqLobCountryCode() >> 'TR'
    }

    when:
    def underTest = new CustomerEntityEdqLobCountryCodeExtractor([firstLobCountryCode, secondLobCountryCode])
    def actual = underTest.extract()

    then:
    actual == 'EG'
  }
}
