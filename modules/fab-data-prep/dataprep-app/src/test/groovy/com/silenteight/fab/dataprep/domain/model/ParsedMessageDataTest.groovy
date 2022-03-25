package com.silenteight.fab.dataprep.domain.model

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData.CustomerType

import spock.lang.Specification
import spock.lang.Unroll

class ParsedMessageDataTest extends Specification {

  @Unroll
  def 'type should be extracted correctly: #expected'() {
    given:
    ParsedMessageData payload = ParsedMessageData.builder()
        .customerType(type)
        .build()

    when:
    def result = payload.getCustomerTypeAsEnum()

    then:
    result == expected

    where:
    type         | expected
    'Individual' | CustomerType.INDIVIDUAL
    'individual' | CustomerType.INDIVIDUAL
    'Corporate'  | CustomerType.CORPORATE
    'corporate'  | CustomerType.CORPORATE
    'unknown'    | CustomerType.DATA_SOURCE_ERROR
    ''           | CustomerType.NO_DATA
  }
}
