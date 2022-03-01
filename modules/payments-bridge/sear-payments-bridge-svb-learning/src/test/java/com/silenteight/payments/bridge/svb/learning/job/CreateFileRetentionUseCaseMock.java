package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;

import javax.annotation.Nonnull;

class CreateFileRetentionUseCaseMock implements CreateFileRetentionUseCase {

  @Override
  public void create(
      @Nonnull Iterable<FileDataRetention> dataRetentions) {

  }
}
