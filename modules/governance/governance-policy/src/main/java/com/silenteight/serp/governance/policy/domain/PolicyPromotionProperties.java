package com.silenteight.serp.governance.policy.domain;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "serp.governance.policy.promotion")
class PolicyPromotionProperties {

  private boolean direct;
}
