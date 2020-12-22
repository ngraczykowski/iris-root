package com.silenteight.serp.governance.featureset;

import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.sep.base.common.protocol.ByteStringUtils;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.featureset.ReasoningBranchFeaturesQueryRepositoryIT.ReasoningBranchFeaturesQueryRepositoryTestConfiguration;
import com.silenteight.serp.governance.featureset.dto.FeatureSetToStoreDto;
import com.silenteight.serp.governance.featureset.dto.StoreFeatureSetRequest;
import com.silenteight.serp.governance.featurevector.FeatureVectorModule;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest.Vector;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsResponse;

import com.google.protobuf.ByteString;
import org.assertj.core.api.AbstractOptionalAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { ReasoningBranchFeaturesQueryRepositoryTestConfiguration.class })
class ReasoningBranchFeaturesQueryRepositoryIT extends BaseDataJpaTest {

  static final ByteString FEATURES_1 = ByteString.copyFromUtf8("f1");
  static final ByteString FEATURES_2 = ByteString.copyFromUtf8("f2");

  @Autowired
  private FeatureSetRepository featureSetRepository;
  @Autowired
  private FeatureSetService featureSetService;
  @Autowired
  private FeatureVectorService featureVectorService;
  @Autowired
  private ReasoningBranchFeaturesQueryRepository underTest;

  @Test
  void noFeatureSets_emptyFeatures() {
    assertBranchFeatures(1L).isEmpty();
  }

  @Test
  void noVectors_emptyFeatures() {
    storeFeatures(FEATURES_1, "f1", "f2", "f3");

    assertBranchFeatures(1L).isEmpty();
  }

  @Test
  void vectorStored_foundVectorFeatures() {
    long expectedFeatureSetId = storeFeatures(FEATURES_1, "f1", "f2", "f3");
    long id = storeVector(FEATURES_1, "v1", "v2", "v3");

    Optional<ReasoningBranchFeaturesQuery> rb = underTest.findByFeatureVectorId(id);
    assertReasoningBranchFeaturesQuery(rb, expectedFeatureSetId, asList("f1", "f2", "f3"));
  }

  @Test
  void multipleFeaturesAndVectorsStored_foundVectorFeatures() {
    long expectedFeatureSetId1 = storeFeatures(FEATURES_1, "f1", "f2");
    long expectedFeatureSetId2 = storeFeatures(FEATURES_2, "f1", "f2", "f3");
    long id1 = storeVector(FEATURES_1, "v1", "v2");
    long id2 = storeVector(FEATURES_2, "v1", "v2", "v3");

    Optional<ReasoningBranchFeaturesQuery> rb1 = underTest.findByFeatureVectorId(id1);
    assertReasoningBranchFeaturesQuery(rb1, expectedFeatureSetId1, asList("f1", "f2"));

    Optional<ReasoningBranchFeaturesQuery> rb2 = underTest.findByFeatureVectorId(id2);
    assertReasoningBranchFeaturesQuery(rb2, expectedFeatureSetId2, asList("f1", "f2", "f3"));
  }

  private void assertReasoningBranchFeaturesQuery(Optional<ReasoningBranchFeaturesQuery> rb,
      long expectedFeatureSetId, List<String> expectedFeatures) {
    assertThat(rb).map(ReasoningBranchFeaturesQuery::getFeatures)
        .hasValue(expectedFeatures);
    assertThat(rb).map(ReasoningBranchFeaturesQuery::getFeatureSetId)
        .hasValue(expectedFeatureSetId);
  }

  private long storeVector(ByteString signature, String... values) {
    Vector vector = createVector(values);

    StoreFeatureVectorsResponse response = featureVectorService.store(
        new StoreFeatureVectorsRequest(signature, singletonList(vector)));

    return response.getVectorId(vector.getVectorSignature());
  }

  private static Vector createVector(String... values) {
    ByteString vectorSignature = ByteString.copyFromUtf8(String.join(";", values));
    return new Vector(vectorSignature, stream(values)
        .map(v -> VectorValue.newBuilder().setTextValue(v).build())
        .collect(Collectors.toList()));
  }

  private long storeFeatures(ByteString signature, String... values) {
    FeatureSetToStoreDto featureSet = new FeatureSetToStoreDto(signature, asList(values));
    featureSetService.storeFeatureSet(new StoreFeatureSetRequest(singleton(featureSet)));
    return featureSetRepository.findByFeaturesSignature(ByteStringUtils.toBase64String(signature))
        .get().getId();
  }

  private AbstractOptionalAssert<?, List<String>> assertBranchFeatures(long id) {
    return assertThat(underTest.findByFeatureVectorId(id))
        .map(ReasoningBranchFeaturesQuery::getFeatures);
  }

  @Configuration
  @ComponentScan(basePackageClasses = {
      FeatureSetModule.class,
      FeatureVectorModule.class
  })
  public static class ReasoningBranchFeaturesQueryRepositoryTestConfiguration {

  }
}
