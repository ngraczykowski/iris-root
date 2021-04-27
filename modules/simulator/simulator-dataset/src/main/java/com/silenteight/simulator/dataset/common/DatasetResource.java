package com.silenteight.simulator.dataset.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.removeStart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasetResource {

  private static final String DATASET_RESOURCE_NAME = "datasets/";

  public static String toResourceName(UUID datasetId) {
    return DATASET_RESOURCE_NAME + datasetId.toString();
  }

  public static UUID fromResourceName(String datasetName) {
    return fromString(removeStart(datasetName, DATASET_RESOURCE_NAME));
  }
}
