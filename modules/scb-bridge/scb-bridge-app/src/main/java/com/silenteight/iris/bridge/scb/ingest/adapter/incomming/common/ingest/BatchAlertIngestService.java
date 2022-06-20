/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext;

import java.util.List;

public interface BatchAlertIngestService {

  void ingestAlertsForLearn(String internalBatchId, List<Alert> alerts);

  IngestedAlertsStatus ingestAlertsForRecommendation(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext);
}
