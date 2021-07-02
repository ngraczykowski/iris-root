package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.util.List;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@Data
@RequiredArgsConstructor(staticName = "of")
public class GetAlertsSampleRequest {

  @NonNull
  @Valid
  DateRangeDto dateRangeDto;
  @NonNull
  List<Distribution> distributions;
  @NonNull
  Long limit;

  public List<RequestedAlertsFilter> getRequestedAlertsFilters() {
    return distributions.stream()
        .map(GetAlertsSampleRequest::getRequestedAlertsFilters)
        .collect(toList());
  }

  private static RequestedAlertsFilter getRequestedAlertsFilters(Distribution distribution) {
    return RequestedAlertsFilter
        .newBuilder()
        .setFieldName(distribution.getFieldName())
        .setFieldValue(distribution.getFieldValue())
        .build();
  }
}
