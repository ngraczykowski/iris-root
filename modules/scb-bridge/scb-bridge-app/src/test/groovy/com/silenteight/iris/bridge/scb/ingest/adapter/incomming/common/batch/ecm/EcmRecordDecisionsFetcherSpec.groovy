/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision

import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class EcmRecordDecisionsFetcherSpec extends Specification {

  def ecmDecisionRowMapper = Mock(EcmDecisionRowMapper)
  def underTest = new EcmRecordDecisionsFetcher(ecmDecisionRowMapper, 'ecmView')

  def resultSet = Mock(ResultSet)
  def connection = Mock(Connection)
  def preparedStatement = Mock(PreparedStatement)

  def setup() {
    connection.prepareStatement(_ as String) >> preparedStatement
    preparedStatement.executeQuery() >> resultSet
  }

  def 'should fetch ecm decisions of existing externalIds'() {
    given:
    def resultSetIdx = 0
    def externalId1 = new ExternalId('systemId1', 'AS10782119')
    def externalId2 = new ExternalId('systemId1', 'AS10782120')
    def externalId3 = new ExternalId('systemId2', 'AS10782119')
    def externalIds = [externalId1, externalId2, externalId3]

    when:
    def result = underTest.fetchDecisions(connection, externalIds)

    then:
    result.size() == 1
    resultSet.next() >> {(++resultSetIdx <= 1)}
    1 * resultSet.getString('SYSTEM_ID') >> 'systemId1'
    1 * resultSet.getString('HIT_UNIQUE_ID') >> 'AS10782119_NAM_1'
    1 * ecmDecisionRowMapper.mapRow(resultSet) >> Decision.builder().build()
  }

  def 'should do not fetch ecm decisions of nonExisting externalIds'() {
    given:
    def resultSetIdx = 0
    def externalId1 = new ExternalId('systemId1', 'AS10782119')
    def externalId2 = new ExternalId('systemId1', 'AS10782120')
    def externalId3 = new ExternalId('systemId2', 'AS10782119')
    def externalIds = [externalId1, externalId2, externalId3]

    when:
    def result = underTest.fetchDecisions(connection, externalIds)

    then:
    result.isEmpty()
    resultSet.next() >> {(++resultSetIdx <= 1)}
    1 * resultSet.getString('SYSTEM_ID') >> systemId
    1 * resultSet.getString('HIT_UNIQUE_ID') >> hitUniqueId
    0 * ecmDecisionRowMapper.mapRow(resultSet)

    where:
    systemId        | hitUniqueId
    'dummySystemId' | 'AS10782119_NAM_1'
    'systemId1'     | 'SS10782119_NAM_1'
    'dummySystemId' | 'SS10782119_NAM_1'
  }
}
