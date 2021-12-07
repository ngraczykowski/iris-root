package com.silenteight.universaldatasource.api.library.allowlist.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchAllowListInputsOut {

  @Builder.Default
  List<AllowListInputOut> allowListInputs = List.of();

  static BatchGetMatchAllowListInputsOut createFrom(BatchGetMatchAllowListInputsResponse response) {
    return BatchGetMatchAllowListInputsOut.builder()
        .allowListInputs(response.getAllowListInputsList()
            .stream()
            .map(AllowListInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
