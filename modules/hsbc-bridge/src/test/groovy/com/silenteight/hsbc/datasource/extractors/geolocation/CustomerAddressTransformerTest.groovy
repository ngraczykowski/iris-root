package com.silenteight.hsbc.datasource.extractors.geolocation

import spock.lang.Specification
import spock.lang.Unroll

class CustomerAddressTransformerTest extends Specification {

  @Unroll
  def "Should return valid addresses"() {
    given:

    when:
    def transformed = CustomerAddressTransformer.builder()
        .addresses(addresses)
        .names(names)
        .countries(countries)
        .build()
        .getAddressWithoutNamesAndWithCountries()

    then:
    transformed == expected

    where:
    addresses                                                                            | names                           | countries                    || expected
    ["Sherlock Holmes 221B Baker St., London."]                                          | [""]                            | [""]                         || "SHERLOCK HOLMES 221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London."]                                          | ["Sherlock Holmes"]             | [""]                         || "221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London."]                                          | [""]                            | ["United Kingdom"]           || "SHERLOCK HOLMES 221B BAKER ST., LONDON. UNITED KINGDOM"
    ["Sherlock Holmes 221B Baker St., London."]                                          | ["Sherlock Holmes"]             | ["United Kingdom"]           || "221B BAKER ST., LONDON. UNITED KINGDOM"
    ["Sherlock Holmes 221B Baker St., London."]                                          | ["John Doe"]                    | [""]                         || "SHERLOCK HOLMES 221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London."]                                          | ["John Doe"]                    | ["Poland"]                   || "SHERLOCK HOLMES 221B BAKER ST., LONDON. POLAND"
    ["Sherlock Holmes 221B Baker St., London."]                                          | ["John Doe", "Sherlock Holmes"] | ["Poland"]                   || "221B BAKER ST., LONDON. POLAND"

    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | [""]                            | [""]                         || "SHERLOCK HOLMES 221B BAKER ST., LONDON. SHERLOCK HOLM 221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["Sherlock Holmes"]             | [""]                         || "221B BAKER ST., LONDON. SHERLOCK HOLM 221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | [""]                            | ["United Kingdom"]           || "SHERLOCK HOLMES 221B BAKER ST., LONDON. UNITED KINGDOM SHERLOCK HOLM 221B BAKER ST., LONDON. UNITED KINGDOM"
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["Sherlock Holmes"]             | ["United Kingdom"]           || "221B BAKER ST., LONDON. UNITED KINGDOM SHERLOCK HOLM 221B BAKER ST., LONDON. UNITED KINGDOM"
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["John Doe"]                    | [""]                         || "SHERLOCK HOLMES 221B BAKER ST., LONDON. SHERLOCK HOLM 221B BAKER ST., LONDON."
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["John Doe"]                    | ["Poland"]                   || "SHERLOCK HOLMES 221B BAKER ST., LONDON. POLAND SHERLOCK HOLM 221B BAKER ST., LONDON. POLAND"

    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | [""]                            | ["United Kingdom", "Poland"] || "SHERLOCK HOLMES 221B BAKER ST., LONDON. UNITED KINGDOM POLAND SHERLOCK HOLM 221B BAKER ST., LONDON. UNITED KINGDOM POLAND"
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["Sherlock Holmes"]             | ["United Kingdom", "Poland"] || "221B BAKER ST., LONDON. UNITED KINGDOM POLAND SHERLOCK HOLM 221B BAKER ST., LONDON. UNITED KINGDOM POLAND"
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["John Doe"]                    | ["Poland", "United Kingdom"] || "SHERLOCK HOLMES 221B BAKER ST., LONDON. POLAND UNITED KINGDOM SHERLOCK HOLM 221B BAKER ST., LONDON. POLAND UNITED KINGDOM"
    ["Sherlock Holmes 221B Baker St., London.", "Sherlock Holm 221B Baker St., London."] | ["John Doe", "Sherlock Holmes"] | ["Poland", "United Kingdom"] || "221B BAKER ST., LONDON. POLAND UNITED KINGDOM SHERLOCK HOLM 221B BAKER ST., LONDON. POLAND UNITED KINGDOM"
  }
}
