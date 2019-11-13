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
public class PostRejectChangeRequest {

  long decisionTreeId;

  @NonNull
  @NotEmpty
  List<Long> matchGroupIds;

  @NonNull
  @NotBlank
  String comment;

  public RejectChangeRequest createDomainRequest() {
    //return new RejectChangeRequest(DecisionTreeId.of(decisionTreeId), matchGroupIds, comment);
    return new RejectChangeRequest();
  }

  private static class RejectChangeRequest {

  }
}
