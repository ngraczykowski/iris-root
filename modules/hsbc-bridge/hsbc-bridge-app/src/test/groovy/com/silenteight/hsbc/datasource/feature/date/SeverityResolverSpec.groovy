package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.dto.date.SeverityMode

import spock.lang.Specification
import spock.lang.Unroll

class SeverityResolverSpec extends Specification {

  @Unroll
  def 'should resolve severity mode and return `#expectedResult`'() {
    when:
    def result = new SeverityResolver(sourceValue, watchlistTypes).resolve()

    then:
    result == expectedResult

    where:
    sourceValue                  || expectedResult
    getMatchData('AE-MEWOLF')    || SeverityMode.STRICT
    getMatchData('MENA-GREY')    || SeverityMode.STRICT
    getMatchData('MEWOLF')       || SeverityMode.STRICT
    getMatchData('MX-DARK-GREY') || SeverityMode.STRICT
    getMatchData('SAN')          || SeverityMode.STRICT
    getMatchData('US-HBUS')      || SeverityMode.STRICT

    getMatchData('AML')          || SeverityMode.NORMAL
    getMatchData('CTF-P2')       || SeverityMode.NORMAL
    getMatchData('INNIA')        || SeverityMode.NORMAL
    getMatchData('MX-AML')       || SeverityMode.NORMAL
    getMatchData('MX-SHCP')      || SeverityMode.NORMAL

    getMatchData('PEP')          || SeverityMode.NORMAL
    getMatchData('SCION')        || SeverityMode.NORMAL
    getMatchData('SSC')          || SeverityMode.NORMAL

    getMatchData('')             || SeverityMode.NORMAL
    getMatchData('     ')        || SeverityMode.NORMAL
  }


  static def watchlistTypes =
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

  static MatchData getMatchData(String extendedAttribute5) {
    return [
        getCaseInformation: {
          [
              getExtendedAttribute5: {extendedAttribute5}
          ] as CaseInformation
        }
    ] as MatchData
  }
}
