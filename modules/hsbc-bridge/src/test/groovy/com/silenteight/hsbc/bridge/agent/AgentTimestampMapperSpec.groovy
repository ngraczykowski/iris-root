package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import spock.lang.Specification

class AgentTimestampMapperSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("dd-MMM-yy")
  def underTest = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())

  static String RAW_DATE = "10-DEC-19"

  def "Should parse date to unix timestamp"() {
    when:
    def result = underTest.toUnixTimestamp(RAW_DATE)

    then:
    result == 1575936000
  }

  def "Should throw DateParsingException when parameter is empty"() {
    when:
    underTest.toUnixTimestamp("")

    then:
    thrown DateParsingException
  }

  def "Should throw NPE with proper message when parameter is null"() {
    when:
    underTest.toUnixTimestamp(null)

    then:
    def e = thrown(NullPointerException)
    e.message == "rawDate is marked non-null but is null"
  }
}
