package com.silenteight.scb;

import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation;
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.silenteight.scb",
    basePackageClasses = QcoOverriddenRecommendationJpaRepository.class)
@EntityScan(basePackages = "com.silenteight.scb",
    basePackageClasses = QcoOverriddenRecommendation.class)
@EnableTransactionManagement
@EnableRetry
public class ScbBridgeApplication {
  public static void main(String[] args) {
    SpringApplication.run(ScbBridgeApplication.class, args);
  }
}
