package com.silenteight.serp.governance.featurevector.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.google.protobuf.ByteString;

import java.util.List;

@Value
@Builder
public class FeatureVectorView {

  long id;

  @NonNull
  ByteString featuresSignature;

  @NonNull
  ByteString vectorSignature;

  @NonNull
  private List<String> values;
}
