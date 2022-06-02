package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeInput;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class IsOfGivenDocumentTypeInputOut {

  String match;

  @Builder.Default
  List<IsOfGivenDocumentTypeFeatureInputOut> isOfGivenDocumentTypeFeatureInputs = List.of();

  static IsOfGivenDocumentTypeInputOut createFrom(IsOfGivenDocumentTypeInput input) {
    return IsOfGivenDocumentTypeInputOut.builder()
        .match(input.getMatch())
        .isOfGivenDocumentTypeFeatureInputs(input.getIsOfGivenDocumentTypeFeaturesInputList()
            .stream()
            .map(IsOfGivenDocumentTypeFeatureInputOut::createFrom)
            .collect(toList())
        )
        .build();
  }
}
