package com.silenteight.hsbc.datasource.category

import spock.lang.Specification
import spock.lang.Unroll

class CategoryModelHolderSpec extends Specification {

  def fixtures = new Fixtures()
  def underTest = new CategoryModelHolder(fixtures.categoryProperties)

  @Unroll
  def 'should map source risk type value and return `#expectedResult` when sourceValue=`#sourceValue`'() {
    when:
    def result = underTest.mapSourceRiskTypeValue(sourceValue)

    then:
    result == expectedResult

    where:
    sourceValue    || expectedResult
    'AE-MEWOLF'    || 'SAN'
    'MENA-GREY'    || 'SAN'
    'MEWOLF'       || 'SAN'
    'MX-DARK-GREY' || 'SAN'
    'SAN'          || 'SAN'
    'US-HBUS'      || 'SAN'

    'AML'          || 'AML'
    'CTF-P2'       || 'AML'
    'INNIA'        || 'AML'
    'MX-AML'       || 'AML'
    'MX-SHCP'      || 'AML'

    'PEP'          || 'PEP'
    'SCION'        || 'EXITS'
    'SSC'          || 'SSC'

    ''             || 'OTHER'
    'DUMMY'        || 'OTHER'
    null           || 'OTHER'
  }

  class Fixtures {

    def categoryProperties =
        [
            'SAN'  : [
                'AE-MEWOLF',
                'MENA-GREY',
                'MEWOLF',
                'MX-DARK-GREY',
                'SAN',
                'US-HBUS',
            ] as List,
            'AML'  : [
                'AML',
                'CTF-P2',
                'INNIA',
                'MX-AML',
                'MX-SHCP',
            ] as List,
            'PEP'  : [
                'PEP'
            ] as List,
            'EXITS': [
                'SCION'
            ] as List,
            'SSC'  : [
                'SSC'
            ]
        ] as Map
  }
}
