package com.silenteight.payments.bridge.svb.learning.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.data.retention.model.FilesExpiredEvent;
import com.silenteight.payments.bridge.svb.learning.port.LearningDataAccess;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
class DeleteFileDataService {

  private final LearningDataAccess learningDataAccess;

  @EventListener
  public void removeFileData(FilesExpiredEvent filesExpiredEvent) {
    log.info("Received expired files to remove data = {}", filesExpiredEvent.getFileNames());
    learningDataAccess.removeFileData(filesExpiredEvent.getFileNames());
  }
}
