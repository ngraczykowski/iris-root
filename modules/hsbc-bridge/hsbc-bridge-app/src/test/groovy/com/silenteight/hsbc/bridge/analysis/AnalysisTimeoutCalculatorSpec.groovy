package com.silenteight.hsbc.bridge.analysis

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AnalysisTimeoutCalculatorSpec extends Specification {

  def timeout = Duration.ofHours(1L)
  def underTest = new AnalysisTimeoutCalculator(timeout)
  def static FORMATTER = DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm')
  def static FROM_0_TO_5K = OffsetDateTime.now().plusHours(1L).format(FORMATTER)
  def static FROM_5K_TO_10K = OffsetDateTime.now().plusHours(2L).format(FORMATTER)
  def static FROM_10K_TO_15K = OffsetDateTime.now().plusHours(3L).format(FORMATTER)
  def static FROM_15K_TO_20K = OffsetDateTime.now().plusHours(4L).format(FORMATTER)

  @Unroll
  def 'should calculate analysis timeout and return `#expectedResult` for alertsCount=`#alertsCount`'() {
    when:
    def result = underTest.determineTimeout(alertsCount)

    then:
    result.format(FORMATTER) == expectedResult

    where:
    alertsCount || expectedResult
    0           || FROM_0_TO_5K
    1           || FROM_0_TO_5K
    4999        || FROM_0_TO_5K
    5000        || FROM_5K_TO_10K
    5001        || FROM_5K_TO_10K
    9999        || FROM_5K_TO_10K
    10000       || FROM_10K_TO_15K
    10001       || FROM_10K_TO_15K
    14999       || FROM_10K_TO_15K
    15000       || FROM_15K_TO_20K
    15001       || FROM_15K_TO_20K
    19999       || FROM_15K_TO_20K
  }
}
