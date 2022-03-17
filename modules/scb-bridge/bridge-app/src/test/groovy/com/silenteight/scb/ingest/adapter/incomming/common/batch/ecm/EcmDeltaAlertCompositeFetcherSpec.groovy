package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.batch.AlertComposite
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DecisionsCollection
import com.silenteight.scb.ingest.adapter.incomming.common.batch.RecordToAlertMapper
import com.silenteight.scb.ingest.adapter.incomming.common.batch.SuspectsCollection
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty
import com.silenteight.proto.serp.v1.alert.Decision

import spock.lang.Specification

import java.sql.Connection
import javax.sql.DataSource

class EcmDeltaAlertCompositeFetcherSpec extends Specification {

  def ECM_DELTA = "ECM_DELTA"
  def syncDeltaService = Mock(GnsSyncDeltaService)
  def dataSource = Mock(DataSource)
  def ecmDataSource = Mock(DataSource)
  def ecmRecordDecisionsFetcher = Mock(EcmRecordDecisionsFetcher)
  def ecmRecordCompositeFetcher = Mock(EcmRecordCompositeFetcher)
  def objectUnderTest = new EcmDeltaAlertCompositeFetcher(
      ecmRecordDecisionsFetcher, ecmDataSource, dataSource, ecmRecordCompositeFetcher,
      syncDeltaService, ECM_DELTA)
  def externalIdSys1 = new ExternalId('sys-id-1', '123')
  def externalIdSys2 = new ExternalId('sys-id-2', '456')

  def someValue = 'test'
  def someDecision = new Decision()
  def someAlertComposite = createAlertComposite()

  def "should fetch alerts when delta is present"() {
    given:
    def connection = Mock(Connection)
    def systemIds = [externalIdSys1, externalIdSys2]

    def decisionMap = [(externalIdSys1): [new Decision()],
                       (externalIdSys2): [new Decision()]]
    def recordsCount = [(new ExternalId('sys-id-1|123', '123')): 1,
                        (new ExternalId('sys-id-2|456', '456')): 0]

    when:
    def result = objectUnderTest.fetch(connection, systemIds)

    then:
    !result.isEmpty()
    1 * ecmDataSource.getConnection() >> connection
    1 * ecmRecordDecisionsFetcher.fetchDecisions(connection, systemIds) >> decisionMap
    1 * syncDeltaService.findAllByExternalId(systemIds, ECM_DELTA) >> recordsCount
    1 * ecmRecordCompositeFetcher.
        fetchRecordsWithDetails(connection, decisionMap, [externalIdSys2]) >>
        [someAlertComposite]
  }

  def "should return empty list when no delta"() {
    given:
    def connection = Mock(Connection)
    def systemIds = [externalIdSys1, externalIdSys2]
    def decisionMap = [(externalIdSys1): [someDecision],
                       (externalIdSys2): [someDecision]]
    def recordsCount = [(new ExternalId('sys-id-1|123', '123')): 1,
                        (new ExternalId('sys-id-2|456', '456')): 1]

    when:
    def result = objectUnderTest.fetch(connection, systemIds)

    then:
    result.isEmpty()
    1 * ecmDataSource.getConnection() >> connection
    1 * ecmRecordDecisionsFetcher.fetchDecisions(connection, systemIds) >> decisionMap
    1 * syncDeltaService.findAllByExternalId(systemIds, ECM_DELTA) >> recordsCount
    0 * ecmRecordCompositeFetcher.fetchRecordsWithDetails(connection, _ as Map, systemIds)
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
