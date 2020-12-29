package com.silenteight.serp.governance.policy.solve;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_NO_DECISION;

@RequiredArgsConstructor
@Slf4j
public class SolvingService {

  private static final String DEFAULT_STEP_NAME = "default";
  private static final SolutionWithStepId DEFAULT_SOLUTION =
      new SolutionWithStepId(DEFAULT_STEP_NAME, SOLUTION_NO_DECISION);

  private final StepPolicyFactory stepPolicyFactory;

  public SolutionWithStepId getSolution(List<String> values) {
    SolutionWithStepId solutionWithStepId = getSolutionForValues(values);
    //TODO(mmastylo): Send Configure Branch message to a queue
    return solutionWithStepId;
  }

  private SolutionWithStepId getSolutionForValues(List<String> values) {
    return stepPolicyFactory
        .getSteps()
        .stream()
        .filter(step -> step.matchValues(values))
        .map(Step::getSolutionWithStepId)
        .peek(step -> log.debug("Using step: {}", step.getStepId()))
        .findFirst()
        .orElse(DEFAULT_SOLUTION);
  }
}
