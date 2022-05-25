package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData

import spock.lang.Specification
import spock.lang.Unroll

class ChineseNamesResolverSpec extends Specification {

  @Unroll
  def "should get valid chinese names from ScreenableData"() {
    when:
    def result = ChineseNamesResolver.getChineseNames(screenableData)

    then:
    result == expectedResult as Set

    where:
    screenableData              | expectedResult
    new ScreenableData(
        supplementaryInformation1: null,
        alternateName1: null)   | []
    new ScreenableData(
        supplementaryInformation1: 'Tom',
        alternateName1: 'Li')   | []
    new ScreenableData(
        supplementaryInformation1: '黃波Li',
        alternateName1: '李Xyz') | []
    new ScreenableData(
        supplementaryInformation1: '黃波',
        alternateName1: '')     | ['黃波']
    new ScreenableData(
        supplementaryInformation1: '',
        alternateName1: '李')    | ['李']
    new ScreenableData(
        supplementaryInformation1: '黃波',
        alternateName1: '李')    | ['黃波', '李']
  }

  @Unroll
  def "should get valid chinese names from GNSParty"() {
    when:
    def result = ChineseNamesResolver.getChineseNames(gnsParty)

    then:
    result == expectedResult as Set

    where:
    gnsParty                       | expectedResult
    GnsParty.empty()               | []
    createGnsParty('Tom', 'Li')    | []
    createGnsParty('黃波Li', '李Xyz') | []
    createGnsParty('黃波', '')       | ['黃波']
    createGnsParty('', '李')        | ['李']
    createGnsParty('黃波', '李')      | ['黃波', '李']
  }

  private static GnsParty createGnsParty(String supplementaryInformation1, String alternateName1) {
    def gnsParty = GnsParty.create("someSourceSystem", "someCustomerNo")
    gnsParty.add("supplementaryInformation1", supplementaryInformation1)
    gnsParty.add("alternateName1", alternateName1)
    return gnsParty
  }
}
