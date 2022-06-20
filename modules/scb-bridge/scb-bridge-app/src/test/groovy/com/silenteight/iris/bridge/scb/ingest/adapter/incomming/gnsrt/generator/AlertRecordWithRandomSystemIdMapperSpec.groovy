/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator


import spock.lang.Specification

import java.sql.ResultSet

class AlertRecordWithRandomSystemIdMapperSpec extends Specification {

  def "should map resultSet and generate new systemId"() {
    given:
    def unit = 'AE_PERD_DUDL'
    def resultSet = Mock(ResultSet)

    when:
    def result = AlertRecordWithRandomSystemIdMapper.mapResultSet(resultSet)

    then:
    result.systemId.contains(unit)
    result.systemId.length() == 48
    1 * resultSet.getString('batch_id') >> 'batchId'
    1 * resultSet.getString('unit') >> unit
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
}
