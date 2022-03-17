package com.silenteight.fab.dataprep.domain.feature;

import com.silenteight.universaldatasource.api.library.Feature;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public interface FabFeature {

  Feature buildFeature(BuildFeatureCommand buildFeatureCommand);

  default <T> List<T> merge(List<JsonNode> jsonNodes, Function<JsonNode, List<T>> function) {
    return jsonNodes.stream()
        .flatMap(jsonNode -> function.apply(jsonNode).stream())
        .collect(toList());
  }
}
