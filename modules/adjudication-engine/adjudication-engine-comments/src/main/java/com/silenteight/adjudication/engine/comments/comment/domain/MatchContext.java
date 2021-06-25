package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchContext {
  /**
   * ae_match.client_match_identifier
   */
  private String matchId;
  private String solution;
  private Map<String, Object> reason;
  /**
   * Map of a category name to its value for the match.
   */
  @Singular
  private Map<String, String> categories;
  /**
   * Map of feature name to the feature value context.
   */
  @Singular
  private Map<String, FeatureContext> features;
}
