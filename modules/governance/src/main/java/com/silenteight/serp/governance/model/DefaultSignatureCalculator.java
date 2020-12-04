package com.silenteight.serp.governance.model;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.List;

//TODO: if moved to common please replace it and remove
@SuppressWarnings("findsecbugs:WEAK_MESSAGE_DIGEST_SHA1") // not used for cryptography
public class DefaultSignatureCalculator implements SignatureCalculator {

  @Override
  public ByteString calculateFeaturesSignature(List<String> sortedFeatures) {
    return ByteString.copyFrom(calculateSignatureForFeatures(sortedFeatures));
  }

  @Override
  public ByteString calculateVectorSignature(
      ByteString featureSignature, List<String> featureValues) {
    return ByteString.copyFrom(calculateSignatureForVector(featureSignature, featureValues));
  }

  private static byte[] calculateSignatureForFeatures(List<String> sortedFeatures) {
    MessageDigest digest = DigestUtils.getSha1Digest();

    sortedFeatures.forEach(feature -> digest.update(feature.getBytes()));

    return digest.digest();
  }

  private byte[] calculateSignatureForVector(
      ByteString featureSignature, List<String> featureValues) {

    MessageDigest digest = DigestUtils.getSha1Digest();

    digest.update(featureSignature.asReadOnlyByteBuffer());
    featureValues.forEach(element -> digest.update(element.getBytes()));

    return digest.digest();
  }
}

