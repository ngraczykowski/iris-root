package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class NationalIdFeatureInputDto {

  String feature;
  List<String> alertedPartyDocumentNumbers;
  List<String> watchlistDocumentNumbers;
  String alertedPartyCountry;
  String watchlistCountry;
}
