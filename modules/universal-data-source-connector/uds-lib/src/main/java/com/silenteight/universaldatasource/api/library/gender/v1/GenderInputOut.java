package com.silenteight.universaldatasource.api.library.gender.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.gender.v1.GenderInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class GenderInputOut {

  String match;

  @Builder.Default
  List<GenderFeatureInputOut> genderFeatureInputs = List.of();

  static GenderInputOut createFrom(GenderInput input) {
    return GenderInputOut.builder()
        .match(input.getMatch())
        .genderFeatureInputs(input.getGenderFeatureInputsList()
            .stream()
            .map(GenderFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
