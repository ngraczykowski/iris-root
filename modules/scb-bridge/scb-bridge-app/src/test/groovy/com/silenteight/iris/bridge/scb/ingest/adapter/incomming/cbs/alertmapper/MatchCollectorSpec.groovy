/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match.Flag
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId

import spock.lang.Specification

class MatchCollectorSpec extends Specification {

  def objectUnderTest = new MatchCollector()
  def context = AlertContext.builder()
      .lastDecBatchId('lastBatchId')
      .typeOfRec('typeOfRec')
      .build()

  def 'should makeMatches'() {
    given:
    def suspect = createSuspect(1)

    when:
    def result = objectUnderTest.collectMatches([suspect], context)

    then:
    result.size() == 1
    def match = result.first()
    verifyMatchId(match.id, suspect)
    verifyMatchedParty(match.matchedParty)
    verifyMatchFlags(match.flags)
    match.index == 0
  }

  def 'should makeMatches return matches sorted correctly with zero based index'() {
    given:
    def suspect2 = createSuspect(2)
    def suspect3 = createSuspect(4)
    def suspect1 = createSuspect(1)
    def suspects = [
        suspect2, suspect1, suspect3
    ]

    when:
    def result = objectUnderTest.collectMatches(suspects, context)

    then:
    result.size() == 3
    verifyMatchId(result.get(0).id, suspect1)
    verifyMatchId(result.get(1).id, suspect2)
    verifyMatchId(result.get(2).id, suspect3)
    result.get(0).index == 0
    result.get(1).index == 1
    result.get(2).index == 2
  }

  def 'should throw exception when create matches from suspects while one of them has no index'() {
    given:
    def suspects = [createSuspect(null), createSuspect(1)]

    when:
    objectUnderTest.collectMatches(suspects, context)

    then:
    thrown(IllegalStateException.class)
  }

  def verifyMatchFlags(int flags) {
    flags == Flag.NONE.value
  }

  def verifyMatchedParty(com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchedParty party) {
    party.id
  }

  def verifyMatchId(ObjectId objectId, com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect suspect) {
    objectId.id
    objectId.sourceId == suspect.ofacId
    objectId.discriminator == suspect.batchId
  }

  def createSuspect(index) {
    new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(
        ofacId: 'ofacId' + index,
        nationalId: 'nationalId' + index,
        name: 'name' + index,
        batchId: 'batchId' + index,
        index: index
    )
  }
}
