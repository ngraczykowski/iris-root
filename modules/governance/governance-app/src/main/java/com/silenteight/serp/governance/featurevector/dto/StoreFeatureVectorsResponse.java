package com.silenteight.serp.governance.featurevector.dto;

import lombok.RequiredArgsConstructor;

import com.google.protobuf.ByteString;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class StoreFeatureVectorsResponse {

  private final Map<ByteString, Long> vectorSignatureIds;

  public long getVectorId(ByteString vectorSignature) {
    return Optional.ofNullable(vectorSignatureIds.get(vectorSignature))
        .orElseThrow(() -> new NoVectorWithSuchSignatureWasStoredException(vectorSignature));
  }

  private static final class NoVectorWithSuchSignatureWasStoredException extends RuntimeException {

    private static final long serialVersionUID = 3922886486076856017L;

    NoVectorWithSuchSignatureWasStoredException(ByteString signature) {
      super("Vector with signature " + signature.toString() + " was not stored.");
    }
  }
}
