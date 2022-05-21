package com.silenteight.hsbc.bridge;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge")
@Value
public class BridgeApiProperties {

  String address;
}
