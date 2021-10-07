package com.silenteight.adjudication.engine.analysis.commentinput.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class MissingCommentInputsResult {

  @ToString.Include
  private final List<String> alerts;

  public boolean hasNoAlerts() {
    return alerts.isEmpty();
  }

  public int count() {
    return alerts.size();
  }
}
