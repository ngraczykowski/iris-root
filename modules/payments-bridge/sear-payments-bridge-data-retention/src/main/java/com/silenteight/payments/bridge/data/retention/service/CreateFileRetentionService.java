package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.adapter.FileDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateFileRetentionService implements CreateFileRetentionUseCase {

  private final FileDataRetentionAccessPort dataRetentionAccessPort;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public void create(Iterable<FileDataRetention> dataRetentions) {
    dataRetentionAccessPort.create(dataRetentions);
  }
}
