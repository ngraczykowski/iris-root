package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.BatchId;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchService.register(registerBatchCommand);
  }

  public void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchService.notifyBatchError(notifyBatchErrorCommand);
  }
}
