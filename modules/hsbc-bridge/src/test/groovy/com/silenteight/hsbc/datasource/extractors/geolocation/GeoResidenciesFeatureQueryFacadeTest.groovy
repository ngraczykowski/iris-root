package com.silenteight.hsbc.datasource.extractors.geolocation

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening
import com.silenteight.hsbc.datasource.datamodel.CustomerEntity
import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.extractors.name.Party
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer

import spock.lang.Specification
import spock.lang.Unroll

class GeoResidenciesFeatureQueryFacadeTest extends Specification {

  @Unroll
  def "Should return unique, uppercase and stripped residencies alerted party from all entities"() {
    given:
    def customerEntity1 = Mock(CustomerEntity) {
      getAddress() >> firstAddress
      getProfileFullAddress() >> firstFullAddress
      getAddressCountry() >> firstCountry
    }

    def customerEntity2 = Mock(CustomerEntity) {
      getAddress() >> secondAddress
      getProfileFullAddress() >> secondsFullAddress
      getAddressCountry() >> secondCountry
    }

    def matchData = Mock(MatchData) {
      getCustomerEntities() >> [customerEntity1, customerEntity2]
    }

    def entitiesAlertedPartyNames = [firstCustomerName, secondCustomerName]

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(matchData, new CountryDiscoverer()).getApEntitiesGeoResidencies(entitiesAlertedPartyNames)

    then:
    residencies == expected

    where:
    firstAddress                | firstFullAddress    | firstCountry | firstCustomerName || secondAddress          | secondsFullAddress | secondCountry    | secondCustomerName || expected
    "Leningrad REGION"          | "Azovske Dzhankoi"  | "Poland"     | "John Doe"        || "The triskellion us"   | "Khartoum"         | "United Kingdom" | "John Doe"         || "LENINGRAD REGION POLAND AZOVSKE DZHANKOI POLAND THE TRISKELLION US UNITED KINGDOM KHARTOUM UNITED KINGDOM"
    "Leningrad REGION John Doe" | "Azovske Dzhankoi"  | "Poland"     | "John Doe"        || "Leningrad REGION"     | "Khartoum"         | "United Kingdom" | "John Doe"         || "LENINGRAD REGION POLAND AZOVSKE DZHANKOI POLAND LENINGRAD REGION UNITED KINGDOM KHARTOUM UNITED KINGDOM"
    "John Doe Leningrad REGION" | "Leningrad REGION"  | "Poland"     | "John Doe"        || "Khartoum"             | "Khartoum"         | "United Kingdom" | "John Doe"         || "LENINGRAD REGION POLAND KHARTOUM UNITED KINGDOM"
    "John Doe Leningrad REGION" | "Leningrad REGION"  | "Poland"     | "John Doe"        || "Leningrad REGION"     | "Leningrad REGION" | "Poland"         | "John Doe"         || "LENINGRAD REGION POLAND"
    "Leningrad John Doe REGION" | "LENINGRAD REGION"  | "Poland"     | "John Doe"        || "Leningrad REGION"     | "Leningrad REGION" | "United Kingdom" | "John Doe"         || "LENINGRAD REGION POLAND LENINGRAD REGION UNITED KINGDOM"
    " Leningrad REGION"         | "Azovske Dzhankoi " | "Poland"     | "John Doe"        || "  The triskellion us" | "Khartoum  "       | ""               | "John Doe"         || "LENINGRAD REGION POLAND AZOVSKE DZHANKOI POLAND THE TRISKELLION US KHARTOUM"
  }

  def "Should return valid residencies for all match party entities"() {
    given:
    def ctrpIndividual1 = Mock(CtrpScreening) {
      getCtrpValue() >> ctrpValue1
      getCountryCode() >> ctrpCountryCode1
    }

    def ctrpIndividual2 = Mock(CtrpScreening) {
      getCtrpValue() >> ctrpValue2
      getCountryCode() >> ctrpCountryCode2
    }

    def matchData = Mock(MatchData) {
      getCtrpScreeningEntities() >> [ctrpIndividual1, ctrpIndividual2]
    }

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(matchData, new CountryDiscoverer()).getMpEntitiesGeoResidencies()

    then:
    residencies == expected

    where:
    ctrpValue1   | ctrpCountryCode1 | ctrpValue2     | ctrpCountryCode2 || expected
    "CTRP value" | "RU"             | "CTRP Value 2" | "PL"             || "CTRP VALUE RU CTRP VALUE 2 PL"
  }

