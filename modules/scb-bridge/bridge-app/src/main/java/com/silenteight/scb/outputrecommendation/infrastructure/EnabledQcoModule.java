package com.silenteight.scb.outputrecommendation.infrastructure;

import com.silenteight.qco.QcoFacade;
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation;
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(value = "silenteight.scb-bridge.qco.enabled", havingValue = "true")
@EnableJpaRepositories(basePackages = "com.silenteight.scb",
    basePackageClasses = QcoOverriddenRecommendationJpaRepository.class)
@EntityScan(basePackages = "com.silenteight.scb",
    basePackageClasses = QcoOverriddenRecommendation.class)
@ComponentScan({ "com.silenteight.qco" })
class EnabledQcoModule {

  @Bean
  public QcoRecommendationProvider qcoRecommendationProvider(QcoFacade qcoFacade) {
    return alert -> qcoFacade.process(alert);
  }
}
