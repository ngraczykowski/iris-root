/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State

import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.NonTransientDataAccessException
import org.springframework.dao.TransientDataAccessException
import org.springframework.jdbc.CannotGetJdbcConnectionException
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.SQLTransientException

class CbsAckGatewaySpec extends Specification {
  def ackFunctionName = 'ack'
  def jdbcTemplate = Mock(JdbcTemplate)
  def sourceApplicationValues = new SourceApplicationValues(alertLevel: 'al', watchlistLevel: 'wl')
  def eventPublisher = Mock(ApplicationEventPublisher)

  def objectUnderTest

  def setup() {
    objectUnderTest = new CbsAckGateway(ackFunctionName, jdbcTemplate, sourceApplicationValues)
    objectUnderTest.setEventPublisher(eventPublisher)
  }

  @Unroll
  def "should ackRecord and return OK if no error, watchlistLevel: #watchlistLevel"() {
    given:
    def someAlert = createAlert(watchlistLevel)

    when:
    def result = objectUnderTest.ackReadAlert(someAlert)

    then:
    result.state == State.OK
    1 * jdbcTemplate.queryForObject(
        expectedQuery(),
        {Object[] p -> p.length == 3 &&
            p[0] == expectedSourceAppl  &&
            p[1] == someAlert.alertExternalId &&
            p[2] == someAlert.batchId},
        String.class) >> '000'
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.AckCalledEvent ev -> ev.statusCode == '000'})

    where:
    watchlistLevel | expectedSourceAppl
    false          | 'al'
    true           | 'wl'
  }

  def "should ackAlerts and return fatal exception"() {
    given:
    def someAlert = createAlert(false)

    when:
    def result = objectUnderTest.ackReadAlert(someAlert)

    then:
    result.state == State.ERROR
    1 * jdbcTemplate.queryForObject(expectedQuery(), _ as Object[], String.class) >>
        {throw Mock(NonTransientDataAccessException)}
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent ev -> ev.functionType == 'ACK'})
  }

  def "should ackAlerts and return non-fatal exception when TransientDataAccessException"() {
    given:
    def someAlert = createAlert(false)

    when:
    def result = objectUnderTest.ackReadAlert(someAlert)

    then:
    result.state == State.TEMPORARY_FAILURE
    1 * jdbcTemplate.queryForObject(expectedQuery(), _ as Object[], String.class) >>
        {throw Mock(TransientDataAccessException)}
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent ev -> ev.functionType == 'ACK'})
  }

  def "should ackAlerts and return non-fatal exception when caused by SQLTransientException"() {
    given:
    def someAlert = createAlert(false)

    when:
    def result = objectUnderTest.ackReadAlert(someAlert)

    then:
    result.state == State.TEMPORARY_FAILURE
    1 * jdbcTemplate.queryForObject(expectedQuery(), _ as Object[], String.class) >>
        {throw new CannotGetJdbcConnectionException("Mock", Mock(SQLTransientException))}
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent ev -> ev.functionType == 'ACK'})
  }

  def expectedQuery() {
    'SELECT ' + ackFunctionName + '(?, ?, ?) FROM dual'
  }

  def createAlert(watchlistLevel) {
    new CbsAckAlert('alertExternalId', 'batchId', watchlistLevel)
  }
}
