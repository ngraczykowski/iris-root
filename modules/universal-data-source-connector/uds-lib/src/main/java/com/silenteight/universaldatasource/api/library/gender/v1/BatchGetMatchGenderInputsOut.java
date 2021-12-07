package com.silenteight.universaldatasource.api.library.gender.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchGenderInputsOut {

  @Builder.Default
  List<GenderInputOut> genderInputs = List.of();

  static BatchGetMatchGenderInputsOut createFrom(BatchGetMatchGenderInputsResponse response) {
    return BatchGetMatchGenderInputsOut.builder()
        .genderInputs(response.getGenderInputsList().stream()
            .map(GenderInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
