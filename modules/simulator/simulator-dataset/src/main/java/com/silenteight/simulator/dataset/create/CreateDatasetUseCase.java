package com.silenteight.simulator.dataset.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.create.exception.EmptyDatasetException;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import java.util.List;

@RequiredArgsConstructor
public class CreateDatasetUseCase {

  @NonNull
  private final CreateDatasetService createDatasetService;
  @NonNull
  private final DatasetMetadataService datasetMetadataService;
  @NonNull
  private final List<DatasetLabel> labels;
  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateDatasetRequest request) {
    request.preAudit(auditingLogger::log);
    request.addLabels(labels);
    Dataset dataset = createDatasetService.createDataset(request);

    if (dataset.getAlertCount() == 0)
      throw new EmptyDatasetException();

    datasetMetadataService.createMetadata(request, dataset);
    request.postAudit(auditingLogger::log);
  }
}
