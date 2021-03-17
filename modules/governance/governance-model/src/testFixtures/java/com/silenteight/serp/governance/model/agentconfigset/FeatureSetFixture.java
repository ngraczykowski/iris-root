package com.silenteight.serp.governance.model.agentconfigset;

import com.silenteight.serp.governance.model.featureset.FeatureDto;
import com.silenteight.serp.governance.model.featureset.FeatureSetDto;

import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_RESPONSE_MATCH;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_RESPONSE_NO_DATA;
import static com.silenteight.serp.governance.model.featureset.FeatureSetRegistry.DEFAULT_FEATURE_SET;
import static java.util.List.of;

public class FeatureSetFixture {

  public static final FeatureDto FEATURE_NAME = FeatureDto.builder()
      .name(AGENT_FEATURE_NAME)
      .agentConfig(NAME_AGENT_CONFIG_NAME)
      .values(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final FeatureSetDto FEATURE_CONFIG_SET = FeatureSetDto.builder()
      .name(DEFAULT_FEATURE_SET)
      .features(of(FEATURE_NAME))
      .build();
}
