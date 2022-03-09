package com.silenteight.scb.ingest.adapter.incomming.cbs.domain

import spock.lang.Specification
import spock.lang.Unroll

class NeoFlagSpec extends Specification {

  @Unroll
  def "should parse neoFlag into #expectedResult for input: #inputString"() {
    when:
    def result = NeoFlag.parse(inputString)

    then:
    result == expectedResult

    where:
    inputString | expectedResult
    ''          | Optional.empty()
    'O'         | Optional.of(NeoFlag.OBSOLETE)
    'N'         | Optional.of(NeoFlag.NEW)
    'E'         | Optional.of(NeoFlag.EXISTING)
  }
}
