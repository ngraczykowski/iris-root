package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class IsPepFeatureInputDto {

  String feature;
  String region;
  List<ModelFieldValueDto> modelFieldValues;
}
