package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.warehouse.indexer.production.v2.ProductionAlertIndexV2UseCase;
import com.silenteight.warehouse.retention.production.AlertDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.MatchDiscriminatorResolvingService;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.ListUtils.partition;

@RequiredArgsConstructor
@Slf4j
public class ErasePersonalInformationUseCase {

  private static final String EMPTY_STRING = "";
  @NonNull
  private final AlertDiscriminatorResolvingService alertDiscriminatorResolvingService;
  @NonNull
  private final MatchDiscriminatorResolvingService matchDiscriminatorResolvingService;
  @NonNull
  private final ProductionAlertIndexV2UseCase productionAlertIndexUseCase;
  private final int batchSize;
  @NonNull
  private List<String> fields;

  public void activate(List<String> alerts) {
    log.info("Looking up for {} alerts to clear PII data.", alerts.size());
    partition(alerts, batchSize)
        .stream()
        .map(alertDiscriminatorResolvingService::getDiscriminatorsForAlertNames)
        .filter(list -> !list.isEmpty())
        .map(this::createRequestWithErasedPiiData)
        .forEach(this::activateProductionDataIndexRequest);
  }

  private ProductionDataIndexRequest createRequestWithErasedPiiData(List<String> discriminators) {
    return ProductionDataIndexRequest
        .newBuilder()
        .setRequestId(generateRequestId())
        .addAllAlerts(getAlerts(discriminators))
        .build();
  }

  private List<Alert> getAlerts(List<String> discriminators) {
    return discriminators.stream()
        .map(this::toAlert)
        .collect(toList());
  }

  private Alert toAlert(String discriminator) {
    return Alert.newBuilder()
        .setDiscriminator(discriminator)
        .setPayload(createPayload(fields))
        .addAllMatches(createMatches(discriminator))
        .build();
  }


  private static Struct createPayload(List<String> payload) {
    Map<String, Value> convertedMap = payload.stream()
        .collect(toMap(value -> value, value -> getEmptyStringValue()));

    return Struct.newBuilder().putAllFields(convertedMap).build();
  }

  private static Value getEmptyStringValue() {
    return Value.newBuilder().setStringValue(EMPTY_STRING).build();
  }

  private static String generateRequestId() {
    return randomUUID().toString();
  }

  private Iterable<Match> createMatches(String discriminator) {
    return matchDiscriminatorResolvingService.getDiscriminatorsForAlert(discriminator)
        .stream()
        .map(ErasePersonalInformationUseCase::createMatch)
        .collect(toList());
  }

  private static Match createMatch(String discriminator) {
    return Match
        .newBuilder()
        .setDiscriminator(discriminator)
        .setPayload(createMatchPayload())
        .build();
  }

  private static Struct createMatchPayload() {
    return Struct.newBuilder().build();
  }

  private void activateProductionDataIndexRequest(ProductionDataIndexRequest request) {
    log.debug("Sending ProductionDataIndexRequest with requestId {} and alerts count {}.",
        request.getRequestId(), request.getAlertsCount());
    productionAlertIndexUseCase.handle(request);
  }
}
