/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import spock.lang.Specification

class OracleDatabaseGnsRtRequestGeneratorSpec extends Specification {

  def jdbcTemplate = Mock(JdbcTemplate)
  def mapper = Mock(GnsRtRequestMapper)
  def validator = Mock(GnsRtRecommendationRequestValidator)
  def objectUnderTest = new OracleDatabaseGnsRtRequestGenerator(jdbcTemplate, mapper, validator)

  def someRequest = new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest()
  def someAlertRecord = AlertRecord.builder()
      .systemId('systemId')
      .build()
  def someIdValue = 'id'

  def "should generate request by systemId"() {
    when:
    def result = objectUnderTest.generateBySystemId(someIdValue)

    then:
    result == someRequest
    1 * jdbcTemplate.query(
        {String query -> query.contains("R.SYSTEM_ID =")}, _ as RowMapper, _) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
  }

  def "should generate request by recordId"() {
    when:
    def result = objectUnderTest.generateByRecordId(someIdValue)

    then:
    result == someRequest
    1 * jdbcTemplate.query(
        {String query -> query.contains("R.RECORD_ID =")}, _ as RowMapper, _) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
  }

  def 'should throw EmptyResultDataAccessException'() {
    when: 'generate by systemId'
    objectUnderTest.generateBySystemId(someIdValue)

    then:
    1 * jdbcTemplate.query(_ as String, _ as RowMapper, _) >> []
    thrown(EmptyResultDataAccessException)

    when: 'generate by recordId'
    objectUnderTest.generateByRecordId(someIdValue)

    then:
    1 * jdbcTemplate.query(_ as String, _ as RowMapper, _) >> []
    thrown(EmptyResultDataAccessException)
  }

  def 'should generate request with random system_id'() {
    given:
    def numOfAlerts = 1

    when:
    def result = objectUnderTest.generateWithRandomSystemId(numOfAlerts)

    then:
    result == someRequest
    1 * jdbcTemplate.query(_ as String, _ as RowMapper, numOfAlerts) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
    1 * validator.isValid(someRequest) >> true
  }

  def 'should generate another request when request is invalid'() {
    given:
    def numOfAlerts = 1

    when:
    def result = objectUnderTest.generateWithRandomSystemId(numOfAlerts)

    then:
    result == someRequest
    1 * jdbcTemplate.query(_ as String, _ as RowMapper, numOfAlerts) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
    1 * validator.isValid(someRequest) >> false

    1 * jdbcTemplate.query(_ as String, _ as RowMapper, numOfAlerts) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
    1 * validator.isValid(someRequest) >> true
  }
}
