package com.silenteight.hsbc.datasource.feature.country

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CountryDiscovererTest extends Specification {

  @Subject
  def countryDiscoverer = new CountryDiscoverer()


  @Unroll
  def "Should return the following countries: expectedValues when 'country' is given"() {
    when:
    def discoveredCountries = countryDiscoverer.discover(countries)

    then:
    discoveredCountries.containsAll(expectedValues)

    where:
    countries                                  || expectedValues
    [["PL"]]                                   || [["PL"]]
    [["PL"], ["PL"]]                           || [["PL"], ["PL"]]
    [["PL US"], ["PL US"]]                     || [["PL", "US"], ["PL", "US"]]
    [["Poland; Ukraine"]]                      || [["POLAND", "UKRAINE"]]
    [["Antigua and Barbuda; POLAND"]]          || [["ANTIGUA AND BARBUDA", "POLAND"]]
    [["PL, CZ"]]                               || [["PL", "CZ"]]
    [["Bonaire, Sint Eustatius and Saba; PL"]] || [["BONAIRE, SINT EUSTATIUS AND SABA", "PL"]]
    [["PL", "PL GB"]]                          || [["PL", "GB"]]
    [[null, "", "PL"]]                         || [["PL"]]
    [['"PL","GB"']]                            || [["PL", "GB"]]
    [["the United States of America; PL"]]     || [["THE UNITED STATES OF AMERICA", "PL"]]
    [["United States of America; PL"]]         || [["UNITED STATES OF AMERICA", "PL"]]
    [["UNITED STATES OF AMERICA (the); PL"]]   || [["UNITED STATES OF AMERICA (THE)", "PL"]]
    [['"HK","TH"', '"TH"']]                    || [["HK", "TH"]]
    [["PK PK BD PK PK PK"]]                    || [["PK", "BD"]]
    [["KOREA, REPUBLIC OF; POLAND"]]           || [["KOREA, REPUBLIC OF", "POLAND"]]

    [[null]]                                   || [[]]
    [[""]]                                     || [[]]
    [["UNK"]]                                  || [[]]
    [["AAAAPLAAAA"]]                           || [["AAAAPLAAAA"]]
    [["APOLA"]]                                || [["APOLA"]]
    [["Unknown"]]                              || [[]]
    [["Unknown Country; PL"]]                  || [["Unknown Country; PL"]]
    [["Unknown", "PL"]]                        || [["PL"]]
    [[null, "", "PL", "Unknown"]]              || [["PL"]]
    [[null, "", "PL", "APLA"]]                 || [["PL", "APLA"]]
    [[null, "", "PL", "Unknown; PL"]]          || [["PL"]]
    [['"XX","PL"']]                            || [["PL"]]
    [['"ZZ","PL"']]                            || [["PL"]]
    [['"ZZ","PL"'], ['"XX","PL"']]             || [["PL"], ["PL"]]
    [['"XX"']]                                 || [[]]
  }
}
