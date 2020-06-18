package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.silenteight.sens.webapp.backend.reasoningbranch.dto.ReasoningBranchIdDto;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class BulkChangeIdsForReasoningBranchDto {

  private ReasoningBranchIdDto reasoningBranchId;
  private List<UUID> bulkChangeIds;
}
