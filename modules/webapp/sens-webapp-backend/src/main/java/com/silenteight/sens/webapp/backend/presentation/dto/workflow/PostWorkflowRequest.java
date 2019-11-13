package com.silenteight.sens.webapp.backend.presentation.dto.workflow;

import lombok.Value;

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
    return new ConfigureWorkflowRequest();
    //return new ConfigureWorkflowRequest(DecisionTreeId.of(decisionTreeId), makers, approvalLevels);
  }

  private static class ApproversView {

  }

  private static class ConfigureWorkflowRequest {

  }
}
