package com.silenteight.serp.governance.common.signature;

import com.google.protobuf.ByteString;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.List;

import static com.google.protobuf.ByteString.copyFrom;

@SuppressWarnings("findsecbugs:WEAK_MESSAGE_DIGEST_SHA1") // not used for cryptography
public class SignatureCalculator {

  public ByteString calculateFeaturesSignature(List<String> sortedFeatures) {
    return copyFrom(calculateSignatureForFeatures(sortedFeatures));
  }

  public ByteString calculateVectorSignature(
      ByteString featureSignature, List<String> featureValues) {
    return copyFrom(calculateSignatureForVector(featureSignature, featureValues));
  }

  private static byte[] calculateSignatureForFeatures(List<String> sortedFeatures) {
    MessageDigest digest = DigestUtils.getSha1Digest();

    sortedFeatures.forEach(feature -> digest.update(feature.getBytes()));

    return digest.digest();
  }

  private static byte[] calculateSignatureForVector(
      ByteString featureSignature, List<String> featureValues) {

    MessageDigest digest = DigestUtils.getSha1Digest();

    digest.update(featureSignature.asReadOnlyByteBuffer());
    featureValues.forEach(element -> digest.update(element.getBytes()));

    return digest.digest();
  }
}

