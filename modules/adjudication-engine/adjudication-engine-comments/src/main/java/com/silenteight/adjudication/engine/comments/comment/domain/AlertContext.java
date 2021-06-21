package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertContext {

  /**
   * ae_alert.client_alert_identifier
   */
  private String alertId;
  private Map<String, Object> commentInput;
  private String recommendedAction;
  @Singular
  private List<MatchContext> matches;
}
