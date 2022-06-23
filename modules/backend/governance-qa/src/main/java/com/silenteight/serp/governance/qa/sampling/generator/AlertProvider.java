package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc.SamplingAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.generator.dto.AlertsSampleRequest;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class AlertProvider {

  @NonNull
  private final SamplingAlertsServiceBlockingStub samplingStub;

  private final long timeoutMs;

  List<String> getAlerts(AlertsSampleRequest request) {
    AlertsSampleResponse response = samplingStub
        .withDeadlineAfter(timeoutMs, MILLISECONDS)
        .getAlertsSample(getAlertsSampleRequest(request));

    return response
        .getAlertsList()
        .stream()
        .map(Alert::getName)
        .collect(toList());
  }

  private static com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest
      getAlertsSampleRequest(AlertsSampleRequest request) {

    return com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest
        .newBuilder()
        .setTimeRangeFrom(toTimestamp(request.getDateRangeDto().getFrom()))
        .setTimeRangeTo(toTimestamp(request.getDateRangeDto().getTo()))
        .setAlertCount(request.getLimit().intValue())
        .addAllRequestedAlertsFilter(request.getRequestedAlertsFilters())
        .build();
  }
}
