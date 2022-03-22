package com.silenteight.agent.facade.exchange;

import lombok.Value;

import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import java.util.Optional;

import static com.silenteight.agent.facade.AgentErrorHandler.ERROR_MESSAGE_FIELD;
import static org.assertj.core.api.Assertions.*;

@Value
public class AgentExchangeResponseAssert {

  AgentExchangeResponse response;

  public AgentExchangeResponseAssert hasOutputSize(int size) {
    var agentOutputsList = response.getAgentOutputsList();
    assertThat(agentOutputsList).hasSize(size);
    return this;
  }

  public AgentOutputAssert hasSingleOutput() {
    var agentOutputsList = response.getAgentOutputsList();
    assertThat(agentOutputsList).hasSize(1);
    return new AgentOutputAssert(agentOutputsList.get(0));
  }

  public AgentOutputAssert getOutput(int index) {
    return new AgentOutputAssert(response.getAgentOutputsList().get(index));
  }

  public AgentOutputAssert getOutputForMatch(String match) {
    Optional<AgentOutput> outputForMatch = response
        .getAgentOutputsList()
        .stream()
        .filter(agentOutput -> agentOutput.getMatch().equals(match))
        .findFirst();

    assertThat(outputForMatch).isPresent();
    return new AgentOutputAssert(outputForMatch.get());
  }

  @Value
  public static class AgentOutputAssert {

    AgentOutput agentOutput;

    public AgentOutputAssert withMatch(String match) {
      assertThat(agentOutput.getMatch()).isEqualTo(match);
      return this;
    }

    public FeatureAssert hasSingleFeature() {
      var featuresList = agentOutput.getFeaturesList();
      assertThat(featuresList).hasSize(1);
      return new FeatureAssert(featuresList.get(0));
    }
  }

  @Value
  public static class FeatureAssert {

    Feature feature;

    public FeatureAssert withFeature(String string) {
      assertThat(feature.getFeature()).isEqualTo(string);
      return this;
    }

    public FeatureSolutionAssert hasSingleFeatureSolution() {
      var featureSolutionsList = feature.getFeatureSolution();
      return new FeatureSolutionAssert(featureSolutionsList);
    }
  }

  @Value
  public static class FeatureSolutionAssert {

    FeatureSolution featureSolution;

    public FeatureSolutionAssert withSolution(String solution) {
      assertThat(featureSolution.getSolution()).isEqualTo(solution);
      return this;
    }

    public FeatureSolutionAssert withErrorMessage(String message) {
      String errorMessage = getErrorMessage();
      assertThat(errorMessage).isEqualTo(message);
      return this;
    }

    public FeatureSolutionAssert withNoErrorMessage() {
      var errorMessage = featureSolution.getReason().getFieldsMap().get(ERROR_MESSAGE_FIELD);
      assertThat(errorMessage).isNull();
      return this;
    }

    public FeatureSolutionAssert withErrorMessageMatchesPhrase(String phrase) {
      String errorMessage = getErrorMessage();
      assertThat(errorMessage).matches(phrase);
      return this;
    }

    private String getErrorMessage() {
      return featureSolution.getReason().getFieldsMap().get(ERROR_MESSAGE_FIELD).getStringValue();
    }
  }
}
