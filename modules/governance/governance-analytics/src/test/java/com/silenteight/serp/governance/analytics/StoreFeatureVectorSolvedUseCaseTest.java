package com.silenteight.serp.governance.analytics;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.analytics.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.analytics.usage.UsageService;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { AnalyticsTestConfiguration.class })
class StoreFeatureVectorSolvedUseCaseTest extends BaseDataJpaTest {

  private static final String FEATURE_NAME_1 = "nameAgent";
  private static final String FEATURE_VALUE_1 = "EXACT_MATCH";

  @Autowired
  StoreFeatureVectorSolvedUseCase underTest;

  @Autowired
  CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;

  @Autowired
  UsageService usageService;

  @Autowired
  FeatureVectorService featureVectorService;

  @Test
  void shouldStoreFeatureVector() {
    Map<String, String> featureValuesByName = Map.of(FEATURE_NAME_1, FEATURE_VALUE_1);

    underTest.activate(featureValuesByName, randomUUID());

    assertThat(featureVectorService.getFeatureVectorStream())
        .containsExactly(new FeatureVectorDto(of(FEATURE_NAME_1), of(FEATURE_VALUE_1)));
  }

  @Test
  void shouldCountFeatureVectorMatches() {
    Map<String, String> featureValuesByName = Map.of(FEATURE_NAME_1, FEATURE_VALUE_1);

    underTest.activate(featureValuesByName, randomUUID());
    underTest.activate(featureValuesByName, randomUUID());

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromMap(featureValuesByName);
    assertThat(usageService.getUsageCount(canonicalFeatureVector)).isEqualTo(2);
  }
}