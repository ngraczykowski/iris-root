package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createMissingMatchFeatures;
import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.dummyAgent;
import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.dummyAlert;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class DefaultFeatureRequestingStrategyTest {

  private DefaultFeatureRequestingStrategy strategy = new DefaultFeatureRequestingStrategy(10);
  private MissingMatchFeatureChunk chunk;

  @Test
  void givenChunkIsEmpty_generatesNoMessages() {
    givenChunk(emptyList());

    var messages = whenGenerate();

    assertThat(messages).isEmpty();
  }

  @Test
  void givenSingleFeatureInChunk_generatesSingleMessage() {
    givenChunk(createMissingMatchFeatures(dummyAlert(1, 1), dummyAgent(1)));

    var messages = whenGenerate();

    assertThat(messages).hasSize(1).first().satisfies(m -> {
      assertThat(m.getMessageSize()).isEqualTo(1);
    });
  }

  @Test
  void givenTwoMatchesInChunk_generatesSingleMessage() {
    givenChunk(createMissingMatchFeatures(dummyAlert(2, 1), dummyAgent(1)));

    var messages = whenGenerate();

    assertThat(messages).hasSize(1);
  }

  @Test
  void givenThreeMatchesInChunkAndMaxTwoMessages_generatesTwoMessages() {
    givenStrategy(2);
    var agent = dummyAgent(1);
    var alert = dummyAlert(3, 1);
    givenChunk(createMissingMatchFeatures(alert, agent));

    var messages = whenGenerate();

    assertThat(messages)
        .hasSize(2)
        .allSatisfy(m ->
            assertThat(m.getFeatures()).hasSize(1).containsExactlyElementsOf(agent.getFeatures()))
        .extracting(AgentExchangeRequestMessage::getMatchCount)
        .containsExactly(2, 1);
  }

  @Test
  void givenTwoFeatures_doesNotDuplicateMatches() {
    var agent = dummyAgent(2);
    var alert = dummyAlert(1, 1);
    givenChunk(createMissingMatchFeatures(alert, agent));

    var messages = whenGenerate();

    assertThat(messages)
        .extracting(AgentExchangeRequestMessage::getMatchCount)
        .containsExactly(1);
  }

  @Nonnull
  private List<AgentExchangeRequestMessage> whenGenerate() {
    var result = new ArrayList<AgentExchangeRequestMessage>();
    strategy.createRequests(chunk, result::add);
    strategy.flush(result::add);
    return result;
  }

  private void givenChunk(List<MissingMatchFeature> missingMatchFeatures) {
    chunk = new MissingMatchFeatureChunk(missingMatchFeatures);
  }

  private void givenStrategy(int maxMessageSize) {
    strategy = new DefaultFeatureRequestingStrategy(maxMessageSize);
  }
}
