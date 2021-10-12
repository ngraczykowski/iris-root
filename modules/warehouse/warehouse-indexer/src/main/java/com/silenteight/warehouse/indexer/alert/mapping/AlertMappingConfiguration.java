package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;
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
    return new AlertMapper(timeSource, alertMappingProperties, getIgnoredKeysStrategy());
  }

  Predicate<String> getIgnoredKeysStrategy() {
    return new SkipAnyMatchingKeysStrategy(alertMappingProperties.getIgnoredKeys());
  }
}
