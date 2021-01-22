package com.silenteight.serp.governance.common.signature;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class SignatureCalculatorTest {

  private static final String FEATURE_1 = "nameAgent";
  private static final String FEATURE_2 = "dateAgent";
  private static final String VALUE_1 = "EXACT_MATCH";
  private static final String VALUE_2 = "NO_DATA";

  private final SignatureCalculator underTest = new SignatureCalculator();

  @Test
  void shouldCalculateFeaturesSignature() {
    ByteString featuresSignature = underTest.calculateFeaturesSignature(of(FEATURE_1, FEATURE_2));

    assertThat(featuresSignature).isEqualTo(ByteString.copyFrom(new byte[] {
        20, 126, 3, -90, -100, -95, -18, -69, -90, 114,
        -31, -21, -88, 75, 56, -120, 86, -58, 125, -51 }));
  }

  @Test
  void shouldCalculateDifferentFeaturesSignatureIfOrderIsDifferent() {
    ByteString featuresSignature1 = underTest.calculateFeaturesSignature(of(FEATURE_1, FEATURE_2));
    ByteString featuresSignature2 = underTest.calculateFeaturesSignature(of(FEATURE_2, FEATURE_1));

    assertThat(featuresSignature1).isNotEqualTo(featuresSignature2);
  }

  @Test
  void shouldCalculateVectorSignature() {
    ByteString featuresSignature = underTest.calculateFeaturesSignature(of(FEATURE_1, FEATURE_2));
    ByteString vectorSignature = underTest
        .calculateVectorSignature(featuresSignature, of(VALUE_1, VALUE_2));

    assertThat(vectorSignature).isEqualTo(ByteString.copyFrom(new byte[] {
        -47, 9, 0, 103, 111, -65, -85, 50, -30, 39,
        60, -121, -33, -18, -120, 80, -91, -1, 77, 88}));
  }

  @Test
  void shouldCalculateDifferentVectorSignatureIfFeatureSignaturesAreDifferent() {
    List<String> commonFeatureValues = of(VALUE_1, VALUE_2);
    ByteString featuresSignature1 = underTest.calculateFeaturesSignature(of(FEATURE_1, FEATURE_2));
    ByteString featuresSignature2 = underTest.calculateFeaturesSignature(of(FEATURE_2, FEATURE_1));

    ByteString vectorSignature1 =
        underTest.calculateVectorSignature(featuresSignature1, commonFeatureValues);
    ByteString vectorSignature2 =
        underTest.calculateVectorSignature(featuresSignature2, commonFeatureValues);

    assertThat(vectorSignature1).isNotEqualTo(vectorSignature2);
  }

  @Test
  void shouldCalculateDifferentVectorSignatureIfFeatureValuesAreDifferent() {
    ByteString commonFeaturesSignature = underTest
        .calculateFeaturesSignature(of(FEATURE_1, FEATURE_2));

    ByteString vectorSignature1 =
        underTest.calculateVectorSignature(commonFeaturesSignature, of(VALUE_1, VALUE_2));
    ByteString vectorSignature2 =
        underTest.calculateVectorSignature(commonFeaturesSignature, of(VALUE_2, VALUE_1));

    assertThat(vectorSignature1).isNotEqualTo(vectorSignature2);
  }
}