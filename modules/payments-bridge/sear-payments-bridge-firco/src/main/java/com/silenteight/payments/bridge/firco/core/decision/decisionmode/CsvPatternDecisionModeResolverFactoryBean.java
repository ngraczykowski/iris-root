package com.silenteight.payments.bridge.firco.core.decision.decisionmode;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStreamReader;

@RequiredArgsConstructor
class CsvPatternDecisionModeResolverFactoryBean implements FactoryBean<DecisionModeResolver>,
    BeanFactoryAware, ResourceLoaderAware {

  private final String csvFilePath;

  @Setter(onMethod_ = @Override)
  private BeanFactory beanFactory;

  @Setter(onMethod_ = @Override)
  private ResourceLoader resourceLoader;

  @Override
  public DecisionModeResolver getObject() throws Exception {
    var factory = beanFactory.getBean(CsvPatternDecisionModeResolverFactory.class);
    var resource = resourceLoader.getResource(csvFilePath);

    try (var reader = new InputStreamReader(resource.getInputStream())) {
      return factory.create(reader);
    }
  }

  @Override
  public Class<?> getObjectType() {
    return DecisionModeResolver.class;
  }
}
