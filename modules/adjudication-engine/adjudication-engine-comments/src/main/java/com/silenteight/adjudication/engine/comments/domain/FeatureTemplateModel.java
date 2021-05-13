package com.silenteight.adjudication.engine.comments.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureTemplateModel {

  private String agentName;
  private String result;
  private Map<String,Object> reason;
}
