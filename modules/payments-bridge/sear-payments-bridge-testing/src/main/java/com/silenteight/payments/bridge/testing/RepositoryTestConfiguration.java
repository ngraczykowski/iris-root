package com.silenteight.payments.bridge.testing;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.silenteight.payments.bridge")
@EntityScan(basePackages = "com.silenteight.payments.bridge")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class RepositoryTestConfiguration {
}
