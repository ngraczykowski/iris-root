/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.validation

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
