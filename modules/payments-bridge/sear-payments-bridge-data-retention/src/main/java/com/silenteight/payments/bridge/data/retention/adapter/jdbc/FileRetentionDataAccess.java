package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.adapter.FileDataRetentionAccessPort;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class FileRetentionDataAccess implements FileDataRetentionAccessPort {

  private final FindFileDataRetention findFileDataRetention;

  @Override
  public List<String> findFileNameBefore(OffsetDateTime dateTime) {
    return findFileDataRetention.findAlertTimeBefore(dateTime);
  }
}
