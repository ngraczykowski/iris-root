package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class FeatureContext {

  String agentConfig;
  String solution;
  Map<String, Object> reason;
}
