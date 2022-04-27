package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid

import spock.lang.Specification
import spock.lang.Unroll

import java.sql.ResultSet
import java.sql.Statement
import java.util.function.Consumer

class ChunkProcessorSpec extends Specification {

  def consumer = Mock(Consumer)
  def objectUnderTest = new ChunkProcessor(consumer)

  @Unroll
  def 'should process #foundRecords and send #numberOfChunks chunks'() {
    given:

    def resultSet = Mock(ResultSet)
    def statement = Mock(Statement)
    def resultSetIdx = 0
    def context =
        AlertIdReaderContext.builder()
            .alertIdContext(AlertIdContext.builder()
                    .recordsView('')
                    .build())
            .chunkSize(1_000)
            .build()

    when:
    objectUnderTest.process(statement, context)

    then:
    1 * statement.executeQuery(_ as String) >> resultSet
    (foundRecords + 1) * resultSet.next() >> {(++resultSetIdx <= foundRecords)}
    foundRecords * resultSet.getString("SYSTEM_ID") >> "systemId"
    foundRecords * resultSet.getString("BATCH_ID") >> "batchId"
    numberOfChunks * consumer.accept(_ as AlertIdCollection)

    where:
    foundRecords | numberOfChunks
    0            | 0
    1            | 1
    999          | 1
    1000         | 1
    1001         | 2
    2000         | 2
    2001         | 3
  }
}
