package com.silenteight.qco.infrastructure.parser;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.qco.config-file")
record SolutionConfigurationFileProperties(String location, char separator) {}
