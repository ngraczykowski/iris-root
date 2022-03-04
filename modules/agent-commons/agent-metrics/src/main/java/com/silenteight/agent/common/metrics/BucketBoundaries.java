package com.silenteight.agent.common.metrics;

import lombok.Value;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableList;

@Value
public class BucketBoundaries {

  List<BucketBoundary> boundaries;

  public static BucketBoundaries of(int... values) {
    var boundaries = stream(values)
        .mapToObj(BucketBoundary::new)
        .collect(toUnmodifiableList());
    return new BucketBoundaries(boundaries);
  }
}
