package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.adapter.FileDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class FileRetentionDataAccess implements FileDataRetentionAccessPort {

  private final FindFileDataRetention findFileDataRetention;
  private final InsertFileDataRetention insertFileDataRetention;
  private final UpdateFileDataRetention updateFileDataRetention;

  @Override
  public List<String> findFileNameBefore(OffsetDateTime dateTime) {
    return findFileDataRetention.findAlertTimeBefore(dateTime);
  }

  @Override
  @Transactional
  public void create(Iterable<FileDataRetention> fileDataRetentions) {
    insertFileDataRetention.update(fileDataRetentions);
  }

  @Override
  @Transactional
  public void update(List<String> fileNames) {
    updateFileDataRetention.update(fileNames);
  }
}
