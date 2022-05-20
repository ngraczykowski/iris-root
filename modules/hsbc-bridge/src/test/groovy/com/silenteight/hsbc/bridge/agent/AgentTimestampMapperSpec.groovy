package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import spock.lang.Specification
import spock.lang.Unroll

class AgentTimestampMapperSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("[yyyy-MMM-dd HH:mm:ss][dd-MMM-yy]")
  def underTest = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())

  @Unroll
  def "Should parse date to unix timestamp. Given: `#inputData` Expected: `#expectedResult`"() {
    when:
    def result = underTest.toUnixTimestamp(inputData)

    then:
    result == expectedResult

    where:
    inputData              | expectedResult
    '2020-DEC-30 00:00:00' | 1609286400
    '30-DEC-20'            | 1609286400
    '2004-JAN-01 12:12:12' | 1072959132
  }

  def "Should throw DateParsingException when parameter is empty"() {
    when:
    underTest.toUnixTimestamp("")

    then:
    thrown DateTimeParsingException
  }

  def "Should throw NPE with proper message when parameter is null"() {
    when:
    underTest.toUnixTimestamp(null)

    then:
    def e = thrown(NullPointerException)
    e.message == "rawDate is marked non-null but is null"
  }

  def "Should throw DateParsingException with stacktrace message when date can not be parsed"() {
    given:
    def inputDate = "01-DEC-2001"

    when:
    underTest.toUnixTimestamp(inputDate)

    then:
    def e = thrown(DateTimeParsingException)
  }
}
