package com.silenteight.hsbc.datasource.dto.document;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class DocumentInputDto {

  String match;
  @Builder.Default
  List<DocumentFeatureInputDto> featureInputs = Collections.emptyList();
}
