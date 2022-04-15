package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@Data
@RequiredArgsConstructor(staticName = "of")
public class AlertsSampleRequest {

  @NonNull
  @Valid
  DateRangeDto dateRangeDto;
  @NonNull
  List<Distribution> distributions;
  @NonNull
  List<Filter> defaultFilters;
  @NonNull
  Long limit;

  public List<RequestedAlertsFilter> getRequestedAlertsFilters() {
    return collectFilters()
        .stream()
        .map(AlertsSampleRequest::getRequestedAlertsFilters)
        .collect(toList());
  }

  private static RequestedAlertsFilter getRequestedAlertsFilters(Filter filter) {
    return RequestedAlertsFilter
        .newBuilder()
        .setFieldName(filter.getField())
        .setFieldValue(getValueFromFilter(filter))
        .build();
  }

  private static String getValueFromFilter(Filter filter) {
    return filter.getValues()
        .stream()
        .findFirst()
        .orElseThrow(() ->
            new InvalidConfigurationPropertyValueException(
                filter.getField(), filter, "Values cannot be empty")
    );
  }

  private List<Filter> collectFilters() {
    List<Filter> filters = new ArrayList<>();
    filters.addAll(defaultFilters); 
    filters.addAll(toFilters(distributions));
    return filters;
  }

  private static List<Filter> toFilters(List<Distribution> distributions) {
    return distributions.stream()
        .map(AlertsSampleRequest::toFilter)
        .collect(toList());
  }

  private static Filter toFilter(Distribution distribution) {
    return Filter.builder()
        .field(distribution.getFieldName())
        .values(List.of(distribution.getFieldValue()))
        .build();
  }
}
