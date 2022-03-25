package com.silenteight.connector.ftcc.callback.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.resource.BatchResource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
class BatchCompletedService {

  private final BatchCompletedRepository batchCompletedRepository;

  @Async
  @Transactional
  public void save(String batchId, String analysisId) {
    batchCompletedRepository.save(BatchCompletedEntity.builder()
        .batchId(BatchResource.fromResourceName(batchId))
        .analysisId(analysisId)
        .build());
  }
}
