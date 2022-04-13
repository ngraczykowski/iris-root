package com.silenteight.fab.dataprep.domain.feature

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class AdditionalInfoHelperTest extends Specification {

  def 'field should be returned #additionalInfo'() {
    when:
    log.info("additionalInfo: $additionalInfo")
    def result = AdditionalInfoHelper.getValue(additionalInfo, fieldName)

    then:
    result == expectedValue

    where:
    additionalInfo                                 | fieldName      | expectedValue
    'Nationality: INDIA'                           | 'Nationality'  | 'INDIA'
    ' / Nationality: INDIA / Gender: MALE'         | 'Nationality'  | 'INDIA'
    ' / Nationality: UNITED STATES / Gender: MALE' | 'Nationality'  | 'UNITED STATES'
    ' / Nationality: INDIA / Gender: MALE '        | 'Gender'       | 'MALE'
    ' / Nationality: INDIA / Gender: MALE'         | 'UnknownField' | ''
    'List ID: 1054 / ID: 100 / Gender: MALE'       | 'ID'           | '100'
    'ID: 100 / List ID: 1054 / Gender: MALE'       | 'ID'           | '100'
    'List ID: 1054/ID: 100/Gender: MALE'           | 'ID'           | '100'
  }
}
