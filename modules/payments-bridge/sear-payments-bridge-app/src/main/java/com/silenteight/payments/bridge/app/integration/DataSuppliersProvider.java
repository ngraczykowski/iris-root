package com.silenteight.payments.bridge.app.integration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@Configuration
class DataSuppliersProvider {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AlertDataSupplier alertDataSupplierPrototype(UUID identifier) {
    return new AlertDataSupplier(identifier);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AlertDtoSupplier alertDtoSupplierPrototype(UUID identifier) {
    return new AlertDtoSupplier(identifier);
  }

}
