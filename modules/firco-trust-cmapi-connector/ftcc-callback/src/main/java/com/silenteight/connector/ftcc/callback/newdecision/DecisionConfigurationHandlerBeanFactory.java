package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStreamReader;

@RequiredArgsConstructor
@Slf4j
class DecisionConfigurationHandlerBeanFactory
    implements FactoryBean<DecisionConfigurationHolder>, ResourceLoaderAware {

  private final String resourceLocation;

  @Setter(onMethod_ = @Override)
  private ResourceLoader resourceLoader;

  @NonNull
  @Override
  public DecisionConfigurationHolder getObject() throws Exception {
    var resource = resourceLoader.getResource(resourceLocation);

    try (var reader = new InputStreamReader(resource.getInputStream())) {
      return new DecisionConfigurationHolder(DecisionTransitionCsvReader.decision(reader));
    }
  }

  @Override
  public Class<?> getObjectType() {
    return DecisionConfigurationHolder.class;
  }
}
