package com.silenteight.hsbc.datasource.extractors.geolocation

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification
import spock.lang.Unroll

class GeoResidenciesFeatureQueryFacadeTest extends Specification {

  @Unroll
  def "Should return unique, uppercase and stripped residencies from all entities"() {
    given:
    def customerEntity1 = Mock(CustomerEntity) {
      getAddress() >> firstAddress
      getProfileFullAddress() >> firstFullAddress
    }

    def customerEntity2 = Mock(CustomerEntity) {
      getAddress() >> secondAddress
      getProfileFullAddress() >> secondsFullAddress
    }

    def matchData = Mock(MatchData) {
      getCustomerEntities() >> [customerEntity1, customerEntity2]
    }

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(matchData).getApEntitiesGeoResidencies()

    then:
    residencies == expected

    where:
    firstAddress        | firstFullAddress    | secondAddress          | secondsFullAddress || expected
    "Leningrad REGION"  | "Azovske Dzhankoi"  | "The triskellion us"   | "Khartoum"         || "LENINGRAD REGION AZOVSKE DZHANKOI THE TRISKELLION US KHARTOUM"
    "Leningrad REGION"  | "Azovske Dzhankoi"  | "Leningrad REGION"     | "Khartoum"         || "LENINGRAD REGION AZOVSKE DZHANKOI KHARTOUM"
    "Leningrad REGION"  | "Leningrad REGION"  | "Khartoum"             | "Khartoum"         || "LENINGRAD REGION KHARTOUM"
    "Leningrad REGION"  | "LENINGRAD REGION"  | "Leningrad REGION"     | "Leningrad REGION" || "LENINGRAD REGION"
    " Leningrad REGION" | "Azovske Dzhankoi " | "  The triskellion us" | "Khartoum  "       || "LENINGRAD REGION AZOVSKE DZHANKOI THE TRISKELLION US KHARTOUM"
  }

  @Unroll
  def "Should return unique, uppercase and stripped residencies from all individuals"() {
    given:
    def customerIndividual1 = Mock(CustomerIndividual) {
      getAddress() >> firstAddress
      getProfileFullAddress() >> firstFullAddress
    }

    def customerIndividual2 = Mock(CustomerIndividual) {
      getAddress() >> secondAddress
      getProfileFullAddress() >> secondsFullAddress
    }

    def matchData = Mock(MatchData) {
      getCustomerIndividuals() >> [customerIndividual1, customerIndividual2]
    }

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(
        matchData).getApIndividualsGeoResidencies()

    then:
    residencies == expected

    where:
    firstAddress        | firstFullAddress    | secondAddress          | secondsFullAddress || expected
    "LENINGRAD REGION"  | "AZOVSKE DZHANKOI"  | "The triskellion us"   | "Khartoum"         || "LENINGRAD REGION AZOVSKE DZHANKOI THE TRISKELLION US KHARTOUM"
    "LENINGRAD REGION"  | "AZOVSKE DZHANKOI"  | "LENINGRAD REGION"     | "Khartoum"         || "LENINGRAD REGION AZOVSKE DZHANKOI KHARTOUM"
    "LENINGRAD REGION"  | "LENINGRAD REGION"  | "Khartoum"             | "Khartoum"         || "LENINGRAD REGION KHARTOUM"
    "LENINGRAD REGION"  | "LENINGRAD REGION"  | "LENINGRAD REGION"     | "LENINGRAD REGION" || "LENINGRAD REGION"
    " LENINGRAD REGION" | "AZOVSKE DZHANKOI " | "  The triskellion us" | "Khartoum  "       || "LENINGRAD REGION AZOVSKE DZHANKOI THE TRISKELLION US KHARTOUM"
  }
}
