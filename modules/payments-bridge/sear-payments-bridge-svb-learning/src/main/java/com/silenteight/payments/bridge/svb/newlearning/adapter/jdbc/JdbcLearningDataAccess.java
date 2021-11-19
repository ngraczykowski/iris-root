package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class JdbcLearningDataAccess implements LearningDataAccess {

  private final InsertNonExistingFileNamesQuery insertNonExistingFileNamesQuery;

  @Override
  @Transactional
  public List<String> saveNonProcessedFileNames(List<ObjectPath> names) {
    return insertNonExistingFileNamesQuery.execute(names);
  }
}
