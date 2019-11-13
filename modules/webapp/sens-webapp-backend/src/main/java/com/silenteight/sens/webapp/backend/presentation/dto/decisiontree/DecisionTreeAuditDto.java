package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.common.audit.AuditDto;

import org.hibernate.envers.RevisionType;

import java.time.Instant;

@Data
@Builder
public class DecisionTreeAuditDto implements AuditDto {

  @NonNull
  private final Long decisionTreeId;
  @NonNull
  private final Long aiModelId;
  @NonNull
  private final String aiModelName;
  @NonNull
  private final String decisionTreeName;
  private final String assignedBatchTypes;
  private final String activatedBatchTypes;
  @NonNull
  private final RevisionType revisionType;
  @NonNull
  private final Instant auditedAt;
  private final String userName;
}
