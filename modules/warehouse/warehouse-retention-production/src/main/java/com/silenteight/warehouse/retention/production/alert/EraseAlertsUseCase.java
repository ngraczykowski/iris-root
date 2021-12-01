package com.silenteight.warehouse.retention.production.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionMatchTrackingService;
import com.silenteight.warehouse.retention.production.AlertDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.MatchDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.alert.erasing.DocumentDto;
import com.silenteight.warehouse.retention.production.alert.erasing.DocumentEraseService;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class EraseAlertsUseCase {

  private final int batchSize;
  @NonNull
  private final AlertDiscriminatorResolvingService alertDiscriminatorResolvingService;
  @NonNull
  private final MatchDiscriminatorResolvingService matchDiscriminatorResolvingService;
  @NonNull
  private final ProductionAlertTrackingService productionAlertTrackingService;
  @NonNull
  private final ProductionMatchTrackingService productionMatchTrackingService;
  @NonNull
  private final DocumentEraseService documentEraseService;

  public void activate(List<String> alerts) {
    log.info("Looking up for {} alerts to remove.", alerts.size());

    partition(alerts, batchSize)
        .stream()
        .map(alertDiscriminatorResolvingService::getDiscriminatorsForAlertNames)
        .map(this::mapAlertsAndMatchesToDocuments)
        .forEach(documentEraseService::eraseDocuments);
  }

  private List<DocumentDto> mapAlertsAndMatchesToDocuments(List<String> alertsDiscriminator) {

    Map<String, String> indexNameByDiscriminator =
        productionAlertTrackingService.getIndexNameByDiscriminator(alertsDiscriminator);

    Map<String, List<String>> alertDiscriminatorToMatchesDiscriminators =
        getAlertDiscriminatorToMatchesDiscriminators(alertsDiscriminator);

    Map<String, String> indexNameByDiscriminatorMatchToErase =
        getIndexNameByDiscriminatorMatchToErase(alertDiscriminatorToMatchesDiscriminators);

    indexNameByDiscriminator.putAll(indexNameByDiscriminatorMatchToErase);

    return toDocumentsDto(indexNameByDiscriminator);
  }

  private Map<String, List<String>> getAlertDiscriminatorToMatchesDiscriminators(
      List<String> alertsDiscriminator) {

    return alertsDiscriminator.stream()
        .collect(toMap(identity(), matchDiscriminatorResolvingService::getDiscriminatorsForAlert));
  }

  private Map<String, String> getIndexNameByDiscriminatorMatchToErase(
      Map<String, List<String>> alertDiscriminatorToMatchesDiscriminators) {

    return alertDiscriminatorToMatchesDiscriminators.entrySet()
        .stream()
        .map(this::toIndexNameByDiscriminator)
        .flatMap(map -> map.entrySet().stream())
        .collect(toMap(Entry::getKey, Entry::getValue));
  }

  private Map<String, String> toIndexNameByDiscriminator(
      Entry<String, List<String>> alertDiscriminatorToMatches) {

    return productionMatchTrackingService.getIndexNameByDiscriminator(
        alertDiscriminatorToMatches.getKey(), alertDiscriminatorToMatches.getValue());
  }

  private static List<DocumentDto> toDocumentsDto(Map<String, String> indexNameByDiscriminator) {
    return indexNameByDiscriminator.entrySet().stream()
        .map(EraseAlertsUseCase::toDocumentDto)
        .collect(toList());
  }

  private static DocumentDto toDocumentDto(Entry<String, String> indexByDiscriminator) {
    return DocumentDto.builder()
        .documentId(indexByDiscriminator.getKey())
        .indexName(indexByDiscriminator.getValue())
        .build();
  }
}
