package com.silenteight.hsbc.bridge.json

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite
import com.silenteight.hsbc.bridge.json.ObjectConverter.ObjectConversionException
import com.silenteight.hsbc.bridge.json.external.model.AlertData

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.util.function.Consumer

class ObjectMapperJsonConverterSpec extends Specification {

  static var MAPPER = new ObjectMapper()
  def underTest = new ObjectMapperJsonConverter(10)

  def "should map payload to hashmap"() {
    when:
    def result = underTest.convertAlertDataToMap(getAlertData())

    then:
    result.get("DN_CASE.ID") == "1"
    result.get("DN_CASE.caseKey") == "123qwerty"
    result.get("DN_CASE.description") == "someDescription"
  }

  def "should add prefix to key"() {
    when:
    def result = underTest.convertAlertDataToMap(getAlertData())

    then:
    result.get("1.DN_CASEHISTORY.transition") == "someTransitionOne"
    result.get("2.DN_CASEHISTORY.transition") == "someTransitionTwo"
    result.get("3.DN_CASEHISTORY.transition") == "someTransitionThree"
  }

  def "should throw ObjectConversionException for payload == null"() {
    when:
    underTest.convert(null as byte[], AlertData.class)

    then:
    thrown(ObjectConversionException)
  }

  def "should convert and consume AlertData"() {
    given:
    def input = getClass().getResource("/files/nnsAlertTest.json").openStream()
    def command = new InputCommand("1", input)
    Consumer<AlertDataComposite> consumer = Mock()

    when:
    underTest.convertAndConsumeAlertData(command, consumer)

    then:
    noExceptionThrown()
    1 * consumer.accept(_)
  }

  AlertData getAlertData(){
    def json = getClass().getResource("/files/alertDataProdDateFormat.json")
    return MAPPER.readValue(json, AlertData.class)
  }
}
