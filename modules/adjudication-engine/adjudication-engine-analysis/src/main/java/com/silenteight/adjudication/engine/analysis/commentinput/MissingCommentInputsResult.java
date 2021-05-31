package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MissingCommentInputsResult {

  private final List<String> alerts;

  public boolean hasNoAlerts() {
    return alerts.isEmpty();
  }

  public MissingCommentInputsResult addAlert(String alertId) {
    alerts.add(alertId);
    return this;
  }

  public int count() {
    return alerts.size();
  }
}
