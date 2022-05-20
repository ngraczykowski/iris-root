package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class AlertContext {

  /**
   * ae_alert.client_alert_identifier
   */
  String alertId;
  Map<String, Object> commentInput;
  String recommendedAction;
  @Singular
  List<MatchContext> matches;
}
