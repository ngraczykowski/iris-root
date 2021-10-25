package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.support.PayloadConverter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
    ElasticsearchProperties.class,
    AlertMappingProperties.class
})
class AlertMappingConfiguration {

  @Valid
  private final AlertMappingProperties alertMappingProperties;

  @Bean
  AlertMapper alertMapper(TimeSource timeSource) {
    return new AlertMapper(timeSource, alertMappingProperties, payloadConverter());
  }

  private PayloadConverter payloadConverter() {
    return new PayloadConverter(alertMappingProperties.getIgnoredKeys());
  }
}
