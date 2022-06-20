/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag.EXISTING
import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag.NEW

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

  def cbsHitDetails(int seqNo, com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag neoFlag) {
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails.builder()
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
    '金红'  | []                         | [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金红', com
        .silenteight
        .iris
        .bridge
        .scb
        .ingest
        .adapter
        .incomming
        .common
        .WlNameType.NAME)]
    '金金红' | [new Synonym('金红', true)] |
        [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金金红', com
            .silenteight
            .iris
            .bridge
            .scb
            .ingest
            .adapter
            .incomming
            .common
            .WlNameType.NAME), new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金红', com
            .silenteight
            .iris
            .bridge
            .scb
            .ingest
            .adapter
            .incomming
            .common
            .WlNameType.ALIAS)]
    '金金红' | [new Synonym('金红', false)] |
        [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金金红', com
            .silenteight
            .iris
            .bridge
            .scb
            .ingest
            .adapter
            .incomming
            .common
            .WlNameType.NAME), new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金红', com
            .silenteight
            .iris
            .bridge
            .scb
            .ingest
            .adapter
            .incomming
            .common
            .WlNameType.ALIAS)]
    'Jan' | [new Synonym('金红', false)] | [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金红', com
        .silenteight
        .iris
        .bridge
        .scb
        .ingest
        .adapter
        .incomming
        .common
        .WlNameType.ALIAS)]
    'Jan' | [new Synonym('金红', true)]  | [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName('金红', com
        .silenteight
        .iris
        .bridge
        .scb
        .ingest
        .adapter
        .incomming
        .common
        .WlNameType.ALIAS)]
  }
}
