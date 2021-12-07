package com.silenteight.universaldatasource.api.library.nationalid.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.nationalid.v1.NationalIdInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class NationalIdInputOut {

  String match;

  @Builder.Default
  List<NationalIdFeatureInputOut> nationalIdFeatureInputs = List.of();

  static NationalIdInputOut createFrom(NationalIdInput input) {
    return NationalIdInputOut.builder()
        .match(input.getMatch())
        .nationalIdFeatureInputs(input.getNationalIdFeatureInputsList().stream()
            .map(NationalIdFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
