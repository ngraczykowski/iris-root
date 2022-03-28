package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.RawAlertService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtRequestToAlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper.GnsRtResponseMapper;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;

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
  private AlertRegistrationFacade registrationFacade;
  @Mock
  private IngestEventPublisher ingestEventPublisher;
  @Mock
  private RawAlertService rawAlertService;

  private GnsRtRecommendationUseCaseImpl underTest;

  @BeforeEach
  void setUp() {
    underTest = GnsRtRecommendationUseCaseImpl.builder()
        .alertMapper(alertMapper)
        .responseMapper(responseMapper)
        .alertInfoService(alertInfoService)
        .storeGnsRtRecommendationUseCase(storeGnsRtRecommendationUseCase)
        .recommendationService(recommendationService)
        .alertRegistrationFacade(registrationFacade)
        .ingestEventPublisher(ingestEventPublisher)
        .rawAlertService(rawAlertService)
        .build();
  }

  @Test
  void shouldCallNextWithMappedResponse() {
    var alerts = of(fixtures.alert1);
    when(alertMapper.map(fixtures.requestForAlert1)).thenReturn(alerts);
    when(registrationFacade.registerSolvingAlert(anyString(), eq(alerts), eq(Priority.HIGH)))
        .thenReturn(RegistrationResponse.builder().build());

    var mono = underTest.recommend(fixtures.requestForAlert1);

    StepVerifier
        .create(mono)
        .verifyComplete();

    verify(rawAlertService).store(anyString(), eq(alerts));
    verify(registrationFacade).registerSolvingAlert(anyString(), eq(alerts), eq(Priority.HIGH));
    verify(ingestEventPublisher).publish(any());
  }

  @Test
  void shouldCallNextWithMappedResponseForMultipleAlertRequest() {
    var alerts = of(fixtures.alert1, fixtures.alert2);
    when(alertMapper.map(fixtures.requestForAlert1AndAlert2)).thenReturn(alerts);
    when(registrationFacade.registerSolvingAlert(anyString(), eq(alerts), eq(Priority.HIGH)))
        .thenReturn(RegistrationResponse.builder().build());

    var mono = underTest.recommend(fixtures.requestForAlert1AndAlert2);

    StepVerifier
        .create(mono)
        .verifyComplete();

    verify(rawAlertService).store(anyString(), eq(alerts));
    verify(registrationFacade).registerSolvingAlert(anyString(), eq(alerts), eq(Priority.HIGH));
    verify(ingestEventPublisher, times(2)).publish(any());
  }
}
