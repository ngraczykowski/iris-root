package com.silenteight.simulator.dataset.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

@RequiredArgsConstructor
public class CreateDatasetUseCase {

  @NonNull
  private final CreateDatasetService createDatasetService;
  @NonNull
  private final DatasetMetadataService datasetMetadataService;
  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateDatasetRequest request) {
    request.preAudit(auditingLogger::log);
    Dataset dataset = createDatasetService.createDataset(request);
    datasetMetadataService.createMetadata(request, dataset);
    request.postAudit(auditingLogger::log);
  }
}
