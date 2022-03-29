package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade
import com.silenteight.scb.ingest.domain.model.Batch.Priority
import com.silenteight.scb.ingest.domain.model.BatchSource
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext
import com.silenteight.scb.ingest.domain.model.RegistrationResponse
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import static org.assertj.core.api.Assertions.assertThat

class GnsRtRecommendationUseCaseImplSpec extends Specification {

  static fixtures = new GnsRtFixtures()

  def alertMapper = Mock(GnsRtRequestToAlertMapper)

  def responseMapper = Mock(GnsRtResponseMapper)

  def alertInfoService = Mock(AlertInfoService)

  def storeGnsRtRecommendationUseCase = Mock(StoreGnsRtRecommendationUseCase)

  def recommendationService = Mock(RecommendationGatewayService)

  def registrationFacade = Mock(AlertRegistrationFacade)

  def ingestEventPublisher = Mock(IngestEventPublisher)

  def gnsRtRecommendationService = Mock(GnsRtRecommendationService)

  def rawAlertService = Mock(RawAlertService)

  def batchInfoService = Mock(BatchInfoService)

  @Subject
  def underTest = GnsRtRecommendationUseCaseImpl.builder()
      .alertMapper(alertMapper)
      .responseMapper(responseMapper)
      .alertInfoService(alertInfoService)
      .storeGnsRtRecommendationUseCase(storeGnsRtRecommendationUseCase)
      .recommendationService(recommendationService)
      .alertRegistrationFacade(registrationFacade)
      .ingestEventPublisher(ingestEventPublisher)
      .gnsRtRecommendationService(gnsRtRecommendationService)
      .rawAlertService(rawAlertService)
      .batchInfoService(batchInfoService)
      .build()

  def 'should resolve GnsRtRecommendationRequest with 1 alert'() {
    given:
    def mappedAlert = GnsRtResponseAlert.builder()
        .alertId("alertId")
        .policyId("policyId")
        .build()

    alertMapper.map(fixtures.gnsRtRecommendationRequest) >> fixtures.alerts
    responseMapper.map(fixtures.gnsAlert, fixtures.recommendation) >> mappedAlert
    gnsRtRecommendationService.recommendationsMono(_ as String)
        >> Mono.just(fixtures.recommendations)

    when:
    var mono = underTest.recommend(fixtures.gnsRtRecommendationRequest)

    then:
    StepVerifier
        .create(mono)
        .assertNext(
            r -> assertThat(r.getSilent8Response().getAlerts()).containsExactly(mappedAlert))
        .verifyComplete()

    1 * rawAlertService.store(_, fixtures.alerts)
    1 * batchInfoService.store(_, _ as BatchSource)
    1 * registrationFacade.registerSolvingAlerts(
        _, fixtures.alerts, new RegistrationBatchContext(Priority.HIGH, BatchSource.GNS_RT))
        >> RegistrationResponse.empty()
    1 * ingestEventPublisher.publish(_)
  }

}
