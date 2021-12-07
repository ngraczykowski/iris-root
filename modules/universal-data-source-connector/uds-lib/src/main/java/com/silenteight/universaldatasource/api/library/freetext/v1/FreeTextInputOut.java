package com.silenteight.universaldatasource.api.library.freetext.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.freetext.v1.FreeTextInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class FreeTextInputOut {

  String match;

  @Builder.Default
  List<FreeTextFeatureInputOut> freeTextFeatureInputs = List.of();

  static FreeTextInputOut createFrom(FreeTextInput input) {
    return FreeTextInputOut.builder()
        .freeTextFeatureInputs(input.getFreetextFeatureInputsList().stream()
            .map(FreeTextFeatureInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
