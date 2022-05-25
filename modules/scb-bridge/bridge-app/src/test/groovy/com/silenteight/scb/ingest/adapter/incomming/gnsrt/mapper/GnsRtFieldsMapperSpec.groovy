package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtFieldsMapper
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData

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
    screenableData              | expectedResult
    new ScreenableData()        | []
    new ScreenableData(
        supplementaryInformation1: '黃波Li',
        alternateName1: '李Xyz') | []
    new ScreenableData(
        supplementaryInformation1: '黃波',
        alternateName1: '')     | ['黃波']
    new ScreenableData(
        supplementaryInformation1: '黃波',
        alternateName1: '李')    | ['黃波', '李']
    new ScreenableData(
        supplementaryInformation1: '',
        alternateName1: '李',
        fullLegalName: '波波')    | ['李']
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
