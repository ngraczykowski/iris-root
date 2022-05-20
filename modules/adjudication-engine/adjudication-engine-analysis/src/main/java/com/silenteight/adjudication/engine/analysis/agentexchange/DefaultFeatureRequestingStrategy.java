package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage.Builder;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import com.google.common.base.Preconditions;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.concurrent.NotThreadSafe;

import static com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage.MIN_PRIORITY;

/**
 * @implNote For optimal performance, this implementation does not group matches by features.
 *     This might result in requesting more match feature values than necessary, BUT given current
 *     implementation of {@link RequestMissingFeatureValuesUseCase} the features are iterated one
 *     analysis at a time, and an analysis has a constant set of features.
 */
@NotThreadSafe
@Slf4j
class DefaultFeatureRequestingStrategy implements FeatureRequestingStrategy {

  private final Map<String, CountingBuilder> messageBuilders;

  @Getter
  private final int maxMessageSize;

  // NOTE(ahaczewski): This generator uses fast Random generator with SecureRandom seed,
  //  in contrast to UUID.randomUUID() which uses SecureRandom for each generation.
  @Setter
  private IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  DefaultFeatureRequestingStrategy(int maxMessageSize) {
    Preconditions.checkArgument(
        maxMessageSize > 0, "Max message size %s must be greater than 0.", maxMessageSize);

    this.maxMessageSize = maxMessageSize;

    messageBuilders = new TreeMap<>(String::compareTo);
  }

  @Override
  public void createRequests(
      MissingMatchFeatureChunk chunk, Consumer<AgentExchangeRequestMessage> messageConsumer) {

    if (log.isDebugEnabled()) {
      log.debug(
          "Creating agent requests for missing feature values: missingValueCount={}",
          chunk.getSize());
    }

    chunk.forEach(missing -> {
      var builder = getBuilder(missing.getAgentConfig())
          .feature(missing.getFeature(), missing.getAgentConfigFeatureId())
          .match(missing.getAlertId(), missing.getMatchId())
          .priority(missing.getPriority());

      if (builder.isReady(maxMessageSize)) {
        messageConsumer.accept(builder.build());
        builder.reset(idGenerator.generateId());
      }
    });
  }

  private CountingBuilder getBuilder(String agentConfig) {
    return messageBuilders.computeIfAbsent(
        agentConfig, s -> new CountingBuilder(idGenerator.generateId(), agentConfig));
  }

  @Override
  public void flush(Consumer<AgentExchangeRequestMessage> messageConsumer) {
    if (messageBuilders.isEmpty()) {
      return;
    }

    messageBuilders.forEach((agentConfig, builder) -> {
      if (!builder.isEmpty()) {
        messageConsumer.accept(builder.build());
      }
    });
    messageBuilders.clear();
  }

  private static final class CountingBuilder {

    private final Builder builder;

    private int currentPriority = MIN_PRIORITY;

    CountingBuilder(UUID requestId, String agentConfig) {
      builder = AgentExchangeRequestMessage.builder()
          .requestId(requestId)
          .agentConfig(agentConfig);
    }

    CountingBuilder feature(String feature, long agentConfigFeatureId) {
      builder.uniqueFeature(feature, agentConfigFeatureId);
      return this;
    }

    CountingBuilder match(long alertId, long matchId) {
      builder.match(alertId, matchId);
      return this;
    }

    CountingBuilder priority(int priority) {
      currentPriority = Math.max(currentPriority, priority);
      return this;
    }

    boolean isEmpty() {
      return builder.isEmpty();
    }

    boolean isReady(int maxMessageSize) {
      return builder.currentSize() >= maxMessageSize;
    }

    void reset(UUID requestId) {
      builder.requestId(requestId).clearFeatures().clearMatches();
      currentPriority = 1;
    }

    AgentExchangeRequestMessage build() {
      return builder.priority(currentPriority).build();
    }
  }
}
