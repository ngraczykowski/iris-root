package com.silenteight.adjudication.engine.testing;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.silenteight.adjudication.engine")
@EntityScan(basePackages = "com.silenteight.adjudication.engine")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class RepositoryTestConfiguration {
}
