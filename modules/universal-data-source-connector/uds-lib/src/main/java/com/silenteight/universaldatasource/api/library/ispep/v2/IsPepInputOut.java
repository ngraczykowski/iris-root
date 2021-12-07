package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.ispep.v2.IsPepInput;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class IsPepInputOut {

  String match;
  IsPepFeatureInputOut isPepFeatureInput;

  static IsPepInputOut createFrom(IsPepInput input) {
    return IsPepInputOut.builder()
        .match(input.getMatch())
        .isPepFeatureInput(IsPepFeatureInputOut.createFrom(input.getIsPepFeatureInput()))
        .build();
  }
}
