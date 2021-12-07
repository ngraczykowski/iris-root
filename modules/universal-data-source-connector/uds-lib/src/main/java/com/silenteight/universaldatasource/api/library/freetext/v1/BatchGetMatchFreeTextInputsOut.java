package com.silenteight.universaldatasource.api.library.freetext.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchFreeTextInputsOut {

  @Builder.Default
  List<FreeTextInputOut> freeTextInputs = List.of();

  static BatchGetMatchFreeTextInputsOut createFrom(BatchGetMatchFreeTextInputsResponse response) {
    return BatchGetMatchFreeTextInputsOut.builder()
        .freeTextInputs(response.getFreetextInputsList().stream()
            .map(FreeTextInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
