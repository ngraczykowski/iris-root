package com.silenteight.serp.governance.qa.retention.alert;

import com.silenteight.serp.governance.qa.manage.domain.AlertService;
import com.silenteight.serp.governance.qa.manage.domain.dto.EraseAlertRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.qa.retention.alert.EraseAlertUseCase.PRINCIPAL_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EraseAlertUseCaseTest {

  private static final String ALERT_DISCRIMINATOR = "54673323-7df0-484a-92d2-4a9c4ec6c55a";

  private EraseAlertUseCase underTest;

  @Mock
  private AlertService alertService;

  @BeforeEach
  void setUp() {
    underTest = new EraseAlertUseCase(alertService);
  }

  @Test
  void activateShouldEraseOneAlert() {
    //given
    List<String> alerts = List.of(ALERT_DISCRIMINATOR);
    ArgumentCaptor<EraseAlertRequest> eraseAlertRequestCaptor =
        ArgumentCaptor.forClass(EraseAlertRequest.class);
    //when
    underTest.activate(alerts);
    //then
    verify(alertService,  times(1))
        .eraseAlert(eraseAlertRequestCaptor.capture());

    assertThat(eraseAlertRequestCaptor.getValue().getDiscriminator())
        .isEqualTo(ALERT_DISCRIMINATOR);
    assertThat(eraseAlertRequestCaptor.getValue().getDeletedBy())
        .isEqualTo(PRINCIPAL_NAME);
    assertThat(eraseAlertRequestCaptor.getValue().getCorrelationId())
        .isNotNull();
    assertThat(eraseAlertRequestCaptor.getValue().getDeletedAt()).isNotNull();
  }
}
