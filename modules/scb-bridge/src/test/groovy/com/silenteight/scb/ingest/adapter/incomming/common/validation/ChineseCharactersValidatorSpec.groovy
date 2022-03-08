package com.silenteight.scb.ingest.adapter.incomming.common.validation

import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator

import spock.lang.Specification
import spock.lang.Unroll

class ChineseCharactersValidatorSpec extends Specification {

  @Unroll
  def 'given `#input` should return valid=false'() {
    when:
    def result = ChineseCharactersValidator.isValid(input)

    then:
    !result

    where:
    input << [
        null,
        '',
        '1',
        'A',
        '$',
        'Test',
        '黃a',
        'a黃'
    ]
  }

  @Unroll
  def 'given `#input` should return valid=true'() {
    when:
    def result = ChineseCharactersValidator.isValid(input)

    then:
    result

    where:
    input << [
        '金红',
        '黃波'
    ]
  }
}
