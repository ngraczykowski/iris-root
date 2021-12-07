package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.NameInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class NameInputOut {

  String match;

  @Builder.Default
  List<NameFeatureInputOut> nameFeatureInputs = List.of();

  static NameInputOut createFrom(NameInput input) {
    return NameInputOut.builder()
        .match(input.getMatch())
        .nameFeatureInputs(input.getNameFeatureInputsList().stream()
            .map(NameFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
