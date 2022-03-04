package com.silenteight.customerbridge.common.recommendation

import com.silenteight.customerbridge.common.batch.DateConverter
import com.silenteight.customerbridge.common.config.FetcherConfiguration

import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class DiscriminatorFetcherSpec extends Specification {

  def resultSet = Mock(ResultSet)
  def connection = Mock(Connection)
  def preparedStatement = Mock(PreparedStatement)
  def dataSource = Mock(DataSource)
  def configuration = new FetcherConfiguration('', 1)
  def dateConverter = new DateConverter('Asia/Hong_Kong')
  def objectUnderTest = new DiscriminatorFetcher(dataSource, configuration, dateConverter)

  def setup() {
    dataSource.getConnection() >> connection
    connection.prepareStatement(_ as String) >> preparedStatement
  }

  def "should fetch discriminator value when decision date is present"() {
    given:
    def resultSetIdx = 0
    def someSystemId = 'systemId'

    when:
    preparedStatement.executeQuery() >> resultSet
    def result = objectUnderTest.fetch(someSystemId)

    then:
    result.get() == '2019-09-20T00:47:58Z'
    1 * resultSet.next() >> {(++resultSetIdx <= 1)}
    1 * resultSet.getString("DECISION_DATE") >> '2019/09/20 08:47:58'
    1 * resultSet.getString("FILTERED")
  }
}
