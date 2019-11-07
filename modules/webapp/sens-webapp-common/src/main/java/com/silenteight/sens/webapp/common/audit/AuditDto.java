package com.silenteight.sens.webapp.common.audit;

import org.hibernate.envers.RevisionType;

public interface AuditDto {

  RevisionType getRevisionType();

  default String getAuditedOperation() {
    switch (getRevisionType()) {
      case ADD:
        return "INSERT";
      case DEL:
        return "DELETE";
      case MOD:
      default:
        return "UPDATE";
    }
  }
}
