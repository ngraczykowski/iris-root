package com.silenteight.searpayments.scb.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@RequiredArgsConstructor
class DomainModuleConfiguration {

    private final AlertRepository alertRepository;

    @Bean
    AlertService alertService() {
        return new AlertService(alertRepository);
    }

}
