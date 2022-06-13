package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ftcc.decision")
class DecisionConfigurationProperties {

  String resourceLocation = "classpath:decision/decision.csv";
}
