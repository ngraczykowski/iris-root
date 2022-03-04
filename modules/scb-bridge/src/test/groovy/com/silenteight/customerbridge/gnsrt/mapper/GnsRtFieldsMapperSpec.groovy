package com.silenteight.customerbridge.gnsrt.mapper

import com.silenteight.customerbridge.gnsrt.model.request.ScreenableData

import spock.lang.Specification
import spock.lang.Unroll

class GnsRtFieldsMapperSpec extends Specification {

  def "should get alternate names"() {
    when:
    def result = GnsRtFieldsMapper.getAlternateNames(screenableData)

    then:
    result == expectedResult

    where:
    screenableData                                              | expectedResult
    new ScreenableData()                                        | []
    new ScreenableData(alternateName1: '', alternateName2: '')  | []
    new ScreenableData(alternateName1: 'a', alternateName2: '') | ['a']
    new ScreenableData(
        alternateName1: 'a',
        alternateName2: 'b',
        alternateName3: 'c',
        alternateNameRest: 'd')                                 | ['a', 'b', 'c', 'd']
  }

  @Unroll
  def "should get original chinese names, screenableData=#screenableData"() {
    when:
    def result = GnsRtFieldsMapper.getOriginalChineseNames(screenableData)

    then:
    result == expectedResult as Set

    where:
    screenableData                        | expectedResult
    new ScreenableData(
        sourceSystemIdentifier: 'CUPD',
        amlCountry: 'CN',
        supplementaryInformation1: 'Tom') | []
    new ScreenableData(
        sourceSystemIdentifier: '',
        amlCountry: '',
        supplementaryInformation1: '黃波')  | []
    new ScreenableData(
        sourceSystemIdentifier: 'CUPD',
        amlCountry: 'CN',
        supplementaryInformation1: '黃波')  | ['黃波']
    new ScreenableData(
        sourceSystemIdentifier: 'TNDM',
        amlCountry: 'TW',
        supplementaryInformation1: '黃波')  | ['黃波']
  }

  def "should get residential addresses"() {
    when:
    def result = GnsRtFieldsMapper.getResidentialAddresses(screenableData)

    then:
    result == expectedResult

    where:
    screenableData            | expectedResult
    new ScreenableData()      | []
    new ScreenableData(
        registeredOrResidentialAddress: 'a',
        mailingOrCommunicationAddress: 'b',
        operatingOrOfficialAddress: 'c',
        otherAddress: 'd',
        registeredAddressOfHeadOffice: 'e',
        registeredAddressOfParentCompany: 'f',
        nameOfAuthority: 'g') | ['a', 'b', 'c', 'd', 'e', 'f', 'g']
  }
}
