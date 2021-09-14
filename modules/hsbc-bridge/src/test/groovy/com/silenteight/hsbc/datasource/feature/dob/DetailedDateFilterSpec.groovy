package com.silenteight.hsbc.datasource.feature.dob

import spock.lang.Specification
import spock.lang.Unroll

class DetailedDateFilterSpec extends Specification {

  @Unroll
  def "filter detailed date correctly"() {
    when:
    def underTest = new DetailedDateFilter(dates).test(date)

    then:
    underTest == expected

    where:
    dates                               || date          || expected
    ["2021-02-03", "2021"]              || "2021-02-03"  || true
    ["2021-02-03", "2021-02"]           || "2021-02-03"  || true
    ["2021-02-03", "202102", "2021.02"] || "2021-02-03"  || true
    ["2021-Jan-01", "2021-01"]          || "2021-Jan-01" || true
    ["2021-dec-01", "2021-12"]          || "2021-dec-01" || true
    ["2021-01", "2021"]                 || "2021-01"     || true
    ["2021-02-03", "2022"]              || "2022"        || false
    ["2021-02-03", "2022"]              || "2021-02-03"  || false
    ["2021-dec-01", "2021-05"]          || "2021-05"     || false
    ["2021-dec-01", "2021-05"]          || "2021-dec-01" || false
    ["2022-01", "2021"]                 || "2021"        || false
    ["2022-01", "2021"]                 || "2022-01"     || false
  }
}
