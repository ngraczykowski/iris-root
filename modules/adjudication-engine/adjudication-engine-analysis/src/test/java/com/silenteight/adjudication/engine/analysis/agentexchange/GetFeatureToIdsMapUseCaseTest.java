package com.silenteight.adjudication.engine.analysis.agentexchange;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.LongSupplier;
import java.util.stream.LongStream;
import javax.annotation.Nonnull;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.*;

class GetFeatureToIdsMapUseCaseTest {

  private static final LongSupplier BASE_ID_SUPPLIER = () -> current().nextLong(1, 1000);
  private static final LongSupplier FEATURE_ID_GENERATOR =
      LongStream.iterate(BASE_ID_SUPPLIER.getAsLong(), i -> i + 1).iterator()::nextLong;
  private static final LongSupplier AGENT_CONFIG_FEATURE_ID_GENERATOR =
      LongStream.iterate(BASE_ID_SUPPLIER.getAsLong(), i -> i + 1).iterator()::nextLong;

  protected static final String FEATURE_TEST_1 = "features/test-1";
  protected static final String FEATURE_TEST_2 = "features/test-2";

  private final InMemoryAgentExchangeFeatureQueryRepository repository =
      new InMemoryAgentExchangeFeatureQueryRepository();

  private final GetFeatureToIdsMapUseCase useCase = new GetFeatureToIdsMapUseCase(repository);

  @Test
  void givenEmptyRepository_shouldReturnEmptyMap() {
    var featureIds = useCase.getFeatureToIdsMap(UUID.randomUUID());

    assertThat(featureIds).isEmpty();
  }

  @Test
  void givenNonExistentRequest_shouldReturnEmptyMap() {
    var dummyRequestId =  UUID.randomUUID();
    givenFeature(FEATURE_TEST_1, UUID.randomUUID());

    var featureIds = useCase.getFeatureToIdsMap(dummyRequestId);

    assertThat(featureIds).isEmpty();
  }

  @Test
  void givenFeatures_shouldReturnMapWithAll() {
    var requestId = UUID.randomUUID();
    var feature1 = givenFeature(FEATURE_TEST_1, requestId);
    var feature2 = givenFeature(FEATURE_TEST_2, requestId);

    var featureIds = useCase.getFeatureToIdsMap(requestId);

    assertThat(featureIds)
        .containsOnlyKeys(FEATURE_TEST_1, FEATURE_TEST_2)
        .containsValues(feature1.getAgentConfigFeatureId(), feature2.getAgentConfigFeatureId());
  }

  @Nonnull
  private AgentExchangeFeatureQuery givenFeature(String featureName, UUID requestId) {
    var feature = new AgentExchangeFeatureQuery();
    feature.setFeature(featureName);
    feature.setId(FEATURE_ID_GENERATOR.getAsLong());
    feature.setAgentConfigFeatureId(AGENT_CONFIG_FEATURE_ID_GENERATOR.getAsLong());
    feature.setRequestId(requestId);
    repository.save(feature);
    return feature;
  }
}
