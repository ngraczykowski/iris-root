package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Data;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;

@Data
public class BranchSearchFilterDto {

  private long decisionTreeId;
  private String query;

  public void setQ(String query) {
    this.query = query;
  }

  public DecisionTreeId getDomainObject() {
    return DecisionTreeId.of(decisionTreeId);
  }
}
