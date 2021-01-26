package com.silenteight.serp.governance.analytics.featurevector;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.Signature;

import org.junit.jupiter.api.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class FeatureVectorServiceTest {

  private static final String FEATURE_1 = "nameAgent";
  private static final String VALUE_1 = "EXACT_MATCH";
  private static final String VALUE_2 = "NO_DATA";
  private static final Signature SIGNATURE_1 = new Signature(copyFromUtf8("signature_1"));
  private static final Signature SIGNATURE_2 = new Signature(copyFromUtf8("signature_2"));

  private final InMemoryAnalyticsFeatureVectorRepository repository =
      new InMemoryAnalyticsFeatureVectorRepository();

  private final FeatureVectorService underTest = new FeatureVectorService(repository);

  @Test
  void shouldStoreNewFeatureVector() {
    CanonicalFeatureVector vector = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1))
        .values(of(VALUE_1))
        .vectorSignature(SIGNATURE_1)
        .build();

    underTest.storeUniqueFeatureVector(vector);

    FeatureVector featureVector = repository.findByVectorSignature(SIGNATURE_1).orElseThrow();
    assertThat(featureVector.getNames()).isEqualTo(of(FEATURE_1));
    assertThat(featureVector.getValues()).isEqualTo(of(VALUE_1));
    assertThat(featureVector.getVectorSignature()).isEqualTo(SIGNATURE_1);
  }

  @Test
  void shouldStoreFeatureVectorOnlyOnce() {
    CanonicalFeatureVector vector = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1))
        .values(of(VALUE_1))
        .vectorSignature(SIGNATURE_1)
        .build();

    underTest.storeUniqueFeatureVector(vector);
    underTest.storeUniqueFeatureVector(vector);

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void shouldStoreFeaturesWithDistinctSignaturesSeparately() {
    CanonicalFeatureVector vector1 = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1))
        .values(of(VALUE_1))
        .vectorSignature(SIGNATURE_1)
        .build();

    CanonicalFeatureVector vector2 = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1))
        .values(of(VALUE_2))
        .vectorSignature(SIGNATURE_2)
        .build();

    underTest.storeUniqueFeatureVector(vector1);
    underTest.storeUniqueFeatureVector(vector2);

    assertThat(repository.count()).isEqualTo(2);
  }
}