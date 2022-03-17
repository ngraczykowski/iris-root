package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.State
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution

import io.micrometer.core.instrument.Tags
import spock.lang.Shared
import spock.lang.Specification

class RecordToAlertMapperSpec extends Specification {

  @Shared
  def someOfacId = '123'
  @Shared
  def EMPTY_VALUE = ''
  @Shared
  def someSuspect = createSuspect()
  @Shared
  def batchId = '2019/09/20_0001_EN_BTCH_ALLOW'

  def 'should throw exception when watchlistLevel processing and incorrect no of matches'() {
    given:
    def objectUnderTest = createRecordToAlertMapperBuilder()
        .watchlistLevel(true)
        .build()
    def suspectCollection = createSuspectCollection(suspects)

    when:
    objectUnderTest.toAlert(suspectCollection)

    then:
    thrown(IllegalStateException)

    where:
    suspects << [[], [someSuspect, someSuspect]]
  }

  def "should watchListId be set correctly"() {
    given:
    def objectUnderTest = createRecordToAlertMapperBuilder()
        .watchlistLevel(watchlistLevel)
        .build()
    def suspectCollection = createSuspectCollection(suspects)

    when:
    def result = objectUnderTest.toAlert(suspectCollection)

    then:
    result.details.watchlistId == expectedResult

    where:
    watchlistLevel | suspects                   | expectedResult
    false          | []                         | EMPTY_VALUE
    false          | [someSuspect]              | EMPTY_VALUE
    false          | [someSuspect, someSuspect] | EMPTY_VALUE
    true           | [someSuspect]              | someOfacId
  }

  def "should bookingLocation be set correctly"() {
    given:
    def objectUnderTest = createRecordToAlertMapperBuilder()
        .watchlistLevel(false)
        .build()
    def suspectCollection = createSuspectCollection([someSuspect])

    when:
    def result = objectUnderTest.toAlert(suspectCollection)

    then:
    def details = result.alertedParty

    and:
    details.apBookingLocation == 'bl'
  }

  def 'should build suspect context'() {
    given:
    def decisions = new DecisionsCollection([Decision.builder().build()])
    def alertData = createAlertRecordBuilder()
        .lastDecBatchId('lastBatchId')
        .typeOfRec('I')
        .build()
    def alertMapper = createRecordToAlertMapperBuilder()
        .alertData(alertData)
        .decisionsCollection(decisions)
        .build()

    when:
    def result = alertMapper.buildAlertContext()

    then:
    result.lastDecBatchId == alertData.lastDecBatchId
    result.typeOfRec == alertData.typeOfRec
    result.lastDecisionPresent == decisions.hasLastDecision()
  }

  def 'should map to alert and mark alert as damaged when no suspects'() {
    given:
    def suspects = new SuspectsCollection([])
    def objectUnderTest = RecordToAlertMapper.builder()
        .alertData(createAlertRecord())
        .recordSignature(EMPTY_VALUE)
        .alertedParty(GnsParty.create('sourceSystemId', 'customerId'))
        .decisionsCollection(new DecisionsCollection([]))
        .build()

    when:
    def result = objectUnderTest.toAlert(suspects)

    then:
    assertDamagedAlert(result)
  }

  def 'should map to alert and mark alert as damaged when empty alerted party'() {
    given:
    def suspects = new SuspectsCollection([someSuspect])
    def objectUnderTest = RecordToAlertMapper.builder()
        .alertData(createAlertRecord())
        .recordSignature(EMPTY_VALUE)
        .alertedParty(GnsParty.empty())
        .decisionsCollection(new DecisionsCollection([]))
        .build()

    when:
    def result = objectUnderTest.toAlert(suspects)

    then:
    assertDamagedAlert(result)
  }

  def assertDamagedAlert(Alert result) {
    result.state == State.STATE_DAMAGED
    result.decisionGroup == 'EN_BTCH_ALLOW'
    result.securityGroup == 'EN'
    result.decisions.isEmpty()
    result.flags == Alert.Flag.NONE.value
    result.generatedAt
    result.receivedAt
    result.alertedParty
    result.details
    result.id
  }

  def 'should get measuring tags'() {
    given:
    def alertRecord = AlertRecord.builder()
        .unit('EN_BTCH_ALLOW')
        .systemId('EN_BTCH_ALLOW!fe660195-bc51-4813-8bc1-24777182f7cf')
        .batchId(batchId)
        .build()
    def objectUnderTest = RecordToAlertMapper.builder()
        .recordSignature('')
        .alertData(alertRecord)
        .decisionsCollection(new DecisionsCollection([decision]))
        .alertedParty(new GnsParty(sourceSystemIdentifier: 'systemId'))
        .build()

    when:
    def results = objectUnderTest.getMeasuringTags()

    then:
    results == expectedTags

    where:
    decision                  | expectedTags
    createDecision('Analyst') | Tags.of(
        "unit", "EN_BTCH_ALLOW", "country", "EN",
        "solution", "FALSE_POSITIVE", "ssi", "systemId")
    createDecision('FSK')     | Tags.of(
        "unit", "EN_BTCH_ALLOW", "country", "EN",
        "solution", "NO_SOLUTION", "ssi", "systemId")
  }

  def 'one suspect should be mapped to at least one match'() {
    given:
    def suspects = createSuspectCollection([someSuspect])
    def alertRecord = AlertRecord.builder()
        .systemId('EN_BTCH_ALLOW!test')
        .batchId('2019/09/20_0001_EN_BTCH_ALLOW')
        .unit('EN_BTCH_ALLOW')
        .build()
    def objectUnderTest = RecordToAlertMapper.builder()
        .alertData(alertRecord)
        .recordSignature(EMPTY_VALUE)
        .alertedParty(GnsParty.empty())
        .decisionsCollection(new DecisionsCollection([]))
        .build()

    when:
    def result = objectUnderTest.toAlert(suspects)

    then:
    result.matchesCount > 0
  }

  def createSuspectCollection(suspects) {
    new SuspectsCollection(suspects)
  }

  def createSuspect() {
    new Suspect(ofacId: someOfacId, batchId: EMPTY_VALUE, index: 1)
  }

  def createDecision(String authorId) {
    Decision.builder()
        .authorId(authorId)
        .solution(AnalystSolution.ANALYST_FALSE_POSITIVE)
        .build()
  }

  def createAlertData() {
    AlertRecord.builder()
        .systemId('EN_BTCH_ALLOW!test')
        .batchId(batchId)
        .build()
  }

  def createAlertRecordBuilder() {
    AlertRecord.builder()
        .systemId(EMPTY_VALUE)
        .batchId(EMPTY_VALUE)
  }

  def createRecordToAlertMapperBuilder() {
    def gnsParty = GnsParty.create("ssid", "cin")
    gnsParty.fields.put("bookingLocation", "bl")

    RecordToAlertMapper.builder()
        .alertData(createAlertData())
        .recordSignature(EMPTY_VALUE)
        .alertedParty(gnsParty)
        .decisionsCollection(new DecisionsCollection([]))
  }

  def createAlertRecord() {
    AlertRecord.builder()
        .systemId('EN_BTCH_ALLOW!test')
        .batchId(batchId)
        .unit('EN_BTCH_ALLOW')
        .build()
  }
}
