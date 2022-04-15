package com.silenteight.serp.governance.common.signature;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@RequiredArgsConstructor
@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CanonicalFeatureVector {

  List<String> names;
  List<String> values;

  @EqualsAndHashCode.Include
  Signature vectorSignature;

  public String vectorSignatureAsString() {
    return vectorSignature.asString();
  }
}
