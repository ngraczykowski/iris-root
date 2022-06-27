package com.silenteight.simulator.retention.dataset.expired;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.dataretention.api.v1.DatasetsExpired;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DatasetExpiredFixtures {

  private static final String EXTERNAL_RESOURCE_NAME_1 =
      "datasets/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String EXTERNAL_RESOURCE_NAME_2 =
      "datasets/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final List<String> EXTERNAL_RESOURCE_NAMES =
      List.of(EXTERNAL_RESOURCE_NAME_1, EXTERNAL_RESOURCE_NAME_2);
  private static final String RESOURCE_NAME_1 = "datasets/a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  private static final String RESOURCE_NAME_2 = "datasets/04e81eda-5ce7-4ce7-843c-34ee32a5182f";
  static final List<String> RESOURCE_NAMES = List.of(RESOURCE_NAME_1, RESOURCE_NAME_2);
  private static final String ANALYSIS_NAME_1 = "datasets/658c898e-0691-49ed-a9e8-5010ae46c84b";
  private static final String ANALYSIS_NAME_2 = "datasets/3dccee78-146e-4eae-b654-eaad6f6bb469";
  static final List<String> ANALYSIS_NAMES = List.of(ANALYSIS_NAME_1, ANALYSIS_NAME_2);

  static final DatasetsExpired DATASETS_EXPIRED_MESSAGE =
      DatasetsExpired.newBuilder()
          .addAllDatasets(EXTERNAL_RESOURCE_NAMES)
          .build();
}
