package com.silenteight.adjudication.engine.analysis.agentexchange;


import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createFeatureRequestingStrategySupplier;
import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createMissingMatchFeatureChunk;
import static org.assertj.core.api.Assertions.*;

class AgentRequestHandlerTest {

  private List<AgentExchangeRequestMessage> messages = new ArrayList<>();
  private AgentExchangeRequestMessageRepository repository =
      new InMemoryAgentRequestMessageRepository();
  private final AgentRequestHandler agentRequestHandler = new AgentRequestHandler(
      createFeatureRequestingStrategySupplier(10), repository);
  private final ChunkHandler chunkHandler =
      agentRequestHandler.createChunkHandler(this::acceptProcessedChunk);

  @BeforeEach
  void setUp() {
    messages = new ArrayList<>();
    ((InMemoryAgentRequestMessageRepository) repository).clear();
  }

  @Test
  void shouldProcessMissingMatchFutureChunk() {
    var missingMatchFeatureChunk = handleChunk();
    chunkHandler.finished();
    assertThat(missingMatchFeatureChunk.getSize()).isEqualTo(matchesCount());
  }

  @Test
  void shouldProcessMultipleMissingMatchFutureChunk() {
    List<MissingMatchFeatureChunk> missingMatchFeatureChunks = new ArrayList<>();
    IntStream.range(1, 8).forEach((v) -> missingMatchFeatureChunks.add(handleChunk()));
    chunkHandler.finished();
    assertThat(missingMatchFeatureChunkCount(missingMatchFeatureChunks)).isEqualTo(matchesCount());
  }

  @Test
  void shouldSaveMissingMatchFutureChunk() {
    handleChunk();
    chunkHandler.finished();
    assertThat(((InMemoryAgentRequestMessageRepository) repository).getMatchesCount())
        .isEqualTo(matchesCount());
  }

  @Test
  void shouldSaveMultipleMissingMatchFutureChunk() {
    IntStream.range(1, 8).forEach((v) -> handleChunk());
    chunkHandler.finished();
    assertThat(((InMemoryAgentRequestMessageRepository) repository).getMatchesCount())
        .isEqualTo(matchesCount());
  }


  private MissingMatchFeatureChunk handleChunk() {
    var missingMatchFeatureChunk = createMissingMatchFeatureChunk(10);
    chunkHandler.handle(missingMatchFeatureChunk);
    return missingMatchFeatureChunk;
  }

  private void acceptProcessedChunk(List<AgentExchangeRequestMessage> messages) {
    this.messages.addAll(messages);
  }

  private int missingMatchFeatureChunkCount(
      List<MissingMatchFeatureChunk> missingMatchFeatureChunks) {
    return missingMatchFeatureChunks.stream().mapToInt(MissingMatchFeatureChunk::getSize).sum();
  }

  private int matchesCount() {
    return this.messages
        .stream().mapToInt(AgentExchangeRequestMessage::getMatchCount).sum();
  }
}
