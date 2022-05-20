package com.silenteight.warehouse.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.persistence.ProductionPersistenceModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;

@ComponentScan(basePackageClasses = {
    ProductionPersistenceModule.class
})
@EnableAutoConfiguration
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
class ProductionMessagePersistenceTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return ARBITRARY_INSTANCE;
  }
}
