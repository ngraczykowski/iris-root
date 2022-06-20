/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZoneOffset

import static java.time.LocalDateTime.parse
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME

class DateConverterAcceptanceSpec extends Specification {

  def dateConverter = new DateConverter("Asia/Hong_Kong")

  def assertInputAndExpected(String input, String expected) {
    def expectedParsed = parse(expected, ISO_DATE_TIME).toInstant(ZoneOffset.UTC)
    dateConverter.convert(input).get() == expectedParsed
  }

  @Unroll
  def "should convert inputString=#input into #expected"() {
    expect:
    assertInputAndExpected(input, expected)

    where:
    input                           | expected
    " 2019/01/03 13:23:55"          | "2019-01-03T05:23:55.000"
    " 2019/01/03 13:23:55XXXX"      | "2019-01-03T05:23:55.000"
    " 2019/01/03 13:23:55.9"        | "2019-01-03T05:23:55.900"
    " 2019/01/03 13:23:55.94"       | "2019-01-03T05:23:55.940"
    "   2019/01/03 13:23:55.940"    | "2019-01-03T05:23:55.940"
    "2019/01/03 13:23:55.943"       | "2019-01-03T05:23:55.943"
    "2019-01-03 13:23:55"           | "2019-01-03T05:23:55.000"
    "  2019-01-03 13:23:55"         | "2019-01-03T05:23:55.000"
    "2019-01-03 13:23:55YYY"        | "2019-01-03T05:23:55.000"
    "   2019/01/03 13:23:55.943XXX" | "2019-01-03T05:23:55.943"
    "  2019/01/03 13:23:55.9435"    | "2019-01-03T05:23:55.943"
    "2019-01-03 13:23:55.9"         | "2019-01-03T05:23:55.900"
    "2019-01-03 13:23:55.94"        | "2019-01-03T05:23:55.940"
    "2019-01-03 13:23:55.940"       | "2019-01-03T05:23:55.940"
    "2019-01-03 13:23:55.943"       | "2019-01-03T05:23:55.943"
    "  1560842008  "                | "2019-06-18T07:13:28.000"
    "1560842008000 "                | "2019-06-18T07:13:28.000"
    "1560842008123"                 | "2019-06-18T07:13:28.123"
    "   2019/01/03 13:23:55.0"      | "2019-01-03T05:23:55.000"
    "2019/01/03 13:23:55.00"        | "2019-01-03T05:23:55.000"
    "2019/01/03 13:23:55.000"       | "2019-01-03T05:23:55.000"
    "2019/01/03 13:23:55."          | "2019-01-03T05:23:55.000"
  }

  @Unroll
  def "invalid date formats, input=#input"() {
    expect:
    dateConverter.convert(input).isEmpty()

    where:
    input << [
        null,
        "2019/01/03 13:23:5",
        "aaaa2019/01/03 03:23:55",
        "aaaa2019/01/03 03:23:55"
    ]
  }
}
