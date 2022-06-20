/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure;

import com.silenteight.iris.bridge.scb.feeding.domain.category.ApTypeCategoryValueCreator;
import com.silenteight.iris.bridge.scb.feeding.domain.category.IsDenyCategoryValueCreator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryValueConfiguration {

  @Bean
  ApTypeCategoryValueCreator apTypeCategoryValueCreator() {
    return new ApTypeCategoryValueCreator();
  }

  @Bean
  IsDenyCategoryValueCreator isDenyCategoryValueCreator() {
    return new IsDenyCategoryValueCreator();
  }
}
