package com.silenteight.hsbc.datasource.extractors.name

import spock.lang.Specification
import spock.lang.Unroll

class CountryLanguageMatcherSpec extends Specification {

  @Unroll
  def 'should check matches variations'() {
    when:
    def isMatching = CountryLanguageMatcher.matches(languages, countries)

    then:
    isMatching == expected

    where:
    languages | countries    | expected
    null      | null         | false
    null      | []           | false
    [null]    | [null]       | false
    [null]    | []           | false
    []        | []           | false
    ['zh-CN'] | []           | false
    []        | ['HK']       | false
    ['zh-CN'] | ['RU']       | false
    ['zh-CN'] | ['CN']       | true
    ['zh-CN'] | ['CN', 'RU'] | true
    ['zh-CN'] | ['china']    | true
  }
}
