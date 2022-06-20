/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord


import spock.lang.Ignore
import spock.lang.Specification

import java.sql.ResultSet
import javax.validation.ConstraintViolationException

class AlertRecordMapperSpec extends Specification {

  def "should map resultSet"() {
    given:
    def resultSet = Mock(ResultSet)

    when:
    def result = AlertRecordMapper.mapResultSet(resultSet)

    then:
    result
    1 * resultSet.getString('system_id') >> 'systemId'
    1 * resultSet.getString('batch_id') >> 'batchId'
    1 * resultSet.getString('unit') >> 'unit'
    1 * resultSet.getString('db_account') >> 'dbAccount'
    1 * resultSet.getString("type_of_rec") >> 'I'
    1 * resultSet.getString('fmt_name')
    1 * resultSet.getString("last_dec_batch_id")
    1 * resultSet.getString("details")
    1 * resultSet.getString("db_city")
    1 * resultSet.getString("db_country")
    1 * resultSet.getString("db_dob")
    1 * resultSet.getString("db_name")
    1 * resultSet.getString("db_pob")
    1 * resultSet.getString("filtered")
    1 * resultSet.getString("record_id")
    1 * resultSet.getString("record")
    1 * resultSet.getInt("char_sep")
  }

  def 'should map char_sep int value to char'() {
    given:
    def resultSet = Mock(ResultSet)

    when:
    resultSet.getString('system_id') >> 'systemId'
    resultSet.getString('batch_id') >> 'batchId'
    resultSet.getString('unit') >> 'unit'
    resultSet.getString('db_account') >> 'dbAccount'
    resultSet.getString("type_of_rec") >> 'I'
    resultSet.getInt("char_sep") >> intValue
    def result = AlertRecordMapper.mapResultSet(resultSet)

    then:
    result.charSep == expectedChar

    where:
    intValue          | expectedChar
    0                 | Character.MIN_VALUE
    65                | 'A' as char
    126               | '~' as char
    Integer.MAX_VALUE | Character.MAX_VALUE
  }

  @Ignore
  def 'fails with null batchId'() {
    given:
    def resultSet = Mock(ResultSet)

    when:
    resultSet.getString('system_id') >> 'systemId'
    resultSet.getString('batch_id') >> null
    AlertRecordMapper.mapResultSet(resultSet)

    then:
    thrown(ConstraintViolationException)
  }
}
