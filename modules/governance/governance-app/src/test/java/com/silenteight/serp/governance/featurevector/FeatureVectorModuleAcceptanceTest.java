package com.silenteight.serp.governance.featurevector;

import lombok.experimental.UtilityClass;

import com.silenteight.proto.serp.v1.alert.FeatureGroupVector;
import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.sep.base.testing.spring.ApplicationEventPublisherSpy;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.randomSignature;
import static com.silenteight.serp.governance.featurevector.FeatureVectorModuleAcceptanceTest.ProtoMessageUtils.featureGroupVector;
import static com.silenteight.serp.governance.featurevector.FeatureVectorModuleAcceptanceTest.ProtoMessageUtils.vectorValue;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class FeatureVectorModuleAcceptanceTest {

  private FeatureVectorFinder finderUnderTest;

  private FeatureVectorService serviceUnderTest;

  private ApplicationEventPublisherSpy publisher = new ApplicationEventPublisherSpy();
  private TestInMemoryFeatureVectorRepository repository =
      new TestInMemoryFeatureVectorRepository();

  @BeforeEach
  void setUp() {
    var conf = new FeatureVectorConfiguration(repository);

    finderUnderTest = conf.featuresVectorFinder();
    serviceUnderTest = conf.featuresVectorService(publisher);
  }

  @Test
  void afterMultipleFeaturesVectorStored_canFindItByFeaturesSignature() {
    var featuresSignature = randomSignature();
    var vector1Signature = randomSignature();
    var vector2Signature = randomSignature();

    var request = StoreFeatureVectorsRequest.of(
        featuresSignature, asList(
            featureGroupVector(vector1Signature, vectorValue("a")),
            featureGroupVector(vector2Signature, vectorValue("b"))
        ));
    serviceUnderTest.store(request);

    var featureVectorSignatures = finderUnderTest.findSignatures(featuresSignature);

    assertThat(featureVectorSignatures).containsOnly(vector1Signature, vector2Signature);
  }

  @Test
  void afterStoringTwoSameVectors_finderReturnsOnlyOne() {
    var featuresSignature = randomSignature();
    var vectorSignature = randomSignature();

    var request = StoreFeatureVectorsRequest.of(
        featuresSignature, singletonList(featureGroupVector(vectorSignature, vectorValue("a"))));

    serviceUnderTest.store(request);
    serviceUnderTest.store(request);

    assertThat(finderUnderTest.findSignatures(featuresSignature)).hasSize(1);
  }

  @Test
  void storingReturnsMapWithGivenVectorSignatures() {
    var featuresSignature = randomSignature();
    var vector1Signature = randomSignature();
    var vector2Signature = randomSignature();

    var request = StoreFeatureVectorsRequest.of(
        featuresSignature, asList(
            featureGroupVector(vector1Signature, vectorValue("a")),
            featureGroupVector(vector2Signature, vectorValue("b"))
        ));

    var response = serviceUnderTest.store(request);

    assertThat(response.getVectorId(vector1Signature)).isNotNull();
    assertThat(response.getVectorId(vector2Signature)).isNotNull();
  }

  @UtilityClass
  static class ProtoMessageUtils {

    static VectorValue vectorValue(String textValue) {
      return VectorValue.newBuilder().setTextValue(textValue).build();
    }

    static FeatureGroupVector featureGroupVector(ByteString signature, VectorValue... values) {
      return FeatureGroupVector.newBuilder()
          .setVectorSignature(signature)
          .addAllValues(asList(values))
          .build();
    }
  }
}
