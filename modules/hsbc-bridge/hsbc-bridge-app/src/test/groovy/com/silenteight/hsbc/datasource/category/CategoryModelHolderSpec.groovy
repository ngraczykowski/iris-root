package com.silenteight.hsbc.datasource.category

import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningEntities
import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningIndividuals

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
    'NNS'          || 'NNS'

    ''             || 'OTHER'
    'DUMMY'        || 'OTHER'
    null           || 'OTHER'
  }

  @Unroll
  def 'should return #expected value for #desc'() {
    when:
    def result = underTest.isTerrorRelated(List.of(nnsEntities), List.of(nnsIndividuals))

    then:
    result == expected

    where:
    desc                                                      || expected || nnsEntities                     || nnsIndividuals
    "NNS Entities with terror related"                        || "YES"    || nnsEntitiesWithTerrorRelated    || nnsIndividualsWithoutTerrorRelated
    "NNS Individuals with terror related"                     || "YES"    || nnsEntitiesWithoutTerrorRelated || nnsIndividualsWithTerrorRelated
    "NNS Individuals and NNS Entities without terror related" || "NO"     || nnsEntitiesWithoutTerrorRelated || nnsIndividualsWithoutTerrorRelated
    "NNS Individuals and NNS Entities with terror related"    || "YES"    || nnsEntitiesWithTerrorRelated    || nnsIndividualsWithTerrorRelated
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
            ],
            'NNS' : [
                'NNS'
            ]
        ] as Map
  }

  static def nnsEntitiesWithTerrorRelated = new NegativeNewsScreeningEntities(sicCodeGlobalKeyword: List.of("Terror Related"), sicCodeLocalKeyword: List.of("Terror Related"))
  static def nnsEntitiesWithoutTerrorRelated = new NegativeNewsScreeningEntities(sicCodeGlobalKeyword: List.of("Bribery and Corruption", "Environmental Crime"), sicCodeLocalKeyword: List.of(""))

  static def nnsIndividualsWithTerrorRelated = new NegativeNewsScreeningIndividuals(sicCodeGlobalKeyword: List.of("Human Rights Violation", "Terror Related"), sicCodeLocalKeyword: List.of("Terror Related"))
  static def nnsIndividualsWithoutTerrorRelated = new NegativeNewsScreeningIndividuals(sicCodeGlobalKeyword: List.of("Human Rights Violation"), sicCodeLocalKeyword: List.of("Money Laundering"))
}
