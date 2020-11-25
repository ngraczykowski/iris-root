package com.silenteight.serp.governance.featureset.dto;

import lombok.Value;

import com.google.protobuf.ByteString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@Value
public class FeatureSetToStoreDto {

  @NotNull
  ByteString featuresSignature;

  @NotNull
  List<String> features;

  public String getFeaturesSignatureAsString() {
    return toBase64String(featuresSignature);
  }
}
