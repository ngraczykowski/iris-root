package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import java.util.Map;

@Value
@Builder
public class FeatureContext {

  String agentConfig;
  String solution;
  Map<String, Object> reason;

  public String getAgentName() {
    return "agents/" + ResourceName.create(agentConfig).get("agents");
  }
}
