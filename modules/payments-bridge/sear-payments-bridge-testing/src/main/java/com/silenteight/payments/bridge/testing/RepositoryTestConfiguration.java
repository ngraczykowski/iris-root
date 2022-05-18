package com.silenteight.payments.bridge.testing;

import com.silenteight.payments.bridge.SpringBootTestConfiguration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackageClasses = SpringBootTestConfiguration.class)
@EntityScan(basePackageClasses = SpringBootTestConfiguration.class)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class RepositoryTestConfiguration {
}
