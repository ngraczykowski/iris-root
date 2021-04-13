package com.silenteight.hsbc.bridge.http.security;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(ignoreInvalidFields = true, prefix = "silenteight.bridge.security")
@Value
@ConstructorBinding
public class HsbcSecurityConfigurationProperties {

  List<String> endpoints;
}
