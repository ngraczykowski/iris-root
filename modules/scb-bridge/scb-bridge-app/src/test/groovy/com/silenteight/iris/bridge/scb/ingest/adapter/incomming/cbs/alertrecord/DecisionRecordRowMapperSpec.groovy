/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.DateConverter

import spock.lang.Specification

import java.sql.ResultSet

import static java.time.Instant.ofEpochMilli
import static java.util.Optional.of

class DecisionRecordRowMapperSpec extends Specification {

  def dateConverter = Mock(DateConverter)
  def gnsSolutionMapper = Mock(GnsSolutionMapper)
  def underTest = new DecisionRecordRowMapper(dateConverter, gnsSolutionMapper)

  def 'should map result set'() {
    given:
    def resultSet = Mock(ResultSet)

    when:
    def result = underTest.mapResultSet(resultSet)

    then:
    1 * resultSet.getString('operator') >> 'operator'
    1 * resultSet.getString('system_id') >> 'systemId'
    1 * resultSet.getString('decision_date') >> '2019/01/07 09:16:28'
    1 * resultSet.getString('comments') >> 'comment'
    1 * resultSet.getString('state_name') >> 'FALSE'
    1 * resultSet.getInt('type') >> 1

    1 * dateConverter.convert('2019/01/07 09:16:28') >> of(ofEpochMilli(2000))
    1 * gnsSolutionMapper.mapSolution(1) >> com
        .silenteight
        .iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution.ANALYST_FALSE_POSITIVE

    result.operator == 'operator'
    result.comments == 'comment'
    result.systemId == 'systemId'
    result.stateName == 'FALSE'
    result.type == 1
    result.decisionDate.epochSecond == 2
    result.solution == com
        .silenteight
        .iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution.ANALYST_FALSE_POSITIVE
  }
}
