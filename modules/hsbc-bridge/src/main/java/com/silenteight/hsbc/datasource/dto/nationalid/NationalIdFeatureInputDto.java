package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NationalIdFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDocumentNumbers = emptyList();
  @Builder.Default
  List<String> watchlistDocumentNumbers = emptyList();
  @Builder.Default
  List<String> alertedPartyCountries = emptyList();
  @Builder.Default
  List<String> watchlistCountries = emptyList();
}
