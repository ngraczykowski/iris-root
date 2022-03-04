package com.silenteight.customerbridge.common.hitdetails.model

import com.silenteight.customerbridge.cbs.domain.CbsHitDetails
import com.silenteight.customerbridge.cbs.domain.NeoFlag
import com.silenteight.customerbridge.common.WlName
import com.silenteight.customerbridge.common.WlNameType

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.customerbridge.cbs.domain.NeoFlag.EXISTING
import static com.silenteight.customerbridge.cbs.domain.NeoFlag.NEW

class SuspectSpec extends Specification {

  @Unroll
  def "should load suspect with neoFlag=#expectedNeoFlag, cbsHitsDetails=#cbsHitsDetails"() {
    given:
    def suspect = new Suspect(index: 2)

    when:
    suspect.loadSuspectWithNeoFlag(cbsHitsDetails)

    then:
    suspect.neoFlag == expectedNeoFlag

    where:
    cbsHitsDetails                                      | expectedNeoFlag
    []                                                  | null
    [cbsHitDetails(1, NEW)]                             | null
    [cbsHitDetails(2, NEW)]                             | NEW
    [cbsHitDetails(1, EXISTING), cbsHitDetails(2, NEW)] | NEW
  }

  def cbsHitDetails(int seqNo, NeoFlag neoFlag) {
    CbsHitDetails.builder()
        .seqNo(seqNo)
        .hitNeoFlag(neoFlag)
        .build()
  }

  @Unroll
  def 'should get chinese names, name=#name,synonyms=#nameSynonims'() {
    given:
    def suspect = new Suspect(name: name, tag: Tag.NAME)
    suspect.getNameSynonyms().addAll(nameSynonims)

    when:
    def result = suspect.getOriginalChineseNames()

    then:
    result == expectedResult

    where:
    name  | nameSynonims               | expectedResult
    null  | []                         | []
    'Jan' | []                         | []
    '金红'  | []                         | [new WlName('金红', WlNameType.NAME)]
    '金金红' | [new Synonym('金红', true)]  |
        [new WlName('金金红', WlNameType.NAME), new WlName('金红', WlNameType.ALIAS)]
    '金金红' | [new Synonym('金红', false)] |
        [new WlName('金金红', WlNameType.NAME), new WlName('金红', WlNameType.ALIAS)]
    'Jan' | [new Synonym('金红', false)] | [new WlName('金红', WlNameType.ALIAS)]
    'Jan' | [new Synonym('金红', true)]  | [new WlName('金红', WlNameType.ALIAS)]
  }
}
