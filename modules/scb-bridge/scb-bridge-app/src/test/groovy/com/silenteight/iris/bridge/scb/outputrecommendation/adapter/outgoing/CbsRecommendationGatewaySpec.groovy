/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CbsRecommendationGatewaySpec extends Specification {

  def eventPublisher = Mock(ApplicationEventPublisher)
  def recomFunctionExecutorService = Mock(RecomFunctionExecutorService)
  def recommendationService = Mock(com
      .silenteight
      .iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService)

  def objectUnderTest

  def setup() {
    objectUnderTest = new CbsRecommendationGateway(
        recomFunctionExecutorService, recommendationService)
    objectUnderTest.setEventPublisher(eventPublisher)
  }

  def 'should call recom function for solved alerts'() {
    given:
    def alertRecommendation = createAlertRecommendationInfo()

    when:
    objectUnderTest.recommendAlert(alertRecommendation)

    then:
    1 * recomFunctionExecutorService.execute(alertRecommendation) >> '000'
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent ev -> ev.statusCode == '000'})
    1 * recommendationService.updateRecomStatus(
        alertRecommendation.alertExternalId,
        alertRecommendation.hitWatchlistId,
        '000')
  }

  def 'should try to call recom function and notify an error'() {
    given:
    def alertRecommendation = createAlertRecommendationInfo()

    when:
    objectUnderTest.recommendAlert(alertRecommendation)

    then:
    1 * recomFunctionExecutorService.execute(alertRecommendation) >> {throw new Exception()}
    1 * eventPublisher.publishEvent({com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent ev -> ev.functionType == 'RECOM'})
    1 * recommendationService.updateRecomStatus(
        alertRecommendation.alertExternalId,
        alertRecommendation.hitWatchlistId,
        'FAILED')
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
}
