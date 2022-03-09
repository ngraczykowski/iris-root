package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect

import spock.lang.Specification

class SuspectsCollectorSpec extends Specification {


  def hitDetails = Mock(HitDetails)
  def hitDetailsParser = Mock(HitDetailsParser)
  def objectUnderTest = new SuspectsCollector(hitDetailsParser)

  def 'should collect suspects'(){
    given:
    def hitDetailsString = 'hitDetails'
    def suspects = [new Suspect()]
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
