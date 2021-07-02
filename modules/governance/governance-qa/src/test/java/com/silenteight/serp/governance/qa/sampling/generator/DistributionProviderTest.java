package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc.DistributionAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.PEP;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.RISK_TYPE;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.SANCTION;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributionProviderTest {

  @Mock
  private DistributionAlertsServiceBlockingStub distributionStub;

  private DistributionProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new DistributionProvider(distributionStub);
  }

  @Test
  void getDistributionShouldCallWithCorrectDateRangeAndGroupingFields() {
    //given
    DateRangeDto dateRangeDto = new DateRangeDto(
        parse("2021-05-01T01:00:00Z"), parse("2021-05-31T01:00:00Z"));
    List<String> groupingFields = of("riskType", "country");
    ArgumentCaptor<AlertsDistributionRequest> requestArgumentCaptor = ArgumentCaptor
        .forClass(AlertsDistributionRequest.class);
    when(distributionStub.getAlertsDistribution(any())).thenReturn(getAlertsDistributionResponse());
    //when
    underTest.getDistribution(dateRangeDto, groupingFields);
    //then
    verify(distributionStub, times(1))
        .getAlertsDistribution(requestArgumentCaptor.capture());
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getTimeRangeFrom())
        .isEqualTo(toTimestamp(dateRangeDto.getFrom()));
    assertThat(requestArgumentCaptor.getValue().getGroupingFields(0)).isEqualTo("riskType");
    assertThat(requestArgumentCaptor.getValue().getGroupingFields(1)).isEqualTo("country");
  }

  private AlertsDistributionResponse getAlertsDistributionResponse() {
    return AlertsDistributionResponse
        .newBuilder()
        .addAllAlertsDistribution(of(
            getAlertDistribution(1200, PEP),
            getAlertDistribution(1200, SANCTION)))
        .build();
  }

  private AlertDistribution getAlertDistribution(Integer alertCount, String fieldValues) {
    return AlertDistribution
        .newBuilder()
        .addAllGroupingFields(of(
            getDistribution(fieldValues),
            getDistribution(fieldValues)))
        .setAlertCount(alertCount)
        .build();
  }

  private Distribution getDistribution(String riskValue) {
    return Distribution
        .newBuilder()
        .setFieldName(RISK_TYPE)
        .setFieldValue(riskValue)
        .build();
  }
}
