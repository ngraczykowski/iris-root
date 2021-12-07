package com.silenteight.universaldatasource.api.library.document.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchDocumentInputsOut {

  @Builder.Default
  List<DocumentInputOut> documentInputs = List.of();

  static BatchGetMatchDocumentInputsOut createFrom(BatchGetMatchDocumentInputsResponse response) {
    return BatchGetMatchDocumentInputsOut.builder()
        .documentInputs(response.getDocumentInputsList().stream()
            .map(DocumentInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
