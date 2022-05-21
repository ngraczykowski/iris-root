package com.silenteight.hsbc.datasource.util

import spock.lang.Specification

import java.util.stream.Stream

class StreamUtilsSpec extends Specification {

  def 'should retrieve empty list when stream is empty'() {
    given:
    def stream = Stream.empty()

    when:
    def result = StreamUtils.toDistinctList(stream)

    then:
    result.isEmpty()
  }

  def 'should retrieve empty list when stream is null'() {
    when:
    def result = StreamUtils.toDistinctList(null)

    then:
    result.isEmpty()
  }

  def 'should retrieve list with distinct values from single stream'() {
    given:
    def stream = Stream.of('test', 'test', 'Test')

    when:
    def result = StreamUtils.toDistinctList(stream)

    then:
    verifyAll {
      result == ['test', 'Test']
    }
  }

  def 'should retrieve list with distinct values from multiple streams'() {
    given:
    def stream1 = Stream.of('test', 'test', 'Test')
    def stream2 = Stream.of('tested')

    when:
    def result = StreamUtils.toDistinctList(stream1, stream2)

    then:
    verifyAll {
      result == ['test', 'Test', 'tested']
    }
  }
}
