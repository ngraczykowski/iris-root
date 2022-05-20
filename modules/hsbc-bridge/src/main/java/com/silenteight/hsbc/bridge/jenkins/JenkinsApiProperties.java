package com.silenteight.hsbc.bridge.jenkins;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.jenkins.api")
@Value
class JenkinsApiProperties {

  String crumbUri;
  String updateModelUri;
  String updateModelStatusUri;
  String username;
  String password;
}
