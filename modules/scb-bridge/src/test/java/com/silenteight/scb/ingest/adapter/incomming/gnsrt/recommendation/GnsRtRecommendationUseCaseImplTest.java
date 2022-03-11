package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.adapter.outgoing.RegistrationApiClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static java.util.List.of;
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
  @Mock
  private RegistrationApiClient registrationApiClient;

  private GnsRtRecommendationUseCaseImpl underTest;

  @BeforeEach
  void setUp() {
    underTest = GnsRtRecommendationUseCaseImpl.builder()
        .alertMapper(alertMapper)
        .responseMapper(responseMapper)
        .alertInfoService(alertInfoService)
        .storeGnsRtRecommendationUseCase(storeGnsRtRecommendationUseCase)
        .recommendationService(recommendationService)
        .registrationApiClient(registrationApiClient)
        .build();
  }

  @Test
  void shouldCallNextWithMappedResponse() {
    var alerts = of(fixtures.alert1);
    when(alertMapper.map(fixtures.requestForAlert1)).thenReturn(alerts);

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .verifyComplete();
  }

  @Test
  void shouldCallNextWithMappedResponseForMultipleAlertRequest() {
    var alerts = of(fixtures.alert1, fixtures.alert2);
    when(alertMapper.map(fixtures.requestForAlert1AndAlert2)).thenReturn(alerts);

    var mono = underTest.recommend(fixtures.requestForAlert1AndAlert2);

    StepVerifier
        .create(mono)
        .verifyComplete();
  }
}
