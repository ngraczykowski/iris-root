package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
class AgentRequestHandler {

  private final Supplier<FeatureRequestingStrategy> featureRequestingStrategySupplier;
  private final AgentExchangeRequestMessageRepository repository;

  ChunkHandler createChunkHandler(MessageConsumer messageConsumer) {
    return new MessageSendingChunkHandler(featureRequestingStrategySupplier.get(), messageConsumer);
  }

  public interface MessageConsumer extends Consumer<List<AgentExchangeRequestMessage>> {
  }

  @RequiredArgsConstructor
  private final class MessageSendingChunkHandler implements ChunkHandler {

    private final FeatureRequestingStrategy featureRequestingStrategy;
    private final Consumer<List<AgentExchangeRequestMessage>> messageConsumer;

    private final List<AgentExchangeRequestMessage> buffer = new ArrayList<>();

    @Override
    public void handle(MissingMatchFeatureChunk chunk) {
      featureRequestingStrategy.createRequests(chunk, buffer::add);
      saveAndConsumeBufferedMessages();
    }

    @Override
    public void finished() {
      featureRequestingStrategy.flush(buffer::add);
      saveAndConsumeBufferedMessages();
    }

    private void saveAndConsumeBufferedMessages() {
      repository.saveAll(buffer);
      messageConsumer.accept(List.copyOf(buffer));
      buffer.clear();
    }
  }
}
