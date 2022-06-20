/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.AlertComposite
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.SuspectDataFetcher
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.SuspectsCollection
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm.EcmRecordCompositeFetcher.EcmFetcherConfiguration
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.metrics.AlertsFetchedEvent
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution

import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.Tag
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class EcmRecordCompositeFetcherSpec extends Specification {

  def ecmAlertCompositeRowProcessor = Mock(EcmAlertCompositeRowProcessor)
  def configuration = new EcmFetcherConfiguration("dbRelationName", 1)
  def applicationEventPublisher = Mock(ApplicationEventPublisher)
  def suspectDataFetcher = Mock(SuspectDataFetcher)
  def underTest = new EcmRecordCompositeFetcher(
      ecmAlertCompositeRowProcessor, configuration, applicationEventPublisher, suspectDataFetcher)

  def fixtures = new Fixtures()

  def resultSet = Mock(ResultSet)
  def connection = Mock(Connection)
  def preparedStatement = Mock(PreparedStatement)

  def setup() {
    connection.prepareStatement(_ as String) >> preparedStatement
    preparedStatement.executeQuery() >> resultSet
  }

  def 'should do not fetch ecm alert records when delta is empty'() {
    given:
    def decisionsMap = prepareDecisionsMap()
    def delta = []

    when:
    underTest.fetchRecordsWithDetails(connection, decisionsMap, delta)

    then:
    0 * _
  }

  def 'should fetch ecm alert records when delta is not empty'() {
    given:
    def resultSetIdx = 0
    def decisionsMap = prepareDecisionsMap()
    def delta = [fixtures.externalId1]

    when:
    def result = underTest.fetchRecordsWithDetails(connection, decisionsMap, delta)

    then:
    result.size() == 1
    resultSet.next() >> {(++resultSetIdx <= 1)}
    1 * resultSet.getString('system_id') >> 'systemId1'
    1 * resultSet.getString('batch_id') >> 'batchId'
    1 * resultSet.getString('unit') >> 'unit'
    1 * resultSet.getString('db_account') >> 'dbAccount'
    1 * resultSet.getString('details') >> 'details'
    1 * applicationEventPublisher.publishEvent(_ as AlertsFetchedEvent)
    1 * suspectDataFetcher.parseHitDetails(connection, _ as AlertRecord) >>
        new SuspectsCollection([fixtures.suspect1, fixtures.suspect2, fixtures.suspect3])
    1 * ecmAlertCompositeRowProcessor.
        process(_ as AlertRecord, decisionsMap, {SuspectsCollection s -> s.size() == 1}) >>
        [fixtures.alertComposite]
  }

  def prepareDecisionsMap() {
    def decisionsMap = [:]
    decisionsMap.put(fixtures.externalId1, fixtures.decision1)
    decisionsMap.put(fixtures.externalId2, fixtures.decision2)
    decisionsMap.put(fixtures.externalId3, fixtures.decision3)
    decisionsMap
  }

  class Fixtures {

    Decision decision1 = Decision.builder().solution(AnalystSolution.ANALYST_TRUE_POSITIVE).build()
    Decision decision2 = Decision.builder().solution(AnalystSolution.ANALYST_FALSE_POSITIVE).build()
    Decision decision3 = Decision.builder().solution(
        AnalystSolution.ANALYST_POTENTIAL_TRUE_POSITIVE).build()

    ExternalId externalId1 = new ExternalId('systemId1', 'watchlistId1')
    ExternalId externalId2 = new ExternalId('systemId1', 'watchlistId2')
    ExternalId externalId3 = new ExternalId('systemId2', 'watchlistId1')

    Suspect suspect1 = new Suspect(ofacId: 'watchlistId1')
    Suspect suspect2 = new Suspect(ofacId: 'watchlistId2')
    Suspect suspect3 = new Suspect(ofacId: 'watchlistId3')

    ObjectId objectId = ObjectId.builder().build();

    Alert alert = Alert.builder().id(objectId).build()
    Tag tag = new ImmutableTag('', '')
    AlertComposite alertComposite = new AlertComposite(alert, 0, [tag])
  }
}
