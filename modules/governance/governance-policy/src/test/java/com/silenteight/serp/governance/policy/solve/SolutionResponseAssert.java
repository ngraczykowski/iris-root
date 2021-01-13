package com.silenteight.serp.governance.policy.solve;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.proto.governance.v1.api.SolutionResponse;

import org.assertj.core.api.AbstractAssert;

import java.util.UUID;
import javax.annotation.Nullable;

import static com.silenteight.governance.protocol.utils.Uuids.fromJavaUuid;

public class SolutionResponseAssert extends
    AbstractAssert<SolutionResponseAssert, SolutionResponse> {

  @Nullable
  private final GetSolutionsResponseAssert parent;

  SolutionResponseAssert(SolutionResponse solutionResponse) {
    this(solutionResponse, null);
  }

  SolutionResponseAssert(SolutionResponse solutionResponse, GetSolutionsResponseAssert parent) {
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

  public SolutionResponseAssert hasSolution(FeatureVectorSolution solution) {
    if (!actual.getFeatureVectorSolution().equals(solution)) {
      failWithMessage("Expected solution to be <%s>, but was <%s>",
          solution, actual.getFeatureVectorSolution());
    }

    return this;
  }
}
