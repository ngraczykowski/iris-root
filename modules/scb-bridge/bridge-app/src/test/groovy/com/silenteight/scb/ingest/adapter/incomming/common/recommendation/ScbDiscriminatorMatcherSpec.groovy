package com.silenteight.scb.ingest.adapter.incomming.common.recommendation

import spock.lang.Specification
import spock.lang.Unroll

class ScbDiscriminatorMatcherSpec extends Specification {

  def underTest = new ScbDiscriminatorMatcher()

  @Unroll
  def 'should return: #expectedResult when matching `#value1` with `#value2`'() {
    expect:
    underTest.match(value1, value2) == expectedResult

    where:
    value1                     | value2                     | expectedResult
    ''                         | ''                         | true
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:20:44Z'     | true
    'a'                        | 'b'                        | false
    '2019-01-28T09:20:44Z'     | ''                         | false
    ''                         | '2019-01-28T09:20:44Z'     | false
    '2019-01-28T09:21:44Z'     | '2019-01-28T09:20:44Z'     | false
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:21:44Z'     | false
    '2019-01-28T09:20:43Z'     | '2019-01-28T09:20:44Z'     | true
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:20:43Z'     | true
    '2019-01-28T09:21:43Z'     | '2019-01-28T09:20:44Z'     | true
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:21:43Z'     | true
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:21:43.510Z' | true
    '2019-01-28T09:20:44Z'     | '2019-01-28T09:21:44.510Z' | false
    '2019-01-28T09:20:44.510Z' | '2019-01-28T09:21:44Z'     | true
    '2022-03-04T08:45:02.584Z' | '2022-03-04T08:46:02.100Z' | true
    '2022-03-04T08:45:02.584Z' | '2022-03-04T08:46:02.600Z' | false
  }
}
