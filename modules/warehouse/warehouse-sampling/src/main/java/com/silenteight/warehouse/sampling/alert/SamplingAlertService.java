package com.silenteight.warehouse.sampling.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.warehouse.indexer.alert.AlertSearchCriteria;
import com.silenteight.warehouse.indexer.alert.RandomAlertQueryService;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class SamplingAlertService {

  @NonNull
  private final RandomAlertQueryService randomAlertQueryService;

  public AlertsSampleResponse generateSamplingAlerts(AlertsSampleRequest req) {
    AlertSearchCriteria alertSearchCriteria = buildAlertSearchCriteria(req);

    List<String> alertsIds =
        randomAlertQueryService.getRandomDiscriminatorByCriteria(alertSearchCriteria);

    return AlertsSampleResponse.newBuilder()
        .addAllAlerts(convertIdsToAlertsList(alertsIds))
        .build();
  }

  private static List<Alert> convertIdsToAlertsList(List<String> uniqueIds) {
    return uniqueIds.stream()
        .map(SamplingAlertService::buildAlert)
        .collect(toList());
  }

  private static Alert buildAlert(String id) {
    return Alert.newBuilder()
        .setName(id)
        .build();
  }

  private static Map<String, String> convertFilterToMap(
      List<RequestedAlertsFilter> requestedAlertsFilter) {

    return requestedAlertsFilter.stream()
        .collect(toMap(
            RequestedAlertsFilter::getFieldName,
            RequestedAlertsFilter::getFieldValue));
  }

  private static AlertSearchCriteria buildAlertSearchCriteria(AlertsSampleRequest req) {
    return AlertSearchCriteria.builder()
        .timeRangeFrom(convertTimeToDate(req.getTimeRangeFrom()))
        .timeRangeTo(convertTimeToDate(req.getTimeRangeTo()))
        .filter(convertFilterToMap(req.getRequestedAlertsFilterList()))
        .alertLimit(req.getAlertCount())
        .build();
  }

  private static String convertTimeToDate(Timestamp rangeTime) {
    LocalDateTime ldt =
        ofEpochSecond(rangeTime.getSeconds(), rangeTime.getNanos(), UTC);
    return ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(ldt);
  }

}
