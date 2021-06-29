package com.silenteight.hsbc.bridge.json

import com.silenteight.hsbc.bridge.json.external.model.AlertData

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class ObjectMapperJsonConverterSpec extends Specification {

  static var MAPPER = new ObjectMapper()
  def underTest = new ObjectMapperJsonConverter()

  def "should map payload to hashmap"(){
    when:
    def result = underTest.convertPayloadToMap(getPayload())

    then:
    result.get("DN_CASE.ID") == "1"
    result.get("DN_CASE.caseKey") == "123qwerty"
    result.get("DN_CASE.description") == "someDescription"
  }

  def "should add prefix to key"(){
    when:
    def result = underTest.convertPayloadToMap(getPayload())

    then:
    result.get("1.DN_CASEHISTORY.transition") == "someTransitionOne"
    result.get("2.DN_CASEHISTORY.transition") == "someTransitionTwo"
    result.get("3.DN_CASEHISTORY.transition") == "someTransitionThree"
  }

  byte[] getPayload(){
    def alert = getAlertData()
    return MAPPER.writeValueAsBytes(alert)
  }

  AlertData getAlertData(){
    def json = getClass().getResource("/files/alertData.json")
    return MAPPER.readValue(json, AlertData.class)
  }
}
