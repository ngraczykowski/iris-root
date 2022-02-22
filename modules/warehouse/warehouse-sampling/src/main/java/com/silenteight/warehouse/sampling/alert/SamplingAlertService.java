package com.silenteight.warehouse.sampling.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.warehouse.indexer.alert.AlertColumnName;
import com.silenteight.warehouse.indexer.query.MultiValueEntry;
import com.silenteight.warehouse.indexer.query.single.AlertSearchCriteria;
import com.silenteight.warehouse.indexer.query.single.RandomAlertService;
import com.silenteight.warehouse.sampling.configuration.FilterProperties;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SamplingAlertService {

  @NonNull
  private final RandomAlertService randomAlertQueryService;

  @NonNull
  private final SamplingProperties samplingProperties;

  public AlertsSampleResponse generateSamplingAlerts(
      AlertsSampleRequest alertsSampleRequest) {
    AlertSearchCriteria
        alertSearchCriteria = SamplingAlertService.buildAlertSearchCriteria(
        alertsSampleRequest, samplingProperties, AlertColumnName.RECOMMENDATION_DATE);

    List<String> alertsIds =
        randomAlertQueryService.getRandomAlertNameByCriteria(alertSearchCriteria);

    return AlertsSampleResponse.newBuilder()
        .addAllAlerts(SamplingAlertService.convertIdsToAlertsList(alertsIds))
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

  private static AlertSearchCriteria buildAlertSearchCriteria(
      AlertsSampleRequest alertsSampleRequest, SamplingProperties samplingProperties,
      AlertColumnName timeFieldName) {

    Stream<MultiValueEntry> externalFilters = alertsSampleRequest.getRequestedAlertsFilterList()
        .stream()
        .map(SamplingAlertService::asMultiValueEntry);

    Stream<MultiValueEntry> internalFilters = samplingProperties.getFilters()
        .stream()
        .map(FilterProperties::toMultiValueEntry);

    List<MultiValueEntry> allFilters = Stream.concat(externalFilters, internalFilters)
        .collect(toList());

    return AlertSearchCriteria.builder()
        .timeFieldName(timeFieldName)
        // TODO: conversion will be not needed after switching to postgres as we are storing it as
        // timestamp
        .timeRangeFrom(convertTimeToDate(alertsSampleRequest.getTimeRangeFrom()))
        .timeRangeTo(convertTimeToDate(alertsSampleRequest.getTimeRangeTo()))
        .alertLimit(alertsSampleRequest.getAlertCount())
        .filter(allFilters)
        .build();
  }

  private static String convertTimeToDate(Timestamp rangeTime) {
    LocalDateTime ldt = ofEpochSecond(rangeTime.getSeconds(), rangeTime.getNanos(), UTC);
    return ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(ldt);
  }

  private static MultiValueEntry asMultiValueEntry(RequestedAlertsFilter requestedAlertsFilter) {
    return new MultiValueEntry(
        requestedAlertsFilter.getFieldName(),
        of(requestedAlertsFilter.getFieldValue()));
  }
}
