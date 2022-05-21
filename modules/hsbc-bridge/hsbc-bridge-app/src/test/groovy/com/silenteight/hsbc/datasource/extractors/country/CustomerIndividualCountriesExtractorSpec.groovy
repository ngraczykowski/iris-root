package com.silenteight.hsbc.datasource.extractors.country

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual

import spock.lang.Specification

import java.util.stream.Collectors

import static org.assertj.core.api.Assertions.assertThat

class CustomerIndividualCountriesExtractorSpec extends Specification {

  def 'should split fields by comma and semicolon'() {
    given:
    def customerIndividuals = [
        Stub(CustomerIndividual) {
          getNationalityOrCitizenship() >> nationalityOrCitizenship
          getNationalityCountries() >> nationalityCountries
          getPassportIssueCountry() >> passportIssueCountry
        }]

    when:
    def underTest = new CustomerIndividualCountriesExtractor(customerIndividuals)
    def actual = underTest.extract()

    then:
    actual.collect(Collectors.toList()) == countriesExpected

    where:
    nationalityOrCitizenship | nationalityCountries | passportIssueCountry | countriesExpected
    "AUSTRALIA;GERMANY"      | "AU, GER"            | "AUSTRALIA"          | ["AUSTRALIA",
                                                                              "GERMANY", "AU",
                                                                              "GER", "AUSTRALIA"]
  }

  def 'should remove only backslash and quote signs'() {
    given:
    def customerIndividuals = [
        Stub(CustomerIndividual) {
          getNationalityOrCitizenship() >> nationalityOrCitizenship
          getNationalityCountries() >> nationalityCountries
        }]

    when:
    def underTest = new CustomerIndividualCountriesExtractor(customerIndividuals)
    def actual = underTest.extract()

    then:
    actual.collect(Collectors.toList()) == countriesExpected

    where:
    nationalityOrCitizenship              | nationalityCountries | countriesExpected
    "Republic of the Congo;Guinea-Bissau" | "\"COG\", \"GNB\""   | ["Republic of the Congo",
                                                                    "Guinea-Bissau", "COG", "GNB"]
  }

  def "returns correct values"() {
    given:
    def customerIndividuals =
        [
            Stub(CustomerIndividual) {
              getNationalityCitizenshipCountries() >> Fixtures.NATIONALITY_CITIZENSHIP
              getNationalityOrCitizenship() >> Fixtures.NATIONALITY_OR_CITIZENSHIP
              getNationalityCountries() >> Fixtures.NATIONALITY_COUNTRIES
              getPassportIssueCountry() >> Fixtures.PASSPORT_ISSUE_COUNTRY
              getCountryOfBirthOriginal() >> Fixtures.COUNTRY_OF_BIRTH_ORIGINAL
              getCountryOfBirth() >> Fixtures.COUNTRY_OF_BIRTH
              getEdqBirthCountryCode() >> Fixtures.COUNTRY_EDQ_BIRTH_COUNTRY_CODE
            }
        ]

    when:
    def underTest = new CustomerIndividualCountriesExtractor(customerIndividuals)
    def actual = underTest.extract()

    then:
    assertThat(actual)
        .containsExactlyInAnyOrder(
            Fixtures.NATIONALITY_CITIZENSHIP,
            Fixtures.NATIONALITY_OR_CITIZENSHIP,
            Fixtures.NATIONALITY_COUNTRIES,
            Fixtures.PASSPORT_ISSUE_COUNTRY,
            Fixtures.COUNTRY_OF_BIRTH_ORIGINAL,
            Fixtures.COUNTRY_OF_BIRTH,
            Fixtures.COUNTRY_EDQ_BIRTH_COUNTRY_CODE
        )
  }

  class Fixtures {

    static def NATIONALITY_CITIZENSHIP = "nationalityCitizenship"
    static def NATIONALITY_OR_CITIZENSHIP = "nationalityOrCitizenship"
    static def NATIONALITY_COUNTRIES = "nationalityCountries"
    static def PASSPORT_ISSUE_COUNTRY = "passportIssueCountry"
    static def COUNTRY_OF_BIRTH_ORIGINAL = "countryOfBirthOriginal"
    static def COUNTRY_OF_BIRTH = "countryOfBirth"
    static def COUNTRY_EDQ_BIRTH_COUNTRY_CODE = "countryEdqBirthCountryCode"
  }
}
