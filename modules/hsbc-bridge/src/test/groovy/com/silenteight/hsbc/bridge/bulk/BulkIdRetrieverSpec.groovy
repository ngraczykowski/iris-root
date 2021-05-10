package com.silenteight.hsbc.bridge.bulk

import spock.lang.Specification
import spock.lang.Unroll

class BulkIdRetrieverSpec extends Specification {

  def underTest = new BulkIdRetriever()

  @Unroll
  def 'should retrieve bulk Id from #json'() {
    when:
    def result = underTest.retrieve(json)

    then:
    result.get() == expectedResult

    where:
    json                            | expectedResult
    '{"bulkId":"123"}'              | '123'
    '{"alerts":[], "bulkId":"123"}' | '123'
    '{"bulkId":"123", "alerts":[]}' | '123'
  }

  @Unroll
  def 'should do not retrieve bulk Id from #json'() {
    when:
    def result = underTest.retrieve(json)

    then:
    result.isEmpty()

    where:
    json << [
        '{}',
        '[]',
        '{"BULK_ID":"123"}',
        '{"alerts":"123"}']
  }
}
