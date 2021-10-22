package com.silenteight.hsbc.datasource.extractors.ispepV2

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual

import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class CustomerIndividualEdqLobCountryCodeExtractorSpec extends Specification {

  def 'returns correct values'() {
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
    assertThat(actual).containsExactly('SG', 'PL')
  }
}
