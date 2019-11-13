package com.silenteight.sens.webapp.backend.presentation.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
    return new ProposeChangesRequest();
//    return ProposeChangesRequest
//        .builder()
//        .decisionTreeId(DecisionTreeId.of(decisionTreeId))
//        .matchGroupIds(matchGroupIds)
//        .solution(solution)
//        .status(status)
//        .comment(comment)
//        .build();
  }

  private static class ProposeChangesRequest {

  }

  private static class SolutionChange {

  }

  private static class StatusChange {

  }
}
