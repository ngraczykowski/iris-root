package com.silenteight.warehouse.sampling.distribution;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.warehouse.sampling.distribution.DistributionTestFixtures.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributionAlertsServiceTest {

  @Mock
  private GroupingQueryService groupingQueryService;

  private DistributionAlertsService underTestService;

  @BeforeEach
  void setUp() {
    SamplingProperties samplingProperties = new SamplingProperties(of());

    underTestService = new DistributionConfiguration().distributionAlertsService(
        groupingQueryService, samplingProperties);
  }

  @Test
  void shouldReturnResponseWithCorrectValuesWhenRequestAskedForOneField() {
    //given
    when(groupingQueryService.generate(any())).thenReturn(FETCH_GROUPED_DATA_RESPONSE);
    //when
    AlertsDistributionResponse response =
        underTestService.getAlertsDistribution(ALERTS_DISTRIBUTION_REQUEST);
    //then
    assertThat(response.getAlertsDistributionCount()).isEqualTo(2);

    AlertDistribution alertDistribution = response.getAlertsDistributionList().get(0);
    assertThat(alertDistribution.getAlertCount()).isEqualTo(ALERT_RISK_TYPE_PEP_COUNT);
  }

  @Test
  void shouldReturnResponseWithCorrectValuesWhenRequestAskedForMoreThanOneField() {
    //given
    when(groupingQueryService.generate(any())).thenReturn(FETCH_GROUPED_DATA_RESPONSE_2);
    //when
    AlertsDistributionResponse response =
        underTestService.getAlertsDistribution(ALERTS_DISTRIBUTION_REQUEST_2);
    //then
    AlertDistribution alertDistribution = response.getAlertsDistributionList().get(0);

    assertThat(alertDistribution.getAlertCount()).isEqualTo(COUNTRY_AND_RISK_TYPE_COUNT);
    assertThat(alertDistribution.getGroupingFieldsList().size()).isEqualTo(2);
  }

  @Test
  void shouldCreateCorrectFetchGroupedDataRequest() {
    //given
    when(groupingQueryService.generate(any())).thenReturn(FETCH_GROUPED_DATA_RESPONSE_2);
    ArgumentCaptor<FetchGroupedTimeRangedDataRequest> captor =
        forClass(FetchGroupedTimeRangedDataRequest.class);

    //when
    underTestService.getAlertsDistribution(ALERTS_DISTRIBUTION_REQUEST_2);

    //then
    verify(groupingQueryService).generate(captor.capture());
    List<String> requestedFields = captor.getValue().getFields();

    assertThat(requestedFields).containsAll(GROUPING_FIELDS_2);
  }
}
