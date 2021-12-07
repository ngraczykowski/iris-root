package com.silenteight.universaldatasource.api.library.allowlist.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.allowlist.v1.AllowListInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class AllowListInputOut {

  String match;

  @Builder.Default
  List<AllowListFeatureInputOut> allowListFeatureInputs = List.of();

  static AllowListInputOut createFrom(AllowListInput input) {
    return AllowListInputOut.builder()
        .match(input.getMatch())
        .allowListFeatureInputs(input.getAllowListFeatureInputsList()
            .stream()
            .map(AllowListFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
