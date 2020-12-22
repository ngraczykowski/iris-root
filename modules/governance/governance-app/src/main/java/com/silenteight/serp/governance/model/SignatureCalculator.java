package com.silenteight.serp.governance.model;

import com.google.protobuf.ByteString;

import java.util.List;

//TODO: if moved to common please replace it and remove
public interface SignatureCalculator {

  ByteString calculateFeaturesSignature(List<String> sortedFeatures);

  ByteString calculateVectorSignature(
      ByteString featureSignature, List<String> featureValues);
}
