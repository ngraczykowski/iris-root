package com.silenteight.hsbc.bridge.agent

import spock.lang.Specification

class AgentUtilsSpec extends Specification {

  static String RAW_DATE = "10-DEC-19"

  def "Should parse date to unix timestamp"() {
    when:
    def result = AgentUtils.toUnixTimestamp(RAW_DATE)

    then:
    result == 1575936000
  }

  def "Should throw DateParsingException when parameter is empty"() {
    when:
    AgentUtils.toUnixTimestamp("")

    then:
    thrown DateParsingException
  }

  def "Should throw NPE with proper message when parameter is null"() {
    when:
    AgentUtils.toUnixTimestamp(null)

    then:
    def e = thrown(NullPointerException)
    e.message == "rawDate is marked non-null but is null"
  }
}
