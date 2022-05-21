package com.silenteight.hsbc.datasource.feature.country

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class MatchNormalizerTest extends Specification {

  @Subject
  def matchNormalizer = new MatchNormalizer()

  @Unroll
  def "Should normalize #country for matching into #expected "() {
    when:
    def normalized = matchNormalizer.normalize(country)

    then:
    normalized == expected

    where:
    country                                   || expected
    "the United States"                       || "UNITED STATES"
    "the United States of America"            || "UNITED STATES AMERICA"
    "  the United States of #@!^% America"    || "UNITED STATES AMERICA"
    "XX"                                      || ""
    "ZZ"                                      || ""
    "LN"                                      || ""
    "UNK"                                     || ""
    "UNKNOWN"                                 || ""
    "UNSPECIFIED"                             || ""
    "NULL"                                    || ""
    "DEFAULT"                                 || ""
    "default xx the United states of america" || "UNITED STATES AMERICA"
  }
}
