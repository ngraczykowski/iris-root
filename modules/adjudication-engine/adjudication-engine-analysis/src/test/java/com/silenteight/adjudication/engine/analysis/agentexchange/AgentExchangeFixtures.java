package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.*;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AgentExchangeFixtures {

  private static final IntSupplier BASE_ID_SUPPLIER = () -> current().nextInt(1, 1000);
  private static final LongSupplier ALERT_ID_GENERATOR =
      LongStream.iterate(BASE_ID_SUPPLIER.getAsInt(), i -> i + 1).iterator()::nextLong;
  private static final LongSupplier MATCH_ID_GENERATOR =
      LongStream.iterate(BASE_ID_SUPPLIER.getAsInt(), i -> i + 1).iterator()::nextLong;
  private static final IntSupplier AGENT_ID_GENERATOR =
      IntStream.iterate(BASE_ID_SUPPLIER.getAsInt(), i -> i + 1).iterator()::nextInt;
  private static final IntSupplier FEATURE_ID_GENERATOR =
      IntStream.iterate(BASE_ID_SUPPLIER.getAsInt(), i -> i + 1).iterator()::nextInt;

  public static List<MissingMatchFeature> createRandomMissingMatchFeatures(
      int alertCount, int maxMatchesPerAlert, int agentCount, int maxFeaturesPerAgent) {

    var agents = IntStream.range(0, agentCount)
        .mapToObj(i -> dummyAgent(1 + current().nextInt(0, maxFeaturesPerAgent)))
        .collect(toList());

    var alerts = IntStream.range(0, alertCount)
        .mapToObj(i ->
            dummyAlert(1 + current().nextInt(0, maxMatchesPerAlert), current().nextInt(1, 11)))
        .collect(toList());

    return createMissingMatchFeatures(alerts, agents);
  }

  public static List<MissingMatchFeature> createMissingMatchFeatures(
      DummyAlert singleAlert, DummyAgent singleAgent) {

    return createMissingMatchFeatures(List.of(singleAlert), List.of(singleAgent));
  }

  public static List<MissingMatchFeature> createMissingMatchFeatures(
      List<DummyAlert> alerts, List<DummyAgent> agents) {

    return alerts.stream()
        .flatMap(alert -> alert.matchIds.stream().flatMap(matchId -> agents.stream()
            .flatMap(agent -> agent.features.stream().map(feature -> MissingMatchFeature.builder()
                .alertId(alert.alertId)
                .matchId(matchId)
                .agentConfig(agent.agentConfig)
                .feature(feature)
                .priority(alert.priority)
                .build()
            ))))
        .collect(toList());
  }

  public static DummyAlert dummyAlert(int matchCount, int priority) {
    return new DummyAlert(
        ALERT_ID_GENERATOR.getAsLong(),
        IntStream.range(0, matchCount)
            .mapToLong(i -> MATCH_ID_GENERATOR.getAsLong())
            .boxed()
            .collect(toList()),
        priority);
  }

  public static DummyAgent dummyAgent(int featureCount) {
    var agentId = AGENT_ID_GENERATOR.getAsInt();
    var configId = current().nextInt(1, 10);
    var features = IntStream.range(0, featureCount)
        .map(j -> FEATURE_ID_GENERATOR.getAsInt())
        .mapToObj(featureId -> "features/dummy_" + agentId + "_feature_" + featureId)
        .collect(toList());

    return new DummyAgent(
        "agents/dummy_" + agentId + "/versions/9.8.7/configs/" + configId, features);
  }

  @RequiredArgsConstructor
  @Builder
  @Getter
  static final class DummyAgent {

    private final String agentConfig;
    private final List<String> features;
  }

  @RequiredArgsConstructor
  @Builder
  @Getter
  static final class DummyAlert {

    private final long alertId;
    @Singular
    private final List<Long> matchIds;
    private final int priority;
  }
}
