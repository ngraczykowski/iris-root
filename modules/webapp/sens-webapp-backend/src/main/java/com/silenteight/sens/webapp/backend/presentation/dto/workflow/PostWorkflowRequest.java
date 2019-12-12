package com.silenteight.sens.webapp.backend.presentation.dto.workflow;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;

import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotNull;

@Value
@Validated
public class PostWorkflowRequest {

  @NotNull
  List<String> makers;

  @NotNull
  List<ApproversView> approvalLevels;

  public ConfigureWorkflowRequest createDomainRequest(long decisionTreeId) {
    return new ConfigureWorkflowRequest(DecisionTreeId.of(decisionTreeId), makers, approvalLevels);
  }

  private static class ApproversView {

  }

  @RequiredArgsConstructor
  private static class ConfigureWorkflowRequest {

    private final DecisionTreeId decisionTreeId;
    private final List<String> makers;
    private final List<ApproversView> approvalLevels;
  }
}
