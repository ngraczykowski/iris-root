package com.silenteight.hsbc.datasource.dto.document;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class DocumentFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDocuments = emptyList();
  @Builder.Default
  List<String> watchlistDocuments = emptyList();
}
