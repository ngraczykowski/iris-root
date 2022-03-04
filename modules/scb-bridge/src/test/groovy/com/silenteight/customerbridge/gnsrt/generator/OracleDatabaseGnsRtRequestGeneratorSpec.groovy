package com.silenteight.customerbridge.gnsrt.generator

import com.silenteight.customerbridge.common.alertrecord.AlertRecord
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import spock.lang.Specification

class OracleDatabaseGnsRtRequestGeneratorSpec extends Specification {

  def jdbcTemplate = Mock(JdbcTemplate)
  def mapper = Mock(GnsRtRequestMapper)
  def objectUnderTest = new OracleDatabaseGnsRtRequestGenerator(jdbcTemplate, mapper)

  def someRequest = new GnsRtRecommendationRequest()
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
        {String query -> query.contains("R.SYSTEM_ID =")}, _, _ as RowMapper) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
  }

  def "should generate request by recordId"() {
    when:
    def result = objectUnderTest.generateByRecordId(someIdValue)

    then:
    result == someRequest
    1 * jdbcTemplate.query(
        {String query -> query.contains("R.RECORD_ID =")}, _, _ as RowMapper) >> [someAlertRecord]
    1 * mapper.map([someAlertRecord]) >> someRequest
  }

  def 'should throw EmptyResultDataAccessException'() {
    when: 'generate by systemId'
    objectUnderTest.generateBySystemId(someIdValue)

    then:
    1 * jdbcTemplate.query(_ as String, _, _ as RowMapper) >> []
    thrown(EmptyResultDataAccessException)

    when: 'generate by recordId'
    objectUnderTest.generateByRecordId(someIdValue)

    then:
    1 * jdbcTemplate.query(_ as String, _, _ as RowMapper) >> []
    thrown(EmptyResultDataAccessException)
  }
}
