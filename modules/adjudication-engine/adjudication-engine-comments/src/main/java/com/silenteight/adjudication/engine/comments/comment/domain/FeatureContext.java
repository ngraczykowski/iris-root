package com.silenteight.adjudication.engine.comments.comment.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.engine.common.resource.ResourceName;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.Map;

@Value
@Builder
public class FeatureContext {

  String agentConfig;
  String solution;
  Map<String, Object> reason;

  @JsonGetter("agentName")
  @JsonProperty(access = Access.READ_ONLY)
  public String getAgentName() {
    return "agents/" + ResourceName.create(agentConfig).get("agents");
  }
}
