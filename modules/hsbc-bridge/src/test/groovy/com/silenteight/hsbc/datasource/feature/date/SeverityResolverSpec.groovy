package com.silenteight.hsbc.datasource.feature.date

import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.NORMAL
import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.STRICT

class SeverityResolverSpec extends Specification {

  @Unroll
  def 'should resolve severity mode and return `#expectedResult`'() {
    when:
    def result = new SeverityResolver(sourceValue, watchlistTypes).resolve()

    then:
    result == expectedResult

    where:
    sourceValue                  || expectedResult
    getMatchData('AE-MEWOLF')    || STRICT
    getMatchData('MENA-GREY')    || STRICT
    getMatchData('MEWOLF')       || STRICT
    getMatchData('MX-DARK-GREY') || STRICT
    getMatchData('SAN')          || STRICT
    getMatchData('US-HBUS')      || STRICT

    getMatchData('AML')          || NORMAL
    getMatchData('CTF-P2')       || NORMAL
    getMatchData('INNIA')        || NORMAL
    getMatchData('MX-AML')       || NORMAL
    getMatchData('MX-SHCP')      || NORMAL

    getMatchData('PEP')          || NORMAL
    getMatchData('SCION')        || NORMAL
    getMatchData('SSC')          || NORMAL

    getMatchData('')             || NORMAL
    getMatchData('     ')        || NORMAL
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

