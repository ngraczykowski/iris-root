/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway

import spock.lang.Specification
import spock.lang.Unroll

class CbsOutputSpec extends Specification {

  @Unroll
  def "should isCbsErrorStatus results in #expectedResult for statusCode: #statusCode"() {
    given:
    def cbsOutput = new CbsOutput(statusCode: statusCode)

    when:
    def result = cbsOutput.isCbsErrorStatus()

    then:
    result == expectedResult

    where:
    statusCode | expectedResult
    null       | true
    ''         | true
    '000'      | false
    '001'      | true
    '002'      | true
    '003'      | true
    '004'      | true
  }
}
