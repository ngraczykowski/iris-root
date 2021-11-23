package com.silenteight.serp.governance.model.archive.amqp;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ModelArchivingProperties.class)
class ModelsArchivedAmqpConfiguration {
}
