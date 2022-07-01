/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "ae.solving.in-memory.publisher")
class PublisherConfigurationProperties {

  private static final int DEFAULT_CORE_POOL_SIZE = 8;
  private static final int DEFAULT_MAX_POOL_SIZE = 128;
  private static final int DEFAULT_QUEUE_CAPACITY = 1024 * 100000;

  private final int corePoolSize = DEFAULT_CORE_POOL_SIZE;
  private final int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
  private final int queueCapacity = DEFAULT_QUEUE_CAPACITY;
}
