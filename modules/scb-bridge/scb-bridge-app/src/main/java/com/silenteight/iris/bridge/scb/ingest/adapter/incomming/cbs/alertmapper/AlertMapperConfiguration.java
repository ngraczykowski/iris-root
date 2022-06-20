/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertMapperProperties.class)
class AlertMapperConfiguration {

  private final AlertMapperProperties properties;

  @Bean
  AlertMapper alertMapper() {
    var dateConverter = new DateConverter(properties.getTimeZone());
    var matchCollector = new MatchCollector();
    var suspectsCollector = new SuspectsCollector(new HitDetailsParser());

    return new AlertMapper(dateConverter, matchCollector, suspectsCollector);
  }
}
