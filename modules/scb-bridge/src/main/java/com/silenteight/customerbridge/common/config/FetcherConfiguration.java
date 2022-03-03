package com.silenteight.customerbridge.common.config;

import lombok.Data;

@Data
public class FetcherConfiguration {

  private final String dbRelationName;
  private final int timeout;
}