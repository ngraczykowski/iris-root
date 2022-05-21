package com.silenteight.hsbc.bridge.nexus;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.nexus.api")
@Value
class NexusApiProperties {

  String username;
  String password;
}
