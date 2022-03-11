package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match.Flag
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty

import spock.lang.Specification

class SuspectsCollectionSpec extends Specification {

  def context = AlertContext.builder()
      .lastDecBatchId('lastBatchId')
      .typeOfRec('typeOfRec')
      .build()

  def 'should makeMatches'() {
    given:
    def suspect = createSuspect(1)
    def objectUnderTest = new SuspectsCollection([suspect])

    when:
    def result = objectUnderTest.makeMatches(context)

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
    def objectUnderTest = new SuspectsCollection([suspect2, suspect1, suspect3])

    when:
    def result = objectUnderTest.makeMatches(context)

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
    def objectUnderTest = new SuspectsCollection([createSuspect(null), createSuspect(1)])

    when:
    objectUnderTest.makeMatches(context)

    then:
    thrown(IllegalStateException.class)
  }

  def verifyMatchFlags(int flags) {
    flags == Flag.NONE.value
  }

  def verifyMatchedParty(MatchedParty party) {
    party.id
  }

  def verifyMatchId(ObjectId objectId, Suspect suspect) {
    objectId.id
    objectId.sourceId == suspect.ofacId
    objectId.discriminator == suspect.batchId
  }

  def createSuspect(Integer index) {
    new Suspect(
        ofacId: 'ofacId' + index,
        nationalId: 'nationalId' + index,
        name: 'name' + index,
        batchId: 'batchId' + index,
        index: index
    )
  }
}
