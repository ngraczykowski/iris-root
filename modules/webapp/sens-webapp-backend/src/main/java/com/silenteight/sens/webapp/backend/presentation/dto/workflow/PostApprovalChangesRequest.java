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
public class PostApprovalChangesRequest {

  @NonNull
  @NotEmpty
  List<Long> changeIds;

  @NonNull
  @NotBlank
  String comment;

  public BulkChangesApprovalRequest createDomainRequest() {
    //return new BulkChangesApprovalRequest(changeIds, comment);
    return new BulkChangesApprovalRequest();
  }

  private static class BulkChangesApprovalRequest {

  }
}
