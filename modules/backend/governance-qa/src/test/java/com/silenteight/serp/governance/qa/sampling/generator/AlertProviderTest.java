package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc.SamplingAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.AlertsSampleRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.FilterFixture.ALERT_RECOMMENDATION_FILTER;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.RISK_TYPE;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.SANCTION;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertProviderTest {

  private static final long TIMEOUT_MS = 1000L;

  @Mock
  private SamplingAlertsServiceBlockingStub samplingStub;

  private AlertProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new AlertProvider(samplingStub, TIMEOUT_MS);
  }

  @Test
  void getAlertsShouldCallStubClientWithCorrectDateRangeAndRequestedAlertsFilter() {
    //given
    DateRangeDto dateRangeDto = new DateRangeDto(
        parse("2021-05-01T01:00:00Z"), parse("2021-05-31T01:00:00Z"));
    ArgumentCaptor<com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest>
        requestArgumentCaptor = ArgumentCaptor.forClass(
            com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest.class);
    AlertsSampleRequest alertsSampleRequest = AlertsSampleRequest.of(
        dateRangeDto, of(getDistribution()), of(ALERT_RECOMMENDATION_FILTER),100L);
    when(samplingStub.withDeadlineAfter(anyLong(), any())).thenReturn(samplingStub);
    when(samplingStub.getAlertsSample(any())).thenReturn(getAlertsSampleResponse());
    //when
    underTest.getAlerts(alertsSampleRequest);
    //then
    verify(samplingStub, times(1))
        .getAlertsSample(requestArgumentCaptor.capture());
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getAlertCount()).isEqualTo(100);
    assertThat(requestArgumentCaptor.getValue().getRequestedAlertsFilterCount()).isEqualTo(2);
    assertThat(requestArgumentCaptor.getValue().getRequestedAlertsFilterList())
        .containsExactly(
            getRequestedAlertsFilter(ALERT_RECOMMENDATION_FILTER.getField(),
                ALERT_RECOMMENDATION_FILTER.getValues().get(0)),
            getRequestedAlertsFilter(RISK_TYPE, SANCTION)
        );
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
        .setName(generateAlertName())
        .build();
  }

  private Distribution getDistribution() {
    return Distribution
        .newBuilder()
        .setFieldName(RISK_TYPE)
        .setFieldValue(SANCTION)
        .build();
  }

  private RequestedAlertsFilter getRequestedAlertsFilter(String field, String value) {
    return RequestedAlertsFilter
        .newBuilder()
        .setFieldName(field)
        .setFieldValue(value)
        .build();
  }
}
