package com.silenteight.adjudication.engine.mock.agents;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Pattern;

@Value
@Builder
class FeatureSolutionProvider {

  private static final SecureRandom RANDOM = new SecureRandom();

  Pattern pattern;
  @Singular
  List<FeatureSolution> solutions;

  boolean matches(String feature) {
    return pattern.matcher(feature).matches();
  }

  FeatureSolution getRandom() {
    return solutions.get(RANDOM.nextInt(solutions.size()));
  }
}
