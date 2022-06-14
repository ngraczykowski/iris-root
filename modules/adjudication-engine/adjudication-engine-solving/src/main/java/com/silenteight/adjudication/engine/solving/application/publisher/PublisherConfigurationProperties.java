/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "ae.solving.in-memory.publisher")
class PublisherConfigurationProperties {
  private static final int DEFAULT_POOL_SIZE = 15;
  private static final int DEFAULT_STORE_POOL_SIZE = 1;

  private final int readyMatchFeatureVectorPublisher = DEFAULT_POOL_SIZE;
  // BA publishers
  private final int governanceAlertPublisher = DEFAULT_POOL_SIZE;
  private final int categoryResolvePublisher = DEFAULT_POOL_SIZE;
  private final int commentInputResolvePublisher = DEFAULT_POOL_SIZE;
  // Storage publishers.
  private final int commentInputStorePublisher = DEFAULT_STORE_POOL_SIZE;
  private final int matchCategoryPublisher = DEFAULT_STORE_POOL_SIZE;
  private final int matchFeaturePublisher = DEFAULT_STORE_POOL_SIZE;
}
