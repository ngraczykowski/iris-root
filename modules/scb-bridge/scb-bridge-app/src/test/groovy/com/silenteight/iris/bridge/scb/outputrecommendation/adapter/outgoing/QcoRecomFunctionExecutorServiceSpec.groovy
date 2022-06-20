/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation.QcoInfo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameterValue
import spock.lang.Specification

class QcoRecomFunctionExecutorServiceSpec extends Specification {

  def recomFunctionName = 'recom'
  def jdbcTemplate = Mock(JdbcTemplate)
  def sourceApplicationValues = new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.SourceApplicationValues(alertLevel: 'al', watchlistLevel: 'wl')

  def objectUnderTest

  def setup() {
    objectUnderTest = new QcoRecomFunctionExecutorService(
        recomFunctionName, jdbcTemplate, sourceApplicationValues)
  }

  def 'should call recom function for solved alerts'() {
    given:
    def alertRecommendation = createAlertRecommendationInfo()

    when:
    objectUnderTest.execute(alertRecommendation)

    then:
    1 * jdbcTemplate.queryForObject(
        expectedQuery(),
        {
          Object[] p ->
            p.length == 13 &&
                p[0] == 'wl' &&
                p[1] == alertRecommendation.alertExternalId &&
                p[2] == alertRecommendation.batchId &&
                p[3] == alertRecommendation.hitWatchlistId &&
                p[4] == alertRecommendation.hitRecommendedStatus &&
                p[5] instanceof SqlParameterValue &&
                p[6] == alertRecommendation.listRecommendedStatus &&
                p[7] instanceof SqlParameterValue &&
                p[8] == alertRecommendation.qcoInfo.policyId &&
                p[9] == alertRecommendation.qcoInfo.hitId &&
                p[10] == alertRecommendation.qcoInfo.stepId &&
                p[11] == alertRecommendation.qcoInfo.fvSignature
                p[12] == alertRecommendation.qcoInfo.qcoSampled
        },
        String.class) >> '000'
  }

  def createQcoInfo() {
    QcoInfo.builder()
        .policyId('policyId')
        .hitId('hitId')
        .stepId('stepId')
        .fvSignature('fvSignature')
        .qcoSampled("yes")
        .build()
  }

  def createAlertRecommendationInfo() {
    CbsAlertRecommendation.builder()
        .alertExternalId('alertExternalId')
        .batchId('batchId')
        .hitWatchlistId('hitWatchlistId')
        .hitRecommendedStatus('hitRecommendedStatus')
        .hitRecommendedComments('hitRecommendedComments')
        .listRecommendedComments('alertRecommendedStatus')
        .listRecommendedStatus('alertRecommendedComments')
        .qcoInfo(createQcoInfo())
        .build()
  }

  def expectedQuery() {
    'SELECT ' + recomFunctionName + '(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) FROM dual'
  }
}
