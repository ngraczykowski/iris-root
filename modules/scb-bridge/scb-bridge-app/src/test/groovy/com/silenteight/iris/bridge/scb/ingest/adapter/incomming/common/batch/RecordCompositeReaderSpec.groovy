/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsParty
import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader

import spock.lang.Specification

class RecordCompositeReaderSpec extends Specification {

  def jdbcCursorItemReader = Mock(BetterJdbcCursorItemReader)
  def multipleAlertCompositeFetcher = Mock(MultipleAlertCompositeFetcher)
  def someId = 'system-id'
  def someBatchId = 'batch-id'
  def someAlertComposite = createAlertComposite()

  def "should do not try to read any records when oraclePage is 0"() {
    when:
    def objectUnderTest = new RecordCompositeReader(
        jdbcCursorItemReader, multipleAlertCompositeFetcher, 0)
    def result = objectUnderTest.read()

    then:
    !result
    0 * _
  }

  def "should read single record"() {
    when:
    def objectUnderTest = new RecordCompositeReader(
        jdbcCursorItemReader, multipleAlertCompositeFetcher, 1)
    def result = objectUnderTest.read()

    then:
    result == someAlertComposite
    1 * jdbcCursorItemReader.read() >> someId
    1 * multipleAlertCompositeFetcher.fetch([someId]) >> [someAlertComposite]
  }

  def createAlertComposite() {
    def alertMapper = RecordToAlertMapper.builder()
        .alertData(createAlertRecord())
        .alertedParty(GnsParty.empty())
        .decisionsCollection(new DecisionsCollection([]))
        .recordSignature('')
        .build()
    def suspectsCollection = new SuspectsCollection([])

    AlertComposite.create(alertMapper, suspectsCollection)
  }

  def createAlertRecord() {
    AlertRecord.builder()
        .systemId(someId)
        .batchId(someBatchId)
        .fmtName('')
        .build()
  }
}
