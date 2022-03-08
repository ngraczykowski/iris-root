package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GnsRtRecommendationUseCaseImplTest {

  private final GnsRtRecommendationUseCaseImplFixtures
      fixtures = new GnsRtRecommendationUseCaseImplFixtures();

  @Mock
  private GnsRtRequestToAlertMapper alertMapper;
  @Mock
  private GnsRtResponseMapper responseMapper;
  @Mock
  private AlertInfoService alertInfoService;
  @Mock
  private StoreGnsRtRecommendationUseCase storeGnsRtRecommendationUseCase;
  @Mock
  private RecommendationGatewayService recommendationService;

  private GnsRtRecommendationUseCaseImpl underTest;

  @BeforeEach
  void setUp() {
    underTest = GnsRtRecommendationUseCaseImpl.builder()
        .alertMapper(alertMapper)
        .responseMapper(responseMapper)
        .alertInfoService(alertInfoService)
        .storeGnsRtRecommendationUseCase(storeGnsRtRecommendationUseCase)
        .recommendationService(recommendationService)
        .build();
  }

  @Test
  void shouldCallNextWithMappedResponse() {
    var alerts = of(fixtures.alert1);
    when(alertMapper.map(fixtures.requestForAlert1)).thenReturn(alerts);
    when(alertInfoService.sendAlertInfo(alerts)).thenReturn(Mono.empty());
    when(recommendationService.recommend(alerts))
        .thenReturn(Flux.just(fixtures.response1));
    when(responseMapper.map(fixtures.gnsAlert1, fixtures.recommendation1))
        .thenReturn(fixtures.mappedResponse1);

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .assertNext(r -> assertThat(r.getSilent8Response().getAlerts())
            .containsExactly(fixtures.mappedResponse1))
        .verifyComplete();
    verify(storeGnsRtRecommendationUseCase).storeRecommendation(
        fixtures.response1.getAlertRecommendation());
  }

  @Test
  void shouldCallNextWithMappedResponseForMultipleAlertRequest() {
    var alerts = of(fixtures.alert1, fixtures.alert2);
    when(alertMapper.map(fixtures.requestForAlert1AndAlert2)).thenReturn(alerts);
    when(alertInfoService.sendAlertInfo(alerts)).thenReturn(Mono.empty());
    when(recommendationService.recommend(alerts))
        .thenReturn(Flux.just(fixtures.response1, fixtures.response2));
    when(responseMapper.map(fixtures.gnsAlert1, fixtures.recommendation1))
        .thenReturn(fixtures.mappedResponse1);
    when(responseMapper.map(fixtures.gnsAlert2, fixtures.recommendation2))
        .thenReturn(fixtures.mappedResponse2);

    var mono = underTest.recommend(fixtures.requestForAlert1AndAlert2);

    StepVerifier
        .create(mono)
        .assertNext(r -> assertThat(r.getSilent8Response().getAlerts())
            .containsExactly(fixtures.mappedResponse1, fixtures.mappedResponse2))
        .verifyComplete();
    verify(storeGnsRtRecommendationUseCase).storeRecommendation(
        fixtures.response1.getAlertRecommendation());
    verify(storeGnsRtRecommendationUseCase).storeRecommendation(
        fixtures.response2.getAlertRecommendation());
  }

  @Test
  void whenUnexpectedRecommendationForGivenAlert_errorShouldBeCalled() {
    var alerts = of(fixtures.alert1);
    when(alertMapper.map(fixtures.requestForAlert1)).thenReturn(alerts);
    when(alertInfoService.sendAlertInfo(alerts)).thenReturn(Mono.empty());
    when(recommendationService.recommend(alerts))
        .thenReturn(Flux.just(fixtures.unexpectedResponse));

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .expectError(IllegalStateException.class)
        .verify();
  }

  @Test
  void whenSendingAlertInfoFailed_errorShouldBeCalledWithoutAskingForRecommendations() {
    when(alertInfoService.sendAlertInfo(anyList())).thenReturn(Mono.error(RuntimeException::new));

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .expectError(RuntimeException.class)
        .verify();
    verifyNoInteractions(recommendationService);
    verifyNoInteractions(storeGnsRtRecommendationUseCase);
  }

  @Test
  void whenRecommendingAlertsFailed_errorShouldBeCalled() {
    var alerts = of(fixtures.alert1);
    when(alertMapper.map(fixtures.requestForAlert1)).thenReturn(alerts);
    when(alertInfoService.sendAlertInfo(alerts)).thenReturn(Mono.empty());
    when(recommendationService.recommend(alerts))
        .thenReturn(Flux.error(RuntimeException::new));

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .expectError(RuntimeException.class)
        .verify();
  }
}
