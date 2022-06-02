package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.BatchGetIsOfGivenDocumentTypeInputsResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetIsOfGivenDocumentTypeInputsOut {

  @Builder.Default
  List<IsOfGivenDocumentTypeInputOut> documentInputs = List.of();

  static BatchGetIsOfGivenDocumentTypeInputsOut createFrom(
      BatchGetIsOfGivenDocumentTypeInputsResponse response) {
    return BatchGetIsOfGivenDocumentTypeInputsOut.builder()
        .documentInputs(response.getIsOfGivenDocumentTypeInputsList().stream()
            .map(IsOfGivenDocumentTypeInputOut::createFrom)
            .collect(toList()))
        .build();
  }
}
