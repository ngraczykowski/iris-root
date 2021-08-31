package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc.SamplingAlertsServiceBlockingStub;
import com.silenteight.serp.governance.qa.sampling.generator.dto.GetAlertsSampleRequest;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class AlertProvider {

  @NonNull
  private final SamplingAlertsServiceBlockingStub samplingStub;

  private final long timeoutMs;

  List<String> getAlerts(GetAlertsSampleRequest request) {
    AlertsSampleResponse response = samplingStub
        .withDeadlineAfter(timeoutMs, MILLISECONDS)
        .getAlertsSample(getAlertsSampleRequest(request));

    return response
        .getAlertsList()
        .stream()
        .map(Alert::getName)
        .collect(toList());
  }

  private static AlertsSampleRequest getAlertsSampleRequest(GetAlertsSampleRequest request) {
    return AlertsSampleRequest
        .newBuilder()
        .setTimeRangeFrom(toTimestamp(request.getDateRangeDto().getFrom()))
        .setTimeRangeTo(toTimestamp(request.getDateRangeDto().getTo()))
        .setAlertCount(request.getLimit().intValue())
        .addAllRequestedAlertsFilter(request.getRequestedAlertsFilters())
        .build();
  }
}
