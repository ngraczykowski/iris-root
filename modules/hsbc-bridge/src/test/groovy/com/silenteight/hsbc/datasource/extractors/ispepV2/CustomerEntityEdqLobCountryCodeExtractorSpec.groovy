package com.silenteight.hsbc.datasource.extractors.ispepV2

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class CustomerEntityEdqLobCountryCodeExtractorSpec extends Specification {

  def 'returns correct values'() {
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
    assertThat(actual).containsExactly('EG', 'TR')
  }
}
