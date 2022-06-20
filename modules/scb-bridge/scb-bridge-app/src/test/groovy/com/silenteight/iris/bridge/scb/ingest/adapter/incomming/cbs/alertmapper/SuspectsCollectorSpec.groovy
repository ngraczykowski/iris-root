/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser

import spock.lang.Specification

class SuspectsCollectorSpec extends Specification {


  def hitDetails = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails)
  def hitDetailsParser = Mock(HitDetailsParser)
  def objectUnderTest = new SuspectsCollector(hitDetailsParser)

  def 'should collect suspects'(){
    given:
    def hitDetailsString = 'hitDetails'
    def suspects = [new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect()]
    def cbsHitDetails = []

    when:
    def result = objectUnderTest.collect(hitDetailsString, cbsHitDetails)

    then:
    result == suspects
    1 * hitDetailsParser.parse(hitDetailsString) >> hitDetails
    1 * hitDetails.extractUniqueSuspects() >> suspects
    1 * hitDetails.getSuspects() >> suspects
  }
}
