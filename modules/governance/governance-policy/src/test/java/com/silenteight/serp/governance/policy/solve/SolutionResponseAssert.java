package com.silenteight.serp.governance.policy.solve;

import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.governance.api.v1.SolutionResponse;
import com.silenteight.serp.governance.common.signature.Signature;

import org.assertj.core.api.AbstractAssert;

import java.util.UUID;
import javax.annotation.Nullable;

import static com.silenteight.governance.api.utils.Uuids.fromJavaUuid;

public class SolutionResponseAssert extends
    AbstractAssert<SolutionResponseAssert, SolutionResponse> {

  @Nullable
  private final SolveFeaturesResponseAssert parent;

  SolutionResponseAssert(SolutionResponse solutionResponse) {
    this(solutionResponse, null);
  }

  SolutionResponseAssert(SolutionResponse solutionResponse, SolveFeaturesResponseAssert parent) {
    super(solutionResponse, SolutionResponseAssert.class);
    this.parent = parent;
  }

  public static SolutionResponseAssert assertThat(SolutionResponse actual) {
    return new SolutionResponseAssert(actual);
  }

  public SolutionResponseAssert hasStepId(UUID stepId) {
    if (!actual.getStepId().equals(fromJavaUuid(stepId))) {
      failWithMessage("Expected stepId to be <%s>, but was <%s>",
          stepId, actual.getStepId());
    }

    return this;
  }

  public SolutionResponseAssert hasNoStepId() {
    if (actual.hasStepId()) {
      failWithMessage("Expected stepId to be null, but was <%s>", actual.getStepId());
    }

    return this;
  }

  public SolutionResponseAssert hasSolution(FeatureVectorSolution solution) {
    if (!actual.getFeatureVectorSolution().equals(solution)) {
      failWithMessage("Expected solution to be <%s>, but was <%s>",
          solution, actual.getFeatureVectorSolution());
    }

    return this;
  }

  public SolutionResponseAssert hasVectorSignature(Signature signature) {
    if (!actual.getFeatureVectorSignature().equals(signature.getValue())) {
      failWithMessage("Expected signature to be <%s>, but was <%s>",
          signature, new Signature(actual.getFeatureVectorSignature()));
    }

    return this;
  }

  public SolveFeaturesResponseAssert and() {
    return parent;
  }
}
