/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsHitDetailsHelperFetcher
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord.AlertRecordBuilder
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser.ParserException

import spock.lang.Specification

import java.sql.Connection

class SuspectDataFetcherSpec extends Specification {

  def hitDetailsParser = Mock(HitDetailsParser)
  def cbsHitDetailsHelperFetcher = Mock(CbsHitDetailsHelperFetcher)
  def connection = Mock(Connection)
  def hitDetails = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails)
  def suspects = [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect()]

  def objectUnderTest = new SuspectDataFetcher(
      hitDetailsParser,
      cbsHitDetailsHelperFetcher)

  def "should return empty list when blank details"() {
    given:
    def someAlertRow = createAlertRecordBuilder(details).build()

    when:
    def result = objectUnderTest.parseHitDetails(connection, someAlertRow)

    then:
    result.size() == 0
    0 * _

    where:
    details << [null, '']
  }

  def "should return empty list when parsing exception"() {
    given:
    def incorrectDetailsText = 'invalidText'
    def someAlertRow = createAlertRecordBuilder(incorrectDetailsText)
        .systemId('')
        .build()

    when:
    def result = objectUnderTest.parseHitDetails(connection, someAlertRow)

    then:
    result.size() == 0
    1 * hitDetailsParser.parse(incorrectDetailsText) >> {throw new ParserException()}
  }

  def "should parse hit details"() {
    given:
    def someDetailsText = 'someDetailsText'
    def someAlertRow = createAlertRecordBuilder(someDetailsText).build()

    when:
    hitDetailsParser.parse(someDetailsText) >> hitDetails
    hitDetails.extractUniqueSuspects() >> suspects
    hitDetails.getSuspects() >> suspects

    def result = objectUnderTest.parseHitDetails(connection, someAlertRow)

    then:
    result.size() == 1
    1 * cbsHitDetailsHelperFetcher
        .fetch(connection, someAlertRow.systemId, someAlertRow.batchId) >> []
  }

  AlertRecordBuilder createAlertRecordBuilder(String details) {
    AlertRecord.builder()
        .details(details)
        .systemId('systemId')
        .batchId('batchId')
  }
}
