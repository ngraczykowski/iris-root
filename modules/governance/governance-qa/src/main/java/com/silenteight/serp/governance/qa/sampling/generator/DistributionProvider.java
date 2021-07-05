package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc.DistributionAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;

@RequiredArgsConstructor
class DistributionProvider {

  @NonNull
  private final DistributionAlertsServiceBlockingStub distributionStub;

  List<AlertDistribution> getDistribution(@Valid DateRangeDto dateRangeDto,
      List<String> groupingFields) {

    AlertsDistributionResponse distributionResponse = distributionStub
        .getAlertsDistribution(createRequest(dateRangeDto, groupingFields));
    return distributionResponse.getAlertsDistributionList();
  }

  private static AlertsDistributionRequest createRequest(
      DateRangeDto dateRangeDto, List<String> groupingFields) {

    return AlertsDistributionRequest
        .newBuilder()
        .addAllGroupingFields(groupingFields)
        .setTimeRangeFrom(toTimestamp(dateRangeDto.getFrom()))
        .setTimeRangeTo(toTimestamp(dateRangeDto.getTo()))
        .build();
  }
}

