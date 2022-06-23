package com.silenteight.serp.governance.vector.domain;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.Signature;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class FeatureVectorQueryTest {

  private static final String FEATURE_1 = "nameAgent";
  private static final String FEATURE_2 = "documentAgent";
  private static final String VALUE_1 = "EXACT_MATCH";
  private static final String VALUE_2 = "NO_DATA";
  private static final Signature SIGNATURE_1 = new Signature(copyFromUtf8("signature_1"));
  private static final Signature SIGNATURE_2 = new Signature(copyFromUtf8("signature_2"));
  private static final Signature SIGNATURE_3 = new Signature(copyFromUtf8("signature_3"));

  private final InMemoryAnalyticsFeatureVectorRepository repository =
      new InMemoryAnalyticsFeatureVectorRepository();
  private final FeatureVectorConfiguration configuration =
      new FeatureVectorConfiguration();

  private final FeatureVectorService service =
      configuration.featureVectorService(repository);

  private final FeatureVectorQuery underTest =
      configuration.featureVectorQuery(repository);

  @Test
  void shouldGetUniqueFeatureNames() {
    CanonicalFeatureVector vector1 = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1, FEATURE_2))
        .values(of(VALUE_1))
        .vectorSignature(SIGNATURE_1)
        .build();

    CanonicalFeatureVector vector2 = CanonicalFeatureVector.builder()
        .names(of(FEATURE_1))
        .values(of(VALUE_1))
        .vectorSignature(SIGNATURE_2)
        .build();

    CanonicalFeatureVector vector3 = CanonicalFeatureVector.builder()
        .names(of(FEATURE_2))
        .values(of(VALUE_2))
        .vectorSignature(SIGNATURE_3)
        .build();

    service.storeUniqueFeatureVector(vector1);
    service.storeUniqueFeatureVector(vector2);
    service.storeUniqueFeatureVector(vector3);

    List<String> uniqueFeatureNames = underTest.getUniqueFeatureNames();

    assertThat(uniqueFeatureNames).containsExactlyInAnyOrder(FEATURE_1, FEATURE_2);
  }
}
