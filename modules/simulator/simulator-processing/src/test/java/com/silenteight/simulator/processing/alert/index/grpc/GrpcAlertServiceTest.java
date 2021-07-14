package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.GetAlertRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.grpc.AlertFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcAlertServiceTest {

  @InjectMocks
  private GrpcAlertService underTest;

  @Mock
  private AlertServiceBlockingStub alertStub;

  @Test
  void shouldGetAlert() {
    // given
    when(alertStub.getAlert(makeGetAlertRequest(NAME))).thenReturn(ALERT);

    // when
    Alert alert = underTest.getAlert(NAME);

    // then
    assertThat(alert.getName()).isEqualTo(NAME);
    assertThat(alert.getAlertId()).isEqualTo(ID);
    assertThat(alert.getPriority()).isEqualTo(PRIORITY);
    assertThat(alert.getLabels()).isEqualTo(LABELS);
  }

  private static GetAlertRequest makeGetAlertRequest(String alert) {
    return GetAlertRequest.newBuilder()
        .setAlert(alert)
        .build();
  }
}
