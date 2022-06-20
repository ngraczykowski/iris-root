/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure;

import com.silenteight.iris.qco.QcoFacade;
import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation;
import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(value = "silenteight.qco.enabled", havingValue = "true")
@EnableJpaRepositories(basePackages = "com.silenteight.iris.bridge.scb",
    basePackageClasses = QcoOverriddenRecommendationJpaRepository.class)
@EntityScan(basePackages = "com.silenteight.iris.bridge.scb",
    basePackageClasses = QcoOverriddenRecommendation.class)
@ComponentScan({ "com.silenteight.iris.qco" })
class EnabledQcoModule {

  @Bean
  public QcoRecommendationProvider qcoRecommendationProvider(QcoFacade qcoFacade) {
    return qcoFacade::process;
  }
}
