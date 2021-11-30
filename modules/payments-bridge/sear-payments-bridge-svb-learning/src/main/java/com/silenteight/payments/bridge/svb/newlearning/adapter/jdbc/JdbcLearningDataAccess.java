package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningFile;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class JdbcLearningDataAccess implements LearningDataAccess {

  private final SelectLearningFilesQuery selectNewFilesQuery;
  private final UpdateLearningFileQuery updateLearningFileQuery;


  @Override
  public Optional<LearningFile> findLearningFileById(Long fileId) {
    return selectNewFilesQuery.execute(fileId);
  }

  @Override
  @Transactional
  public void updateFileStatus(Long fileId, CsvProcessingFileStatus status) {
    updateLearningFileQuery.update(fileId, status);
  }
}
