package com.silenteight.serp.governance.model.category;

import com.silenteight.serp.governance.model.ModelProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties(ModelProperties.class)
class CategoryConfiguration {

  @Bean
  CategoryRegistry categoryRegistry(
      ObjectMapper objectMapper,
      ModelProperties modelProperties,
      ResourceLoader resourceLoader) {

    return new CategoryRegistry(modelProperties.getCategorySource(), objectMapper, resourceLoader);
  }
}
