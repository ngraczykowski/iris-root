/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.IngestedAlertsStatus
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class GnsRtRecommendationUseCaseImplSpec extends Specification {

  static fixtures = new GnsRtFixtures()

  def alertMapper = Mock(GnsRtRequestToAlertMapper)

  def responseMapper = Mock(GnsRtResponseMapper)

  def ingestService = Mock(BatchAlertIngestService)

  def gnsRtRecommendationService = Mock(GnsRtRecommendationService)

  def rawAlertService = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService)

  def batchInfoService = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService)

  def recommendationProperties = Mock(GnsRtRecommendationProperties)

  def trafficManager = Mock(TrafficManager)

  @Subject
  def underTest = GnsRtRecommendationUseCaseImpl.builder()
      .alertMapper(alertMapper)
      .responseMapper(responseMapper)
      .ingestService(ingestService)
      .gnsRtRecommendationService(gnsRtRecommendationService)
      .rawAlertService(rawAlertService)
      .batchInfoService(batchInfoService)
      .trafficManager(trafficManager)
      .recommendationProperties(recommendationProperties)
      .scheduler(Schedulers.boundedElastic())
      .build()

  def 'should resolve GnsRtRecommendationRequest with 1 alert'() {
    given:
    def mappedAlert = com
        .silenteight
        .iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert.builder()
        .alertId("alertId")
        .policyId("policyId")
        .build()

    recommendationProperties.getDeadlineInSeconds() >> 7
    alertMapper.map(fixtures.gnsRtRecommendationRequest, _ as String) >> fixtures.alerts
    responseMapper.map(fixtures.gnsAlert, fixtures.recommendation) >> mappedAlert
    gnsRtRecommendationService.recommendationsMono(_ as String)
        >> Mono.just(fixtures.recommendations)

    when:
    var mono = underTest.recommend(fixtures.gnsRtRecommendationRequest)

    and:
    StepVerifier
        .create(mono)
        .assertNext(
            r -> assertThat(r.getSilent8Response().getAlerts()).containsExactly(mappedAlert))
        .verifyComplete()

    then:
    1 * trafficManager.activateRtSemaphore()
    1 * rawAlertService.store(_, fixtures.alerts)
    1 * batchInfoService.store(_, _ as BatchSource, fixtures.alerts.size())
    1 * ingestService.ingestAlertsForRecommendation(
        _ as String, fixtures.alerts, RegistrationBatchContext.GNS_RT_CONTEXT) >>
        new IngestedAlertsStatus(fixtures.alerts, [])
  }

}
