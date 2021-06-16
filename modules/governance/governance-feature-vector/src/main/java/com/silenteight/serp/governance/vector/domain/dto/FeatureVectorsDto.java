package com.silenteight.serp.governance.vector.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;
import java.util.List;

import static java.util.List.of;

@Value
@Builder
public class FeatureVectorsDto {

  @NonNull
  @Default
  Collection<String> columns = of();
  @NonNull
  @Default
  Collection<FeatureVectorDto> featureVectors = of();

  /**
   * This class contains only values for each vector and can't live without FeatureVectorsDto
   * class.
   * In other words, each object has not enough information needed to present this Feature Vector.
   * Any two objects MUST have the same list of names in a collection in FeatureVectorsDto class,
   * eg:
   * <ul>
   *   <li>First FV: name, dob, country</li>
   *   <li>Second FV: name, dob, country</li>
   * </ul>
   *
   * @see FeatureVectorWithUsageDto
   */
  @Value
  @Builder
  public static class FeatureVectorDto {

    @NonNull
    String signature;
    long matchesCount;
    @NonNull
    @Default
    List<String> values = of();
  }
}

