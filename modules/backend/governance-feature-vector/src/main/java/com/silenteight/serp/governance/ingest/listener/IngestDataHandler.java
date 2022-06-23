package com.silenteight.serp.governance.ingest.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.serp.governance.ingest.repackager.IngestDataToSolvedEventRepackagerService;
import com.silenteight.serp.governance.ingest.repackager.IngestDataValidator;
import com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategyService;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

@Slf4j
@RequiredArgsConstructor
public class IngestDataHandler {

  @NonNull
  private final IngestDataToSolvedEventRepackagerService repackager;
  @NonNull
  private final IngestDataValidator validator;
  @NonNull
  private final FeatureVectorEventStrategyService featureVectorEventStrategyService;

  public FeatureVectorSolvedEventBatch handle(ProductionDataIndexRequest event) {
    log.debug("Received index data request with {} alerts.", event.getAlertsCount());

    if (featureVectorEventStrategyService.isSolve())
      return null;

    if (event.getAlertsCount() == 0)
      return null;

    if (!validator.containsFvKey(event))
      return null;

    log.info("Received ingest data with with {} alerts.", event.getAlertsCount());
    return repackager.activate(event.getAlertsList());
  }
}