  def "Should return valid residencies for match party individuals"() {
    given:
    def worldCheckIndividual1 = Mock(WorldCheckIndividual) {
      getAddress() >> worldCheckAddress1
    }

    def worldCheckIndividual2 = Mock(WorldCheckIndividual) {
      getAddress() >> worldCheckAddress2
    }

    and: "world check individuals"
    def privateListIndividual1 = Mock(PrivateListIndividual) {
      getAddress() >> privateListAddress1
      getAddressCountry() >> privateListCountry1
    }

    def privateListIndividual2 = Mock(PrivateListIndividual) {
      getAddress() >> privateListAddress2
      getAddressCountry() >> privateListCountry2
    }

    and: "ctrp individuals"
    def ctrpIndividual1 = Mock(CtrpScreening) {
      getCtrpValue() >> ctrpValue1
      getCountryCode() >> ctrpCountryCode1
    }

    def ctrpIndividual2 = Mock(CtrpScreening) {
      getCtrpValue() >> ctrpValue2
      getCountryCode() >> ctrpCountryCode2
    }

    def matchData = Mock(MatchData) {
      getWorldCheckIndividuals() >> [worldCheckIndividual1, worldCheckIndividual2]
      getPrivateListIndividuals() >> [privateListIndividual1, privateListIndividual2]
      getCtrpScreeningIndividuals() >> [ctrpIndividual1, ctrpIndividual2]
    }

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(matchData, new CountryDiscoverer()).getMpIndividualsGeoResidencies()
    then:

    residencies == expected

    where:
    worldCheckAddress1 | worldCheckAddress2 | privateListAddress1 | privateListCountry1 | privateListAddress2 | privateListCountry2 | ctrpValue1 | ctrpCountryCode1 | ctrpValue2 | ctrpCountryCode2 || expected
    "LENINGRAD REGION" | "AZOVSKE DZHANKOI" | "Khartoum"          | "RUSSIA"            | "Moscow Region"     | "RUSSIA"            | "CTRP1"    | "RU"             | "CTRP2"    | "RU"             || "LENINGRAD REGION AZOVSKE DZHANKOI KHARTOUM RUSSIA MOSCOW REGION RUSSIA CTRP1 RU CTRP2"
    "LENINGRAD Region" | "AZOVSKE DZHANKOI" | "Khartoum"          | ""                  | "Moscow Region"     | "RUSSIA"            | "CTRP1"    | "RU"             | "CTRP2"    | "RU"             || "LENINGRAD REGION AZOVSKE DZHANKOI KHARTOUM MOSCOW REGION RUSSIA CTRP1 RU CTRP2"

  }

  @Unroll
  def "Should return unique, uppercase and stripped residencies with countries and without names from all individuals"() {
    given:
    def customerIndividual1 = Mock(CustomerIndividual) {
      getAddress() >> firstAddress
      getProfileFullAddress() >> firstFullAddress
      getResidenceCountries() >> firstResidenciesCountries
      getAddressCountry() >> firstAddressCountry
    }

    def customerIndividual2 = Mock(CustomerIndividual) {
      getAddress() >> secondAddress
      getProfileFullAddress() >> secondsFullAddress
      getResidenceCountries() >> secondResidenciesCountries
      getAddressCountry() >> secondAddressCountry
    }

    def matchData = Mock(MatchData) {
      getCustomerIndividuals() >> [customerIndividual1, customerIndividual2]
    }

    def party = new Party([firstCustomerName, secondCustomerName], [])

    when:
    def residencies = new GeoResidenciesFeatureQueryFacade(matchData, new CountryDiscoverer()).getApIndividualsGeoResidencies(party)

    then:
    residencies == expected

    where:
    firstAddress                | firstFullAddress    | firstResidenciesCountries | firstAddressCountry | firstCustomerName || secondAddress          | secondsFullAddress  | secondResidenciesCountries | secondAddressCountry | secondCustomerName || expected
    "LENINGRAD REGION"          | "AZOVSKE DZHANKOI"  | "POLAND"                  | "UNITED KINGDOM"    | ""                || "The triskellion us"   | "Khartoum"          | "POLAND"                   | "UNITED KINGDOM"     | "JOHN DOE"         || "LENINGRAD REGION POLAND UNITED KINGDOM AZOVSKE DZHANKOI POLAND UNITED KINGDOM THE TRISKELLION US POLAND UNITED KINGDOM KHARTOUM POLAND UNITED KINGDOM"
    "LENINGRAD REGION JOHN DOE" | "AZOVSKE DZHANKOI"  | "POLAND"                  | "UNITED KINGDOM"    | "JOHN DOE"        || "LENINGRAD REGION"     | "Khartoum John doe" | "POLAND"                   | "UNITED KINGDOM"     | "JOHN DOE"         || "LENINGRAD REGION POLAND UNITED KINGDOM AZOVSKE DZHANKOI POLAND UNITED KINGDOM LENINGRAD REGION POLAND UNITED KINGDOM KHARTOUM POLAND UNITED KINGDOM"
    "JOHN DOE LENINGRAD REGION" | "AZOVSKE DZHANKOI"  | "POLAND"                  | "POLAND"            | "JOHN DOE"        || "Khartoum"             | "Khartoum"          | "UNITED KINGDOM"           | "UNITED KINGDOM"     | "JOHN DOE"         || "LENINGRAD REGION POLAND AZOVSKE DZHANKOI POLAND KHARTOUM UNITED KINGDOM"
    "LENINGRAD JOHN DOE REGION" | "LENINGRAD REGION"  | "POLAND"                  | "POLAND"            | "JOHN DOE"        || "LENINGRAD REGION"     | "LENINGRAD REGION"  | "UNITED KINGDOM"           | "UNITED KINGDOM"     | "JOHN DOE"         || "LENINGRAD REGION POLAND LENINGRAD REGION UNITED KINGDOM"
    " LENINGRAD REGION"         | "AZOVSKE DZHANKOI " | "POLAND"                  | "UNITED KINGDOM"    | "JOHN DOE"        || "  The triskellion us" | "Khartoum  "        | "POLAND"                   | "UNITED KINGDOM"     | "JOHN DOE"         || "LENINGRAD REGION POLAND UNITED KINGDOM AZOVSKE DZHANKOI POLAND UNITED KINGDOM THE TRISKELLION US POLAND UNITED KINGDOM KHARTOUM POLAND UNITED KINGDOM"
  }
}
