package com.silenteight.warehouse.sampling.distribution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import java.util.List;

import static com.silenteight.warehouse.common.time.Timestamps.toOffsetDateTime;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class DistributionAlertsService {

  @NonNull
  private final GroupingQueryService groupingQueryService;

  @NonNull
  private final SamplingProperties samplingProperties;

  public AlertsDistributionResponse getAlertsDistribution(AlertsDistributionRequest request) {

    FetchGroupedTimeRangedDataRequest fetchGroupedTimeRangedDataRequest =
        buildAlertsDistributionRequest(request);

    FetchGroupedDataResponse response =
        groupingQueryService.generate(fetchGroupedTimeRangedDataRequest);

    List<String> groupingFieldsList = request.getGroupingFieldsList();

    List<AlertDistribution> alertDistributionList =
        buildAlertDistributionListBasedOnField(groupingFieldsList, response);

    return AlertsDistributionResponse.newBuilder()
        .addAllAlertsDistribution(alertDistributionList)
        .build();
  }

  private static List<AlertDistribution> buildAlertDistributionListBasedOnField(
      List<String> fields, FetchGroupedDataResponse response) {

    return response.getRows().stream()
        .map(row -> buildAlertDistribution(fields, row))
        .collect(toList());
  }

  private static AlertDistribution buildAlertDistribution(List<String> fields, Row row) {
    return AlertDistribution.newBuilder()
        .addAllGroupingFields(getDistributionList(fields, row))
        .setAlertCount((int) row.getCount())
        .build();
  }

  private static List<Distribution> getDistributionList(List<String> fields, Row row) {
    return fields.stream()
        .map(field -> buildDistribution(field, row))
        .collect(toList());
  }

  private static Distribution buildDistribution(String fieldName, Row row) {
    return Distribution.newBuilder()
        .setFieldName(fieldName)
        .setFieldValue(row.getValue(fieldName))
        .build();
  }

  private FetchGroupedTimeRangedDataRequest buildAlertsDistributionRequest(
      AlertsDistributionRequest request) {

    return FetchGroupedTimeRangedDataRequest.builder()
        .from(toOffsetDateTime(request.getTimeRangeFrom()))
        .to(toOffsetDateTime(request.getTimeRangeTo()))
        .dateField(samplingProperties.getTimeFieldName())
        .fields(request.getGroupingFieldsList())
        .queryFilters(samplingProperties.getQueryFilters())
        .build();
  }
}
