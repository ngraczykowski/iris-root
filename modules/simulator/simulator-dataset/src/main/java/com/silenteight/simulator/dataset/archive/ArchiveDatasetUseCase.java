package com.silenteight.simulator.dataset.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

@RequiredArgsConstructor
public class ArchiveDatasetUseCase {

  @NonNull
  private final DatasetMetadataService datasetMetadataService;
  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(ArchiveDatasetRequest request) {
    request.preAudit(auditingLogger::log);
    datasetMetadataService.archive(request.getId());
    request.postAudit(auditingLogger::log);
  }
}
