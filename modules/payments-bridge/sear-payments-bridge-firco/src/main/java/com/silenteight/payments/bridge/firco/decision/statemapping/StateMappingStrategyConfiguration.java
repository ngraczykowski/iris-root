package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(StateMappingStrategyProperties.class)
@RequiredArgsConstructor
class StateMappingStrategyConfiguration {

  private final StateMappingStrategyProperties properties;
  private final ConfigurableApplicationContext applicationContext;
  private final ResourceLoader resourceLoader;

  @PostConstruct
  public void init() {
    var beanFactory = applicationContext.getBeanFactory();
    properties.getStrategies().forEach((name, csvFilePath) -> {
      var factoryBean = new StateMappingStrategyFactoryBean(name, csvFilePath);
      factoryBean.setBeanFactory(beanFactory);
      factoryBean.setResourceLoader(resourceLoader);
      beanFactory.registerSingleton("stateMappingStrategy-" + name, factoryBean);
    });
  }
}
