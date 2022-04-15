package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc.DistributionAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@AllArgsConstructor
class DistributionProvider {

  @NonNull
  private final DistributionAlertsServiceBlockingStub distributionStub;

  private final long timeoutMs;

  List<AlertDistribution> getDistribution(
      @Valid DateRangeDto dateRangeDto,
      List<String> groupingFields) {

    return distributionStub
        .withDeadlineAfter(timeoutMs, MILLISECONDS)
        .getAlertsDistribution(createRequest(dateRangeDto, groupingFields))
        .getAlertsDistributionList();
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

