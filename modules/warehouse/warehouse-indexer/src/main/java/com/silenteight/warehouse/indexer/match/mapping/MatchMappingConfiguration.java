package com.silenteight.warehouse.indexer.match.mapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.support.PayloadConverter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(MatchMappingProperties.class)
@RequiredArgsConstructor
class MatchMappingConfiguration {

  @Valid
  private final MatchMappingProperties matchMappingProperties;

  @Bean
  MatchMapper matchMapper(TimeSource timeSource) {
    return new MatchMapper(timeSource, payloadConverter());
  }

  private PayloadConverter payloadConverter() {
    return new PayloadConverter(matchMappingProperties.getIgnoredKeys());
  }
}
