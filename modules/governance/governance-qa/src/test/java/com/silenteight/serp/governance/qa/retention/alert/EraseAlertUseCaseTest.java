package com.silenteight.serp.governance.qa.retention.alert;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
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
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EraseAlertUseCaseTest {

  private static final String ALERT_DISCRIMINATOR_1 = "54673323-7df0-484a-92d2-4a9c4ec6c55a";
  private static final String ALERT_DISCRIMINATOR_2 = "ecc07f88-596f-4341-9310-8aaab179b1f6";
  private static final String ALERT_DISCRIMINATOR_3 = "eb7c41ef-02e6-4583-86cb-c4d5b116207b";
  private static final String ALERT_DISCRIMINATOR_4 = "9b118f20-a070-4195-b565-75fcb41b51dd";
  private static final long ALERT_ID_3 = 3L;

  private EraseAlertUseCase underTest;

  @Mock
  private AlertQuery alertQuery;
  @Mock
  private AlertService alertService;

  private final int batchSize = 2;

  @BeforeEach
  void setUp() {
    underTest = new EraseAlertUseCase(alertQuery, alertService, batchSize);
  }

  @Test
  void activateShouldEraseOneAlert() {
    //given
    List<String> alerts = of(ALERT_DISCRIMINATOR_1, ALERT_DISCRIMINATOR_2, ALERT_DISCRIMINATOR_3,
        ALERT_DISCRIMINATOR_4);

    when(alertQuery.findIdsForDiscriminators(alerts.subList(0,2))).thenReturn(emptyList());
    when(alertQuery.findIdsForDiscriminators(alerts.subList(2,4)))
        .thenReturn(of(ALERT_ID_3));

    ArgumentCaptor<EraseAlertRequest> eraseAlertRequestCaptor =
        ArgumentCaptor.forClass(EraseAlertRequest.class);
    //when
    underTest.activate(alerts);
    //then
    verify(alertService,  times(1))
        .eraseAlert(eraseAlertRequestCaptor.capture());

    assertThat(eraseAlertRequestCaptor.getValue().getAlertId()).isEqualTo(ALERT_ID_3);
    assertThat(eraseAlertRequestCaptor.getValue().getDeletedBy()).isEqualTo(PRINCIPAL_NAME);
    assertThat(eraseAlertRequestCaptor.getValue().getCorrelationId()).isNotNull();
    assertThat(eraseAlertRequestCaptor.getValue().getDeletedAt()).isNotNull();
  }
}
