package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc.SamplingAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.GetAlertsSampleRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.serp.governance.qa.AlertFixture.generateDiscriminator;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.RISK_TYPE;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.SANCTION;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertProviderTest {

  @Mock
  private SamplingAlertsServiceBlockingStub samplingStub;

  private AlertProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new AlertProvider(samplingStub);
  }

  @Test
  void getAlertsShouldCallStubClientWithCorrectDateRangeAndRequestedAlertsFilter() {
    //given
    DateRangeDto dateRangeDto = new DateRangeDto(
        parse("2021-05-01T01:00:00Z"), parse("2021-05-31T01:00:00Z"));
    ArgumentCaptor<AlertsSampleRequest> requestArgumentCaptor = ArgumentCaptor
        .forClass(AlertsSampleRequest.class);
    GetAlertsSampleRequest getAlertsSampleRequest = GetAlertsSampleRequest.of(
        dateRangeDto, of(getDistribution()), 100L);
    when(samplingStub.getAlertsSample(any())).thenReturn(getAlertsSampleResponse());
    //when
    underTest.getAlerts(getAlertsSampleRequest);
    //then
    verify(samplingStub, times(1))
        .getAlertsSample(requestArgumentCaptor.capture());
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getAlertCount()).isEqualTo(100);
    assertThat(requestArgumentCaptor.getValue().getRequestedAlertsFilter(0))
        .isEqualTo(getRequestedAlertsFilter());
  }

  private AlertsSampleResponse getAlertsSampleResponse() {
    return AlertsSampleResponse
        .newBuilder()
        .addAlerts(getAlert())
        .build();
  }

  private Alert getAlert() {
    return Alert
        .newBuilder()
        .setName(generateDiscriminator())
        .build();
  }

  private Distribution getDistribution() {
    return Distribution
        .newBuilder()
        .setFieldName(RISK_TYPE)
        .setFieldValue(SANCTION)
        .build();
  }

  private RequestedAlertsFilter getRequestedAlertsFilter() {
    return RequestedAlertsFilter
        .newBuilder()
        .setFieldName(RISK_TYPE)
        .setFieldValue(SANCTION)
        .build();
  }
}
