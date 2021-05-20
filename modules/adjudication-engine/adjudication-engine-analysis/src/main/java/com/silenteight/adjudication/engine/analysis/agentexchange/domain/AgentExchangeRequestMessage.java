package com.silenteight.adjudication.engine.analysis.agentexchange.domain;

import lombok.*;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import javax.annotation.Nonnull;

@Value
public class AgentExchangeRequestMessage {

  public static final int MIN_PRIORITY = 1;
  public static final int MAX_PRIORITY = 10;

  @NonNull
  UUID requestId;

  @NonNull
  String agentConfig;

  int priority;

  @NonNull
  List<String> features;

  @NonNull
  List<Match> matches;

  public int getMessageSize() {
    return getMatchCount() * features.size();
  }

  public int getMatchCount() {
    return matches.size();
  }

  public AgentExchangeRequest toRequest() {
    var builder = AgentExchangeRequest.newBuilder().addAllFeatures(features);

    matches.forEach(match -> builder.addMatches(match.getMatchName()));

    return builder.build();
  }

  public void forEachMatchId(LongConsumer consumer) {
    matches
        .stream()
        .mapToLong(Match::getMatchId)
        .forEach(consumer);
  }

  public void forEachFeature(Consumer<String> consumer) {
    features.forEach(consumer);
  }

  public static Builder builder() {
    return new Builder();
  }

  @RequiredArgsConstructor
  @Getter
  @ToString(onlyExplicitlyIncluded = true)
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  public static final class Match implements Comparable<Match> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final long alertId;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final long matchId;

    String getMatchName() {
      return "alerts/" + alertId + "/matches/" + matchId;
    }

    @Override
    public int compareTo(@Nonnull Match o) {
      var res = Long.compare(matchId, o.matchId);
      return (res != 0) ? res : Long.compare(alertId, o.alertId);
    }
  }

  @ToString
  public static final class Builder {

    private UUID requestId;
    private String agentConfig;
    private int priority;
    private List<String> features;
    private SortedSet<Match> matches;

    public Builder requestId(@NonNull UUID requestId) {
      this.requestId = requestId;
      return this;
    }

    public Builder agentConfig(@NonNull String agentConfig) {
      this.agentConfig = agentConfig;
      return this;
    }

    public Builder priority(int priority) {
      this.priority = Math.min(Math.max(MIN_PRIORITY, priority), MAX_PRIORITY);
      return this;
    }

    public Builder uniqueFeature(@NonNull String feature) {
      if (features == null) {
        features = new ArrayList<>();
      }

      if (!features.contains(feature)) {
        features.add(feature);
      }

      return this;
    }

    public Builder clearFeatures() {
      if (features != null) {
        features.clear();
      }

      return this;
    }

    public Builder match(long alertId, long matchId) {
      if (matches == null) {
        matches = new TreeSet<>();
      }

      matches.add(new Match(alertId, matchId));
      return this;
    }

    public Builder matches(
        @NonNull Collection<? extends Match> matches) {

      if (this.matches == null) {
        this.matches = new TreeSet<>();
      }

      this.matches.addAll(matches);
      return this;
    }

    public Builder clearMatches() {
      if (matches != null) {
        matches.clear();
      }

      return this;
    }

    public boolean isEmpty() {
      return matches == null || matches.isEmpty();
    }

    public int currentSize() {
      if (matches == null || features == null) {
        return 0;
      }

      return matches.size() * features.size();
    }

    public AgentExchangeRequestMessage build() {
      return new AgentExchangeRequestMessage(
          requestId, agentConfig, priority,
          features == null ? List.of() : List.copyOf(features),
          matches == null ? List.of() : List.copyOf(matches));
    }
  }
}
