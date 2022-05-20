package com.silenteight.hsbc.bridge.unpacker;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.unpacker")
@Value
class UnpackerProperties {

  String path;
}
