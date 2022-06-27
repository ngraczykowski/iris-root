package com.silenteight.simulator.processing.alert.index.domain;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(IndexedAlertDomainConfiguration.class)
class IndexedAlertTestConfiguration {
}
