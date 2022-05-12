package com.silenteight.payments.bridge.app.batch;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("pb.batch.job")
@Data
@Validated
class BatchProcessingProperties {

  private static final int DEFAULT_CONCURRENCY_LIMIT = 10;

  private int concurrencyLimit = DEFAULT_CONCURRENCY_LIMIT;
}
