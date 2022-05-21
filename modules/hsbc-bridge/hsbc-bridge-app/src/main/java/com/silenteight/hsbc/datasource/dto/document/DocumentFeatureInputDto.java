package com.silenteight.hsbc.datasource.dto.document;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class DocumentFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDocuments = Collections.emptyList();
  @Builder.Default
  List<String> watchlistDocuments = Collections.emptyList();
}
