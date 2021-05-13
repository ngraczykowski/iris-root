package com.silenteight.adjudication.engine.comments.domain;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertTemplateModel {

  private String alertId;
  private Map<String, Object> commentInput;
  private String recommendedAction;
  @Singular
  private List<MatchTemplateModel> matches;
}
