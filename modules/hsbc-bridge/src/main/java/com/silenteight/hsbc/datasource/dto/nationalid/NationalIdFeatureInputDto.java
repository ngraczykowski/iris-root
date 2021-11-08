package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class NationalIdFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDocumentNumbers = Collections.emptyList();
  @Builder.Default
  List<String> watchlistDocumentNumbers = Collections.emptyList();
  @Builder.Default
  List<String> alertedPartyCountries = Collections.emptyList();
  @Builder.Default
  List<String> watchlistCountries = Collections.emptyList();
}
