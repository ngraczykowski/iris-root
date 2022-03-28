package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.SourceApplicationValues
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation

import org.springframework.context.ApplicationEventPublisher
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameterValue
import spock.lang.Specification

class CbsRecommendationGatewaySpec extends Specification {

  def recomFunctionName = 'recom'
  def jdbcTemplate = Mock(JdbcTemplate)
  def sourceApplicationValues = new SourceApplicationValues(alertLevel: 'al', watchlistLevel: 'wl')
  def eventPublisher = Mock(ApplicationEventPublisher)

  def objectUnderTest

  def setup() {
    objectUnderTest = new CbsRecommendationGateway(
        recomFunctionName, jdbcTemplate, sourceApplicationValues)
    objectUnderTest.setEventPublisher(eventPublisher)
  }

  def 'should call recom function for solved alerts'() {
    given:
    def alertRecommendation = createAlertRecommendationInfo()

    when:
    objectUnderTest.recommendAlert(alertRecommendation)

    then:
    1 * jdbcTemplate.queryForObject(
        expectedQuery(),
        {
          Object[] p ->
            p.length == 8 &&
                p[0] == 'wl' &&
                p[1] == alertRecommendation.alertExternalId &&
                p[2] == alertRecommendation.batchId &&
                p[3] == alertRecommendation.hitWatchlistId &&
                p[4] == alertRecommendation.hitRecommendedStatus &&
                p[5] instanceof SqlParameterValue &&
                p[6] == alertRecommendation.listRecommendedStatus
            p[7] instanceof SqlParameterValue
        },
        String.class) >> '000'
    1 * eventPublisher.publishEvent({RecomCalledEvent ev -> ev.statusCode == '000'})
  }

  def 'should try to call recom function and notify an error'() {
    given:
    def alertRecommendation = createAlertRecommendationInfo()

    when:
    objectUnderTest.recommendAlert(alertRecommendation)

    then:
    1 * jdbcTemplate.queryForObject(expectedQuery(), _ as Object[], String.class) >>
        {throw new Exception()}
    1 * eventPublisher.publishEvent({CbsCallFailedEvent ev -> ev.functionType == 'RECOM'})
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
        .build()
  }

  def expectedQuery() {
    'SELECT ' + recomFunctionName + '(?, ?, ?, ?, ?, ?, ?, ?) FROM dual'
  }
}
