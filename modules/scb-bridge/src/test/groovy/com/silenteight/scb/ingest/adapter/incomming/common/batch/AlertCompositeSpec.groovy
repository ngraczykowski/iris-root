package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.proto.serp.v1.alert.Decision
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty

import spock.lang.Specification

class AlertCompositeSpec extends Specification {

  def "should create AlertComposite"() {
    given:
    def suspects = new SuspectsCollection([])
    def recordToAlertMapper = createRecordToAlertMapper()

    when:
    def result = AlertComposite.create(recordToAlertMapper, suspects)

    then:
    result
    result.alert
    result.decisionsCount == 1
    result.systemId == 'systemId'
    !result.measuringTags.isEmpty()
  }

  def "should create AlertComposite with given decision count"() {
    given:
    def suspects = new SuspectsCollection([])
    def recordToAlertMapper = createRecordToAlertMapper()
    def decisionCount = 13

    when:
    def result = AlertComposite.create(recordToAlertMapper, suspects, decisionCount)

    then:
    result
    result.alert
    result.decisionsCount == 13
    result.systemId == 'systemId'
    !result.measuringTags.isEmpty()
  }

  def createAlertRow() {
    AlertRecord.builder()
        .systemId('systemId')
        .batchId('batchId')
        .fmtName('fmtName')
        .unit('unit')
        .typeOfRec('I')
        .build()
  }

  def createRecordToAlertMapper() {
    RecordToAlertMapper.builder()
        .decisionsCollection(createDecisionCollection())
        .recordSignature("signature")
        .alertedParty(new GnsParty())
        .alertData(createAlertRow())
        .build()
  }

  def createDecisionCollection() {
    new DecisionsCollection(
        [
            Decision.newBuilder().build()
        ])
  }
}
