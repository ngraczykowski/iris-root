package com.silenteight.payments.bridge.app;

import com.silenteight.payments.bridge.PaymentsBridgeModule;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = PaymentsBridgeModule.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = PaymentsBridgeApplication.class))
@EnableIntegration
@EnableIntegrationGraphController
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@EntityScan(basePackageClasses = PaymentsBridgeModule.class)
@IntegrationComponentScan(basePackageClasses = PaymentsBridgeModule.class)
public class TestApplicationConfiguration {
}
