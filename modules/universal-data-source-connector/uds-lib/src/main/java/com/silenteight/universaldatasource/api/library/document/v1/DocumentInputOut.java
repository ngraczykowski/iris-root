package com.silenteight.universaldatasource.api.library.document.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.document.v1.DocumentInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class DocumentInputOut {

  String match;

  @Builder.Default
  List<DocumentFeatureInputOut> documentFeatureInputs = List.of();

  static DocumentInputOut createFrom(DocumentInput input) {
    return DocumentInputOut.builder()
        .match(input.getMatch())
        .documentFeatureInputs(input.getDocumentFeaturesInputList()
            .stream()
            .map(DocumentFeatureInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
