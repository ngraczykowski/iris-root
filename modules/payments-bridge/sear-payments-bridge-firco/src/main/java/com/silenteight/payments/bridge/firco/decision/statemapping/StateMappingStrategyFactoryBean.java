package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
class StateMappingStrategyFactoryBean implements FactoryBean<StateMappingStrategy>,
    BeanFactoryAware, ResourceLoaderAware {

  @NonNull
  private final String strategyName;

  @NonNull
  private final String csvFilePath;

  @Setter(onMethod_ = @Override)
  private BeanFactory beanFactory;

  @Setter(onMethod_ = @Override)
  private ResourceLoader resourceLoader;

  @Override
  public StateMappingStrategy getObject() throws IOException {
    var patternTupleLoader = beanFactory.getBean(CsvPatternTupleLoader.class);
    var resource = resourceLoader.getResource(csvFilePath);

    try (var reader = new InputStreamReader(resource.getInputStream())) {
      var patternTuples = patternTupleLoader.load(reader);
      return new PatternStateMappingStrategy(strategyName, patternTuples);
    }
  }

  @Override
  public Class<?> getObjectType() {
    return StateMappingStrategy.class;
  }
}
