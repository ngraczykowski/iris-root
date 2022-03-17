package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag
import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration

import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

class HitDetailsHelperFetcherSpec extends Specification {

  def connection = Mock(Connection)
  def dataSource = Mock(DataSource)
  def resultSet = Mock(ResultSet)
  def preparedStatement = Mock(PreparedStatement)
  def queryTimeout = 1
  def configuration = new FetcherConfiguration("TestViewName", queryTimeout)

  def objectUnderTest = new HitDetailsHelperFetcher(configuration)

  def setup() {
    dataSource.getConnection() >> connection
    connection.prepareStatement(_ as String) >> preparedStatement
    preparedStatement.executeQuery() >> resultSet
  }

  def 'should fetch and map records'() {
    given:
    def someId = "test1"
    def someBatchId = "batchId"
    def resultSetIdx = 0

    when:
    def results = objectUnderTest.fetch(connection, someId, someBatchId)

    then:
    results.size() == 1
    1 * preparedStatement.setQueryTimeout(queryTimeout)
    1 * preparedStatement.setFetchSize(1000)
    1 * preparedStatement.setString(1, someId)
    2 * resultSet.next() >> {(++resultSetIdx <= 1)}
    1 * resultSet.getString("system_id") >> "systemId"
    1 * resultSet.getString("batch_id") >> "batchId"
    1 * resultSet.getInt("seq_no") >> 1
    2 * resultSet.getString("hit_neo_flag") >> "N"

    verifyAll(results.first()) {
      batchId == "batchId"
      systemId == 'systemId'
      seqNo == 1
      hitNeoFlag == NeoFlag.NEW
    }
  }

  def 'should return empty list when there is an exception'() {
    given:
    Connection brokenConnection = Mock(Connection)
    def someId = "test1"
    def batchId = "batchId"

    when:
    def result = objectUnderTest.fetch(brokenConnection, someId, batchId)

    then:
    result.isEmpty()
    1 * brokenConnection.prepareStatement(_ as String) >> {
      String query -> throw Mock(SQLException)
    }
  }
}
