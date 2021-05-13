package com.silenteight.adjudication.engine.comments.domain;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchTemplateModel {

  private String matchId;
  private Map<String, Object> commentInput;
  private String solution;
  @Singular
  private Map<String, CategoryTemplateModel> categories;
  @Singular
  private Map<String, FeatureTemplateModel> features;
}
