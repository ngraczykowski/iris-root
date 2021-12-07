package com.silenteight.universaldatasource.api.library.nationalid.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchNationalIdInputsOut {

  @Builder.Default
  List<NationalIdInputOut> nationalIdInputs = List.of();

  static BatchGetMatchNationalIdInputsOut createFrom(
      BatchGetMatchNationalIdInputsResponse response) {
    return BatchGetMatchNationalIdInputsOut.builder()
        .nationalIdInputs(response.getNationalIdInputsList().stream()
            .map(NationalIdInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
