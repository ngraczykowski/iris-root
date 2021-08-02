package com.silenteight.serp.governance.ingest.repackager;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.solving.api.v1.*;

import com.google.protobuf.Value;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.silenteight.solving.api.utils.Uuids.fromJavaUuid;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
public class IngestDataToSolvedEventRepackagerService {

  @NonNull
  private final Pattern featureOrCategoryPattern;
  @NonNull
  private final Pattern prefixOrSuffixPattern;

  public IngestDataToSolvedEventRepackagerService(
      String featureOrCategoryRegex, String prefixOrSuffixPattern) {
    featureOrCategoryPattern = Pattern.compile(featureOrCategoryRegex);
    this.prefixOrSuffixPattern = Pattern.compile(prefixOrSuffixPattern);
  }

  public FeatureVectorSolvedEventBatch activate(List<Alert> alertsList) {
    FeatureVectorSolvedEventBatch result = FeatureVectorSolvedEventBatch
        .newBuilder()
        .addAllEvents(repackAlertsToEvents(alertsList))
        .build();
    log.debug("FeatureVectorSolvedEventBatch = {}", result);
    return result;
  }

  private List<FeatureVectorSolvedEvent> repackAlertsToEvents(List<Alert> alertsList) {
    return alertsList.stream()
        .map(this::repackEventIfNeeded)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  private FeatureVectorSolvedEvent repackEventIfNeeded(Alert alert) {
    List<String> components = fetchVectorsComponentsFields(alert);
    if (components.isEmpty()) {
      log.warn("No features nor categories found in alert (alertDiscriminator={})",
               alert.getDiscriminator());
      return null;
    }

    return repackEvent(alert, components);
  }

  private FeatureVectorSolvedEvent repackEvent(Alert alert, List<String> components) {
    return FeatureVectorSolvedEvent
        .newBuilder()
        .setId(fromJavaUuid(randomUUID()))
        .setCorrelationId(fromJavaUuid(randomUUID()))
        .setFeatureCollection(getFeatureCollection(components))
        .setFeatureVector(getFeatureVector(alert, components))
        .build();
  }

  private FeatureCollection getFeatureCollection(List<String> components) {
    return FeatureCollection
        .newBuilder()
        .addAllFeature(getFeaturesList(components))
        .build();
  }

  private List<Feature> getFeaturesList(List<String> components) {
    return components
        .stream()
        .map(this::mapToFeaturesAndCategoriesNames)
        .map(feature -> Feature.newBuilder().setName(feature).build())
        .collect(toList());
  }

  private FeatureVector getFeatureVector(Alert alert, List<String> components) {
    List<String> result = components
        .stream()
        .map(component -> alert.getPayload().getFieldsOrThrow(component))
        .map(Value::getStringValue)
        .collect(toList());

    return FeatureVector.newBuilder().addAllFeatureValue(result).build();
  }

  List<String> fetchVectorsComponentsFields(Alert alert) {
    return alert
        .getPayload()
        .getFieldsMap()
        .keySet()
        .stream()
        .filter(this::isFeatureOrCategory)
        .collect(toList());
  }

  private boolean isFeatureOrCategory(String fieldName) {
    return featureOrCategoryPattern.matcher(fieldName).matches();
  }

  String mapToFeaturesAndCategoriesNames(String fieldName) {
    return prefixOrSuffixPattern.matcher(fieldName).replaceAll("");
  }
}
