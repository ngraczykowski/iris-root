package com.silenteight.agent.common.metrics.micrometer;

import lombok.NoArgsConstructor;

import com.silenteight.agent.common.metrics.BucketBoundaries;
import com.silenteight.agent.common.metrics.BucketBoundary;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class ServiceLevelObjectivesMapper {

  // NOTE: 0 or less cannot be passed as service level objective so needed to pass
  // a little big greater value as a workaround
  public static final double MIN_SERVICE_LEVEL_OBJECTIVE = 0.001;

  static double[] map(BucketBoundaries buckets) {
    return buckets.getBoundaries().stream()
        .map(BucketBoundary::getValue)
        .distinct()
        .sorted()
        .mapToDouble(Integer::doubleValue)
        .map(value -> value <= 0 ? MIN_SERVICE_LEVEL_OBJECTIVE : value)
        .toArray();
  }
}
