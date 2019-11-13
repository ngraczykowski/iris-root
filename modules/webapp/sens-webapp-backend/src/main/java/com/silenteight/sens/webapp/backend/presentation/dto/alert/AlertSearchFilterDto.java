package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;

@Data
public class AlertSearchFilterDto {

  private Long matchGroupId;
  private Long decisionTreeId;
  private AlertCategory category = AlertCategory.ALL;

  public long getMatchGroupId() {
    if (matchGroupId == null)
      throw new IllegalArgumentException("MatchGroupId can not be null");
    return matchGroupId;
  }

  public long getDecisionTreeId() {
    if (decisionTreeId == null)
      throw new IllegalArgumentException("DecisionTreeId can not be null");
    return decisionTreeId;
  }

  public AlertCategory getCategory() {
    if (category == null)
      throw new IllegalArgumentException("Category can not be null");
    return category;
  }
}
