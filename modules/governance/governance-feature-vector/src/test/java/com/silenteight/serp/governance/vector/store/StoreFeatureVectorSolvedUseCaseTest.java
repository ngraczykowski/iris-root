package com.silenteight.serp.governance.vector.store;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.vector.VectorTestConfiguration;
import com.silenteight.serp.governance.vector.domain.FeatureVectorService;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.vector.list.FeatureVectorUsageQuery;
import com.silenteight.serp.governance.vector.usage.domain.UsageService;
import com.silenteight.solving.api.v1.Feature;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { VectorTestConfiguration.class })
class StoreFeatureVectorSolvedUseCaseTest extends BaseDataJpaTest {

  private static final String FEATURE_NAME = "nameAgent";
  private static final String FEATURE_VALUE = "EXACT_MATCH";
  private static final String FEATURE_VALUE_NO_DATA = "NO_DATA";

  @Autowired
  StoreFeatureVectorSolvedUseCase underTest;

  @Autowired
  CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;

  @Autowired
  UsageService usageService;

  @Autowired
  FeatureVectorService featureVectorService;

  @Autowired
  FeatureVectorUsageQuery featureVectorQuery;

  @Test
  void shouldStoreFeatureVector() {
    FeatureVectorSolvedEvent event = getFeatureVectorSolvedEvent(FEATURE_VALUE);

    underTest.activate(singletonList(event));

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromNamesAndValues(of(FEATURE_NAME), of(FEATURE_VALUE));
    List<FeatureVectorWithUsageDto> vectors = featureVectorQuery
        .getAllWithUsage()
        .collect(toList());
    assertThat(vectors)
        .extracting(FeatureVectorWithUsageDto::getSignature)
        .containsExactly(canonicalFeatureVector.vectorSignatureAsString());
    assertThat(vectors)
        .extracting(FeatureVectorWithUsageDto::getNames)
        .containsExactly(of(FEATURE_NAME));
    assertThat(vectors)
        .extracting(FeatureVectorWithUsageDto::getValues)
        .containsExactly(of(FEATURE_VALUE));
  }

  @Test
  void shouldCountFeatureVectorMatches() {
    FeatureVectorSolvedEvent event = getFeatureVectorSolvedEvent(FEATURE_VALUE);

    underTest.activate(singletonList(event));
    underTest.activate(singletonList(event));

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromNamesAndValues(of(FEATURE_NAME), of(FEATURE_VALUE));
    assertThat(usageService.getUsageCount(canonicalFeatureVector)).isEqualTo(2);
  }

  @Test
  void shouldCountFeatureVectorMatchesInBatch() {
    FeatureVectorSolvedEvent event = getFeatureVectorSolvedEvent(FEATURE_VALUE);

    underTest.activate(asList(event, event));

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromNamesAndValues(of(FEATURE_NAME), of(FEATURE_VALUE));
    assertThat(usageService.getUsageCount(canonicalFeatureVector)).isEqualTo(2);
  }

  @Test
  void shouldBeThreadSafe() {
    FeatureVectorSolvedEvent event = getFeatureVectorSolvedEvent(FEATURE_VALUE_NO_DATA);

    callInParallel(() -> underTest.activate(singletonList(event)));

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromNamesAndValues(of(FEATURE_NAME), of(FEATURE_VALUE_NO_DATA));
    assertThat(usageService.getUsageCount(canonicalFeatureVector)).isEqualTo(2);
  }

  private static FeatureVectorSolvedEvent getFeatureVectorSolvedEvent(String featureValue) {
    return FeatureVectorSolvedEvent
        .newBuilder()
        .setFeatureCollection(featureCollection(FEATURE_NAME))
        .setFeatureVector(featureVector(featureValue))
        .build();
  }

  private static FeatureCollection featureCollection(String... featureNames) {
    List<Feature> features = Arrays
        .stream(featureNames)
        .map(name -> Feature.newBuilder().setName(name).build())
        .collect(toList());

    return FeatureCollection
        .newBuilder()
        .addAllFeature(features)
        .build();
  }

  private static FeatureVector featureVector(String... featureValues) {
    return FeatureVector
        .newBuilder()
        .addAllFeatureValue(asList(featureValues))
        .build();
  }

  private void callInParallel(Runnable runnable) {
    int numberOfParallelThreads = 2;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfParallelThreads);

    try {
      IntStream.range(0, numberOfParallelThreads)
          .parallel()
          .boxed()
          .map(threadNo -> executorService.submit(runnable))
          .forEach(future -> {
            try {
              future.get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });
    } finally {
      executorService.shutdown();
    }
  }
}
