package com.silenteight.scb.ingest.adapter.incomming.common.batch

import spock.lang.Specification

import java.sql.Connection
import javax.sql.DataSource

class DatabaseMultipleAlertCompositeFetcherSpec extends Specification {

  def dataSource = Mock(DataSource)
  def recordDecisionsFetcher = Mock(RecordDecisionsFetcher)
  def recordCompositeFetcher = Mock(RecordCompositeFetcher)
  def objectUnderTest = new DatabaseMultipleAlertCompositeFetcher(
      recordDecisionsFetcher, dataSource, recordCompositeFetcher)

  def someDecisionMap = ['sysId': []] as Map

  def "should fetch alerts"() {
    given:
    def connection = Mock(Connection)
    def systemIds = ['sysId']

    when:
    objectUnderTest.fetch(connection, systemIds)

    then:
    1 * recordDecisionsFetcher.fetchDecisions(connection, systemIds) >> someDecisionMap
    1 * recordCompositeFetcher.fetchRecordsWithDetails(connection, someDecisionMap, systemIds)
  }
}
