package com.silenteight.serp.governance.ingest.repackager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;

@RequiredArgsConstructor
public class IngestDataValidator {

  @NonNull
  private final String fvSignatureKey;

  public boolean containsFvKey(ProductionDataIndexRequest event) {
    return event
        .getAlertsList()
        .stream()
        .findAny()
        .map(Alert::getPayload)
        .map(struct -> struct.containsFields(fvSignatureKey))
        .orElse(Boolean.FALSE);
  }
}
