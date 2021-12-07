package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchNameInputsOut {

  @Builder.Default
  List<NameInputOut> nameInputs = List.of();

  static BatchGetMatchNameInputsOut createFrom(BatchGetMatchNameInputsResponse response) {
    return BatchGetMatchNameInputsOut.builder()
        .nameInputs(response.getNameInputsList().stream()
            .map(NameInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
