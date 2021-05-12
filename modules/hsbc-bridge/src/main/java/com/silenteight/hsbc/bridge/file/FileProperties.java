package com.silenteight.hsbc.bridge.file;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.file")
@Value
class FileProperties {

  String directory;
}
