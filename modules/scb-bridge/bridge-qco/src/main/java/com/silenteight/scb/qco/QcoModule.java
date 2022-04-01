package com.silenteight.scb.qco;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "silenteight.qco.config-file", name = "location")
@ComponentScan(basePackages = "com.silenteight.qco")
public class QcoModule {
}
