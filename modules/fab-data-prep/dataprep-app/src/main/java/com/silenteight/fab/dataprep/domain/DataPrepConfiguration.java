package com.silenteight.fab.dataprep.domain;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableRetry
@EnableAsync
@EnableConfigurationProperties(AlertStateProperties.class)
class DataPrepConfiguration {

}
