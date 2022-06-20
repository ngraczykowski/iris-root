/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import com.silenteight.proto.serp.v1.alert.Decision
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsParty

import spock.lang.Specification

import java.sql.Connection
import javax.sql.DataSource

class DeltaAlertCompositeFetcherSpec extends Specification {

  def GNS_DELTA = "GNS_DELTA"
  def syncDeltaService = Mock(GnsSyncDeltaService)
  def dataSource = Mock(DataSource)
  def recordDecisionsFetcher = Mock(RecordDecisionsFetcher)
  def recordCompositeFetcher = Mock(RecordCompositeFetcher)
  def objectUnderTest = new DeltaAlertCompositeFetcher(
      recordDecisionsFetcher, dataSource, recordCompositeFetcher, syncDeltaService, GNS_DELTA)

  def decisionMap = ['sys-id-1': [someDecision], 'sys-id-2': [someDecision]]
  def someValue = 'test'
  def someDecision = new Decision()
  def someAlertComposite = createAlertComposite()

  def "should fetch alerts when delta is present"() {
    given:
    def connection = Mock(Connection)
    def systemIds = ['sys-id-1', 'sys-id-2']

    when:
    def result = objectUnderTest.fetch(connection, systemIds)

    then:
    !result.isEmpty()
    1 * recordDecisionsFetcher.fetchDecisions(connection, systemIds) >> decisionMap
    1 * syncDeltaService.findAllByAlertExternalId(systemIds, GNS_DELTA) >>
        ['sys-id-1': 1, 'sys-id-2': 0]
    1 * recordCompositeFetcher.fetchRecordsWithDetails(connection, decisionMap, ['sys-id-2']) >>
        [someAlertComposite]
  }

  def "should return empty list when no delta"() {
    given:
    def connection = Mock(Connection)
    def systemIds = ['sys-id-1', 'sys-id-2']

    when:
    def result = objectUnderTest.fetch(connection, systemIds)

    then:
    result.isEmpty()
    1 * recordDecisionsFetcher.fetchDecisions(connection, systemIds) >> decisionMap
    1 * syncDeltaService.findAllByAlertExternalId(systemIds, GNS_DELTA) >>
        ['sys-id-1': 1, 'sys-id-2': 1]
    0 * recordCompositeFetcher.fetchRecordsWithDetails(connection, _ as Map, systemIds)
  }

  def createAlertComposite() {
    def alertMapper = RecordToAlertMapper.builder()
        .alertData(createAlertRecord())
        .alertedParty(GnsParty.empty())
        .decisionsCollection(new DecisionsCollection([]))
        .recordSignature(someValue)
        .build()
    def suspectsCollection = new SuspectsCollection([])

    AlertComposite.create(alertMapper, suspectsCollection)
  }

  def createAlertRecord() {
    AlertRecord.builder()
        .systemId(someValue)
        .batchId(someValue)
        .fmtName(someValue)
        .build()
  }
}
