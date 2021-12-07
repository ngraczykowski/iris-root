package com.silenteight.universaldatasource.api.library.location.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchLocationInputsOut {

  @Builder.Default
  List<LocationInputOut> locationInputs = List.of();

  static BatchGetMatchLocationInputsOut createFrom(BatchGetMatchLocationInputsResponse response) {
    return BatchGetMatchLocationInputsOut.builder()
        .locationInputs(response.getLocationInputsList().stream()
            .map(LocationInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
