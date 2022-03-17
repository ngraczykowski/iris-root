package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.SQLTimeoutException

class RecordCompositeFetcherSpec extends Specification {

  def alertCompositeRowProcessor = Mock(AlertCompositeRowProcessor)
  def someTimeout = 1
  def configuration = new FetcherConfiguration('', someTimeout)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def suspectDataFetcher = Mock(SuspectDataFetcher)
  def objectUnderTest = new RecordCompositeFetcher(
      alertCompositeRowProcessor, configuration, suspectDataFetcher, eventPublisher)

  def connection = Mock(Connection)
  def preparedStatement = Mock(PreparedStatement)

  def "should handle SQLTimeoutException when fetchRecordWithDetails"() {
    given:
    def someDecisions = [] as Map
    def someIds = []

    when:
    connection.prepareStatement(_ as String) >> preparedStatement
    preparedStatement.executeQuery() >> {
      throw new SQLTimeoutException()
    }
    objectUnderTest.fetchRecordsWithDetails(connection, someDecisions, someIds)

    then:
    thrown(SQLException)
  }
}
