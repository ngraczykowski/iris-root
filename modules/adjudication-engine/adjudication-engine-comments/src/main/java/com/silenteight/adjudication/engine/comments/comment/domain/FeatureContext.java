package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Map;

@Value
@Builder
public class FeatureContext {

  String agentConfig;
  String solution;
  Map<String, Object> reason;

  @JsonGetter("agentName")
  public String getAgentName() {
    return "agents/" + ResourceName.create(agentConfig).get("agents");
  }
}
