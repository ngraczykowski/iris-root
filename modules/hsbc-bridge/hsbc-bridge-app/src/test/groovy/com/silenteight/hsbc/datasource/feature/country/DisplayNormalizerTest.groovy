package com.silenteight.hsbc.datasource.feature.country

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class DisplayNormalizerTest extends Specification {

  @Subject
  def displayNormalizer = new DisplayNormalizer()

  @Unroll
  def "Should normalize #country for displaying into #expected "() {
    when:
    def normalized = displayNormalizer.normalize(country)

    then:
    normalized == expected

    where:
    country                                   || expected
    "the United States"                       || "THE UNITED STATES"
    "the United States of America"            || "THE UNITED STATES OF AMERICA"
    "  the United States of #@!^% America"    || "THE UNITED STATES OF AMERICA"
    "The , United (States) of-America."       || "THE , UNITED (STATES) OF-AMERICA."
    "XX"                                      || ""
    "ZZ"                                      || ""
    "LN"                                      || ""
    "UNK"                                     || ""
    "UNKNOWN"                                 || ""
    "UNSPECIFIED"                             || ""
    "NULL"                                    || ""
    "DEFAULT"                                 || ""
    "default xx the United states of america" || "THE UNITED STATES OF AMERICA"
  }
}
