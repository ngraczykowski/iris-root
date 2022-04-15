package com.silenteight.serp.governance.vector.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;


/**
 * This class contains both names and values for each vector.
 * In other words, each object has all information needed to present this Feature Vector.
 * Any two objects CAN have a different list of names, eg:
 * <ul>
 *  <li>First FV: name, dob, country</li>
 *  <li>Second FV: dob, gender, nationality, residence</li>
 * </ul>
 *
 * @see com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto
 */
@Value
@Builder
public class FeatureVectorWithUsageDto {

  @NonNull
  String signature;
  long usageCount;
  @NonNull
  List<String> names;
  @NonNull
  List<String> values;

  public FeatureVectorDto standardize(List<String> columns) {
    return FeatureVectorDto.builder()
         .signature(getSignature())
         .matchesCount(getUsageCount())
         .values(mapToValues(columns))
         .build();
  }

  public FeatureVectorDto standardize(List<String> columns, String stepName) {
    return FeatureVectorDto.builder()
        .signature(getSignature())
        .matchesCount(getUsageCount())
        .values(mapToValues(columns))
        .step(stepName)
        .build();
  }

  private List<String> mapToValues(List<String> columns) {
    Map<String, String> nameValueMap = toNameValueMap();
    return columns
        .stream()
        .map(nameValueMap::get)
        .collect(toList());
  }

  public Map<String, String> toNameValueMap() {
    return range(0, names.size())
        .boxed()
        .collect(toMap(getNames()::get, getValues()::get));
  }
}
