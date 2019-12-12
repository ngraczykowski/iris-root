package com.silenteight.sens.webapp.backend.presentation.dto.workflow;

import lombok.*;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;

import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PostChangeRequest {

  long decisionTreeId;

  @NonNull
  @NotEmpty
  List<Long> matchGroupIds;

  @NonNull
  SolutionChange solution;

  @NonNull
  StatusChange status;

  @NonNull
  @NotBlank
  String comment;

  public ProposeChangesRequest createDomainRequest() {
    return ProposeChangesRequest
        .builder()
        .decisionTreeId(DecisionTreeId.of(decisionTreeId))
        .matchGroupIds(matchGroupIds)
        .solution(solution)
        .status(status)
        .comment(comment)
        .build();
  }

  @Builder
  private static class ProposeChangesRequest {

    private final DecisionTreeId decisionTreeId;
    private final List<Long> matchGroupIds;
    private final SolutionChange solution;
    private final StatusChange status;
    private final String comment;
  }

  private static class SolutionChange {

  }

  private static class StatusChange {

  }
}
